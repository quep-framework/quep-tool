/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.es.dsic.quep.utils;

import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
import es.upv.dsic.quep.dao.PracticeDaoImplement;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.dao.ResultsDaoImplement;
import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.MaturityLevelPractice;
import es.upv.dsic.quep.model.Practice;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestionId;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.Role;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.primefaces.component.menuitem.MenuItem;
/**
 *
 * @author agna8685
 */
public class Results implements Serializable {

    public Results() {
    }

    public Map<QuepQuestion, ResponseEstimate> calculateQuestions(int organizationId) {
        ResultsDaoImplement rdi = new ResultsDaoImplement();

        //1.Evaluation Responses
        //SUM REQ Weight
        List<QuepQuestionResponseOption> lstQQuestionResponseOption = new ArrayList<QuepQuestionResponseOption>();
        lstQQuestionResponseOption = rdi.getQuepQuestionResponseOption(organizationId);

        //2.Calculate Responses
        List<Response> lstResult = new ArrayList<Response>();
        lstResult = rdi.getListResponse(organizationId);

        Map<QuepQuestion, ResponseEstimate> mapSumResponseOp = new HashMap<QuepQuestion, ResponseEstimate>();
        for (QuepQuestionResponseOption oQQRO : lstQQuestionResponseOption) {
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal sumReqQQuestion = BigDecimal.ZERO;        //cambiar mapeo de int a double      
            BigDecimal sumQQuestion = BigDecimal.ZERO;
            BigDecimal avg = BigDecimal.ZERO;

            //Get weight SUMs (included requiered SUM)
            for (QuepQuestionResponseOption oQQRO_aux : lstQQuestionResponseOption) {
                if (oQQRO_aux.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumQQuestion = sumQQuestion.add(oQQRO_aux.getResponseOption().getWeight());
                    if (oQQRO_aux.getResponseOption().getIsRequiered() == 1) {
                        sumReqQQuestion = sumReqQQuestion.add(oQQRO_aux.getResponseOption().getWeight());
                    }
                }
            }

            //Get Responses SUMs of an Organization 
            BigDecimal sumRsp = BigDecimal.ZERO;
            for (Response oResponse : lstResult) {
                if (oResponse.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumRsp = sumRsp.add(oResponse.getResponseOption().getWeight());
                }
            }
            //average Responses SUMs / Requiered SUM of Questions Configurated in QuEP
            if (sumReqQQuestion == BigDecimal.valueOf(0.0) || sumReqQQuestion.equals(BigDecimal.valueOf(0))) {
                sumReqQQuestion = BigDecimal.valueOf(1.0);
            }
            avg = sumRsp.divide(sumReqQQuestion, 2, RoundingMode.HALF_EVEN);

            //setting map Reponses SUMs of Quep Questions
            oREstimate.setSumRequiered(sumReqQQuestion);
            oREstimate.setSum(sumQQuestion);
            oREstimate.setAvg(avg);
            mapSumResponseOp.put(oQQRO.getQuepQuestion(), oREstimate);
        }

        return mapSumResponseOp;
    }

    public Map<Practice, ResponseEstimate> calculatePracticesBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, String sPrincipleId) {
        Map<Practice, ResponseEstimate> mapPracticeEstimate = new HashMap<Practice, ResponseEstimate>();
        List<Practice> lstPractice = new ArrayList<Practice>();

        PracticeDaoImplement prdao = new PracticeDaoImplement();
        lstPractice = prdao.getPractice();

        for (Practice oPr : lstPractice) {
            if (oPr.getPrinciple().getId() == Integer.parseInt(sPrincipleId)) {
                int size = 0;
                ResponseEstimate oREstimate = new ResponseEstimate();
                BigDecimal avg = BigDecimal.ZERO;
                for (Map.Entry<QuepQuestion, ResponseEstimate> oSumQ : mapSumQuepQuestions.entrySet()) {
                    QuepQuestion qq = oSumQ.getKey();
                    ResponseEstimate rsp = oSumQ.getValue();
                    if (qq.getPractice().getId() == oPr.getId()) {
                        avg = avg.add(rsp.getAvg());
                        size++;
                    }
                }
                if (size == 0) {
                    size = 1;
                }
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
                mapPracticeEstimate.put(oPr, oREstimate);
            }
        }
        return mapPracticeEstimate;
    }

    public Map<Role,ResponseEstimate> calculateRolesBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, int idOrganization) {
        Map<Role, ResponseEstimate> mapRoleQQQuestionEstimate = new HashMap<Role,  ResponseEstimate>();
        Map<QuestionnaireQuepQuestion, ResponseEstimate> mapQQQEstimate = new HashMap<QuestionnaireQuepQuestion, ResponseEstimate>();

        List<Role> lstRole = new ArrayList<Role>();
        RoleStakeholderDaoImplement rdao = new RoleStakeholderDaoImplement();
        lstRole = rdao.getLstRoles();

        List<QuestionnaireQuepQuestion> lstQQQ = new ArrayList<QuestionnaireQuepQuestion>();
        QuestioannaireDaoImplement qqdi = new QuestioannaireDaoImplement();
        lstQQQ = qqdi.getQuestionnairesQQbyOrg(idOrganization);

        for (Role oRole : lstRole) {
            if (oRole.getId() < 6) {
                int size = 0;
                ResponseEstimate oREstimate = new ResponseEstimate();
                BigDecimal avg = BigDecimal.ZERO;

                for (QuestionnaireQuepQuestion qqq : lstQQQ) {
                    if (qqq.getQuestionnaire().getRolePractice().getRole().getId() == oRole.getId()) {
                        for (Map.Entry<QuepQuestion, ResponseEstimate> oSumQ : mapSumQuepQuestions.entrySet()) {
                            QuepQuestion qq = oSumQ.getKey();
                            if (qqq.getQuepQuestion().getId() == qq.getId()) {
                                ResponseEstimate rsp = oSumQ.getValue();
                                avg = avg.add(rsp.getAvg());
                                size++;
                            }
                        }
                        if (size == 0) {
                            size = 1;
                        }
                    }
                    //oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
                    //mapQQQEstimate.put(qqq, oREstimate);
                }
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
                mapRoleQQQuestionEstimate.put(oRole, oREstimate);
            }
        }
        return mapRoleQQQuestionEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrincipleBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<Principle> lstPrinciple) {
        Map<Principle, ResponseEstimate> mapPrincipleEstimate = new HashMap<Principle, ResponseEstimate>();

        for (Principle p : lstPrinciple) {
            int size = 0;
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal avg = BigDecimal.ZERO;
            for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                QuepQuestion qq = mQQ.getKey();
                ResponseEstimate rsp = mQQ.getValue();
                if (qq.getPractice().getPrinciple().getId() == p.getId()) {
                    avg = avg.add(rsp.getAvg());
                    size++;
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
            mapPrincipleEstimate.put(p, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<MaturityLevel, ResponseEstimate> calculateMaturityLevelBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<MaturityLevel> lstMaturityLevel) {

        Map<MaturityLevel, ResponseEstimate> mapMaturityLevelEstimate = new HashMap<MaturityLevel, ResponseEstimate>();

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();
        //PrincipleDaoImplement pdi = new PrincipleDaoImplement();

        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();

        //List<MaturityLevel> lstMaturityLevel = new ArrayList<MaturityLevel>();
        //lstMaturityLevel = mldao.getMaturityLevels();
        ResponseEstimate oREstimate = new ResponseEstimate();

        BigDecimal sumLastLevel = BigDecimal.ZERO;
        int sizeLevels = 0;
        MaturityLevel oLastMaturityLevel = new MaturityLevel();
        for (MaturityLevel ml : lstMaturityLevel) {
            if (ml.getId() != 10) { //add un campo mas que indique que es el último nivel en la BD
                oREstimate = new ResponseEstimate();
                BigDecimal avg = BigDecimal.ZERO;
                int size = 0;
                //for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                //if(mlp.getId().getIdMaturityLevel()==ml.getId()){
                for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                    QuepQuestion qq = mQQ.getKey();
                    ResponseEstimate rsp = mQQ.getValue();
                    if ( //qq.getPractice().getId() == mlp.getId().getIdPractice()
                            //&&
                            qq.getMaturityLevel().getId() == ml.getId()) //&& mlp.getId().getIdMaturityLevel() == ml.getId()) 
                    {
                        avg = avg.add(rsp.getAvg());
                        size++;
                    }
                    //  }
                }
                if (size == 0) {
                    size = 1;
                }
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
                mapMaturityLevelEstimate.put(ml, oREstimate);

                sumLastLevel = sumLastLevel.add(avg);
                sizeLevels++;
            } else {
                oLastMaturityLevel = ml;
            }
        }

        //setting last level
        oREstimate = new ResponseEstimate();
        oREstimate.setAvg(sumLastLevel.divide(BigDecimal.valueOf(sizeLevels), 2, RoundingMode.HALF_EVEN));
        mapMaturityLevelEstimate.put(oLastMaturityLevel, oREstimate);

        return mapMaturityLevelEstimate;

    }

    public Map<Principle, ResponseEstimate> calculatePrincipleBySumPractice(Map<Practice, ResponseEstimate> mapSumPractices, List<Principle> lstPrinciple) {
        Map<Principle, ResponseEstimate> mapPrincipleEstimate = new HashMap<Principle, ResponseEstimate>();

        for (Principle p : lstPrinciple) {
            int size = 0;
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal avg = BigDecimal.ZERO;
            for (Map.Entry<Practice, ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                Practice pr = mPra.getKey();
                ResponseEstimate rsp = mPra.getValue();
                if (pr.getPrinciple().getId() == p.getId()) {
                    avg = avg.add(rsp.getAvg());
                    size++;
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
            mapPrincipleEstimate.put(p, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrinciplesByIdMaturityLevel(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<Principle> lstPrinciple, String sMaturityLevelId) {
        Map<Principle, ResponseEstimate> mapPrincipleEstimate
                = new HashMap<Principle, ResponseEstimate>();

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();
        //PrincipleDaoImplement pdi = new PrincipleDaoImplement();

        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();

        // List<Principle> lstPrinciple = new ArrayList<Principle>();
        // lstPrinciple = pdi.getPrinciple();
        for (Principle pri : lstPrinciple) {
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal avg = BigDecimal.ZERO;
            int size = 0;
            for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                if (mlp.getId().getIdMaturityLevel() == Integer.parseInt(sMaturityLevelId)) {
                    for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                        QuepQuestion qq = mQQ.getKey();
                        ResponseEstimate rsp = mQQ.getValue();
                        if (qq.getMaturityLevel().getId() == mlp.getId().getIdMaturityLevel()
                                && pri.getId() == mlp.getIdPrinciple()) {
                            avg = avg.add(rsp.getAvg());
                            size++;
                        }
                    }
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
            mapPrincipleEstimate.put(pri, oREstimate);
        }
        return mapPrincipleEstimate;
    }

}
