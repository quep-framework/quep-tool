/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.es.dsic.quep.utils;

import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
import es.upv.dsic.quep.dao.PracticeDaoImplement;
import es.upv.dsic.quep.dao.QuepQuestionDaoImplement;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.dao.ResultsDaoImplement;
import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.MaturityLevelPractice;
import es.upv.dsic.quep.model.Practice;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResilience;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
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

        //Resilience
        QuepQuestionDaoImplement qqdi = new QuepQuestionDaoImplement();
        QuepQuestionResilience qResilience = new QuepQuestionResilience();

        //2.Calculate Responses
        List<Response> lstResult = new ArrayList<Response>();
        lstResult = rdi.getListResponse(organizationId);

        Map<QuepQuestion, ResponseEstimate> mapSumResponseOp = new HashMap<QuepQuestion, ResponseEstimate>();
        for (QuepQuestionResponseOption oQQRO : lstQQuestionResponseOption) {
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal sumReqQQuestion = BigDecimal.ZERO;        //cambiar mapeo de int a double                  
            BigDecimal sumQQuestion = BigDecimal.ZERO;
            BigDecimal avg = BigDecimal.ZERO;

            BigDecimal sumReqQQuestionR = BigDecimal.ZERO;
            BigDecimal avgR = BigDecimal.ZERO;

            //Get weight SUMs (included requiered SUM
            BigDecimal sumPreviousReqQQ = BigDecimal.ZERO;
            for (QuepQuestionResponseOption oQQRO_aux : lstQQuestionResponseOption) {
                if (oQQRO_aux.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumQQuestion = sumQQuestion.add(oQQRO_aux.getResponseOption().getWeight());
                    if (oQQRO_aux.getResponseOption().getIsRequiered() == 1) {
                        if (oQQRO_aux.getResponseOption().getQuestionType().getName().toLowerCase().trim().contains("check")) {
                            sumReqQQuestion = sumReqQQuestion.add(oQQRO_aux.getResponseOption().getWeight());
                        } else if (oQQRO_aux.getResponseOption().getQuestionType().getName().toLowerCase().trim().contains("radio")) {
                            sumReqQQuestion = oQQRO_aux.getResponseOption().getWeight();
                            if (sumPreviousReqQQ.compareTo(sumReqQQuestion) == -1) {
                                sumPreviousReqQQ = sumReqQQuestion;
                            } else {
                                sumReqQQuestion = sumPreviousReqQQ;
                                break;
                            }
                        }
                    }

                    try {
                        qResilience = qqdi.getQuepQuestionResilience(oQQRO.getIdPrinciple(), oQQRO.getIdPractice(), oQQRO_aux.getId().getIdQuepQuestion());
                        if (qResilience != null) {
                            sumReqQQuestionR = sumReqQQuestion;
                        }
                    } catch (Exception e) {
                        sumReqQQuestionR = BigDecimal.ZERO;
                    }

                }
            }

            //Get Responses SUMs of an Organization 
            BigDecimal sumRsp = BigDecimal.ZERO;
            int sizeStkRsp = 0;

            BigDecimal sumRspR = BigDecimal.ZERO;
            int sizeStkRspR = 0;

            for (Response oResponse : lstResult) {
                if (oResponse.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()
                        && oResponse.getResponseOption().getIsRequiered() == 1) { //++
                    sumRsp = sumRsp.add(oResponse.getResponseOption().getWeight());
                    sizeStkRsp++;

                    try {
                        qResilience = qqdi.getQuepQuestionResilience(oQQRO.getIdPrinciple(), oQQRO.getIdPractice(), oResponse.getId().getIdQuepQuestion());
                        if (qResilience != null) {
                            sumRspR = sumRsp;
                            sizeStkRspR++;
                        }
                    } catch (Exception e) {
                        sumRspR = BigDecimal.ZERO;
                    }

                }
            }
            if (sizeStkRsp == 0) {
                sizeStkRsp = 1;
            }

            if (sizeStkRspR == 0) {
                sizeStkRspR = 1;
            }
            sumRsp = sumRsp.divide(BigDecimal.valueOf(sizeStkRsp), 4, RoundingMode.HALF_EVEN);
            sumRspR = sumRspR.divide(BigDecimal.valueOf(sizeStkRspR), 4, RoundingMode.HALF_EVEN);

            //average Responses SUMs / Requiered SUM of Questions Configurated in QuEP                              
            try {
                avg = sumRsp.divide(sumReqQQuestion, 4, RoundingMode.HALF_EVEN);
            } catch (Exception e) {
                avg = sumRsp.divide(BigDecimal.valueOf(1.0));
            }

            //average Resilience                             
            try {
                avgR = sumRspR.divide(sumReqQQuestionR, 4, RoundingMode.HALF_EVEN);
            } catch (Exception e) {
                avgR = sumRspR.divide(BigDecimal.valueOf(1.0));
            }

            //TODO: Qv= x*w    
            /*    if (oQQRO.getQuepQuestion().getWeight() != null) {
                avg = oQQRO.getQuepQuestion().getWeight().multiply(avg);                
            }*/
            //setting map Reponses SUMs of Quep Questions
            oREstimate.setSumRequiered(sumReqQQuestion);
            oREstimate.setSum(sumQQuestion);
            oREstimate.setAvg(avg);
            oREstimate.setAvgResilience(avgR);
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
                //double sumWeight = 0.0;
                ResponseEstimate oREstimate = new ResponseEstimate();
                
                //BigDecimal avg = BigDecimal.ZERO;
                BigDecimal x = BigDecimal.ZERO;    //avg            
                BigDecimal sumWeight = BigDecimal.ZERO;//BigDecimal.valueOf(0.0)
                BigDecimal Qv = BigDecimal.ZERO;
                
                for (Map.Entry<QuepQuestion, ResponseEstimate> oSumQ : mapSumQuepQuestions.entrySet()) {
                    QuepQuestion qq = oSumQ.getKey();
                    ResponseEstimate rsp = oSumQ.getValue();
                    if (qq.getPractice().getId() == oPr.getId()) {
                        //avg = avg.add(rsp.getAvg());
                        Qv=Qv.add(rsp.getAvg().multiply(qq.getWeight()));
                        sumWeight = sumWeight.add(qq.getWeight());                                                
                        size++;
                        //sumWeight += qq.getWeight().doubleValue();
                    }
                }
                                
                //sum(Qv) / sum(w)
                try {
                    Qv = Qv.divide(sumWeight, 4, RoundingMode.HALF_EVEN);
                } catch (Exception e) {
                    Qv = Qv.divide(BigDecimal.valueOf(1.0), 4, RoundingMode.HALF_EVEN);
                }
                
                oREstimate.setAvg(Qv);
                /*if (sumWeight == 0.0) {
                    sumWeight = 1;
                }//////////***cambiar en todos (no promedio)
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(sumWeight), 4, RoundingMode.HALF_EVEN));*/
                mapPracticeEstimate.put(oPr, oREstimate);
            }
        }
        return mapPracticeEstimate;
    }

    public Map<Role, ResponseEstimate> calculateRolesBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, int idOrganization) {
        Map<Role, ResponseEstimate> mapRoleQQQuestionEstimate = new HashMap<Role, ResponseEstimate>();
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
                    if (qqq.getQuestionnaire().getRole().getId() == oRole.getId()) {
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
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 4, RoundingMode.HALF_EVEN));
                mapRoleQQQuestionEstimate.put(oRole, oREstimate);
            }
        }
        return mapRoleQQQuestionEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrincipleBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<Principle> lstPrinciple) {
        Map<Principle, ResponseEstimate> mapPrincipleEstimate = new HashMap<Principle, ResponseEstimate>();

        for (Principle p : lstPrinciple) {            
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal x = BigDecimal.ZERO;
            BigDecimal sumWeight = BigDecimal.ZERO;//BigDecimal.valueOf(0.0)
            BigDecimal Qv = BigDecimal.ZERO;

            for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                QuepQuestion qq = mQQ.getKey();
                ResponseEstimate rsp = mQQ.getValue();
                if (qq.getPractice().getPrinciple().getId() == p.getId()) {
                    //Qv=x*w                          
                    Qv = Qv.add(rsp.getAvg().multiply(qq.getWeight()));
                    sumWeight = sumWeight.add(qq.getWeight());
                }
            }
            //Qv=x*w::Sum x / Sum weight
            //sum(Qv) / sum(w)
            try {
                Qv = Qv.divide(sumWeight, 4, RoundingMode.HALF_EVEN);
            } catch (Exception e) {
                Qv = Qv.divide(BigDecimal.valueOf(1.0), 4, RoundingMode.HALF_EVEN);
            }

            //oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 4, RoundingMode.HALF_EVEN));
            oREstimate.setAvg(Qv);
            mapPrincipleEstimate.put(p, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrincipleBySumQuestionsResilience(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<Principle> lstPrinciple) {
        Map<Principle, ResponseEstimate> mapPrincipleEstimate = new HashMap<Principle, ResponseEstimate>();

        for (Principle p : lstPrinciple) {
           // int size = 0;
            int sizeR = 0;
            ResponseEstimate oREstimate = new ResponseEstimate();
            //BigDecimal avg = BigDecimal.ZERO;
            //BigDecimal avgR = BigDecimal.ZERO;
            BigDecimal QvR = BigDecimal.ZERO;
            BigDecimal sumWeightR = BigDecimal.ZERO;//BigDecimal.valueOf(0.0)
                      
            BigDecimal sumWeight = BigDecimal.ZERO;//BigDecimal.valueOf(0.0)
            BigDecimal Qv = BigDecimal.ZERO;
                
            for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                QuepQuestion qq = mQQ.getKey();
                ResponseEstimate rsp = mQQ.getValue();
                if (qq.getIdPrinciple() == p.getId()) {
                    //avg = avg.add(rsp.getAvg());
                    
                    //Qv=x*w                          
                    Qv = Qv.add(rsp.getAvg().multiply(qq.getWeight()));
                    sumWeight = sumWeight.add(qq.getWeight());

                    //size++;
                    if (rsp.getAvgResilience().compareTo(BigDecimal.ZERO) != 0) {                        
                        //avgR = avgR.add();
                        QvR=QvR.add(rsp.getAvgResilience().multiply(qq.getWeight()));
                        sumWeightR = sumWeightR.add(qq.getWeight());
                        sizeR++;
                    }
                }
            }
          /*  if (size == 0) {
                size = 1;
            }*/

            if (sizeR == 0) {
                sizeR = 1;
            }
            
            //sum(Qv) / sum(w)
            try {
                Qv = Qv.divide(sumWeight, 4, RoundingMode.HALF_EVEN);
            } catch (Exception e) {
                Qv = Qv.divide(BigDecimal.valueOf(1.0), 4, RoundingMode.HALF_EVEN);
            }
            
            //Resilience
            try {
                QvR = QvR.divide(sumWeightR, 4, RoundingMode.HALF_EVEN);
            } catch (Exception e) {
                QvR = QvR.divide(BigDecimal.valueOf(1.0), 4, RoundingMode.HALF_EVEN);
            }

            oREstimate.setAvg(Qv);
            //oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 4, RoundingMode.HALF_EVEN));
            oREstimate.setAvgResilience(QvR);
            mapPrincipleEstimate.put(p, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<MaturityLevel, ResponseEstimate> calculateMaturityLevelBySumQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<MaturityLevel> lstMaturityLevel) {

        Map<MaturityLevel, ResponseEstimate> mapMaturityLevelEstimate = new HashMap<MaturityLevel, ResponseEstimate>();
        ResponseEstimate oREstimate = new ResponseEstimate();
        BigDecimal sumLastLevel = BigDecimal.ZERO;        
        int sizeLevels = 0;
        MaturityLevel oLastMaturityLevel = new MaturityLevel();

        for (MaturityLevel ml : lstMaturityLevel) {
            if (ml.getId() != 10) { //add un campo mas que indique que es el último nivel en la BD
                oREstimate = new ResponseEstimate();
                BigDecimal x = BigDecimal.ZERO;    //avg            
                BigDecimal sumWeight = BigDecimal.ZERO;//BigDecimal.valueOf(0.0)
                BigDecimal Qv = BigDecimal.ZERO;

               int size = 0;
                for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                    QuepQuestion qq = mQQ.getKey();
                    ResponseEstimate rsp = mQQ.getValue();
                    if (qq.getMaturityLevel().getId() == ml.getId()) {
                        //Qv=x*w                          
                        Qv=Qv.add(rsp.getAvg().multiply(qq.getWeight()));
                        sumWeight = sumWeight.add(qq.getWeight());                        
                        size++;                        
                    }
                }
                
                //sum(Qv) / sum(w)
                try {
                    Qv = Qv.divide(sumWeight, 4, RoundingMode.HALF_EVEN);
                } catch (Exception e) {
                    Qv = Qv.divide(BigDecimal.valueOf(1.0), 4, RoundingMode.HALF_EVEN);
                }
                
                oREstimate.setAvg(Qv);
                mapMaturityLevelEstimate.put(ml, oREstimate);

                sumLastLevel = sumLastLevel.add(Qv);
                sizeLevels++;
            } else {
                oLastMaturityLevel = ml;
            }
        }
        

        //setting last level
        oREstimate = new ResponseEstimate();        
        oREstimate.setAvg(sumLastLevel.divide(BigDecimal.valueOf(sizeLevels), 4, RoundingMode.HALF_EVEN));
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
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 4, RoundingMode.HALF_EVEN));
            mapPrincipleEstimate.put(p, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrinciplesByIdMaturityLevel(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions, List<Principle> lstPrinciple, String sMaturityLevelId) {
        Map<Principle, ResponseEstimate> mapPrincipleEstimate
                = new HashMap<Principle, ResponseEstimate>();

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();        
        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();                

        for (Principle pri : lstPrinciple) {
            ResponseEstimate oREstimate = new ResponseEstimate();
            
           // BigDecimal x = BigDecimal.ZERO; //avg
            BigDecimal sumWeight = BigDecimal.ZERO;//BigDecimal.valueOf(0.0)
            BigDecimal Qv = BigDecimal.ZERO;
            
            for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                if (mlp.getId().getIdMaturityLevel() == Integer.parseInt(sMaturityLevelId)) {
                    for (Map.Entry<QuepQuestion, ResponseEstimate> mQQ : mapSumQuepQuestions.entrySet()) {
                        QuepQuestion qq = mQQ.getKey();
                        ResponseEstimate rsp = mQQ.getValue();
                        if (qq.getMaturityLevel().getId() == mlp.getId().getIdMaturityLevel()
                                && pri.getId() == mlp.getIdPrinciple()
                                && mlp.getIdPrinciple() == qq.getIdPrinciple()) {//++
                            //Qv=x*w                          
                            Qv = Qv.add(rsp.getAvg().multiply(qq.getWeight()));
                            sumWeight = sumWeight.add(qq.getWeight());
                        }
                    }
                
                }
            }
            
                
            //sum(Qv) / sum(w)
            try {
                Qv = Qv.divide(sumWeight, 4, RoundingMode.HALF_EVEN);
            } catch (Exception e) {
                Qv = Qv.divide(BigDecimal.valueOf(1.0), 4, RoundingMode.HALF_EVEN);
            }
            
            //oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 4, RoundingMode.HALF_EVEN));
            oREstimate.setAvg(Qv);
            mapPrincipleEstimate.put(pri, oREstimate);
        }
        return mapPrincipleEstimate;
    }
    
    
    public int calculteLegendQR(int idOrg,int idStatus) {        
        ResultsDaoImplement rdi = new ResultsDaoImplement();        
        try {
            return rdi.getLegendQrStatus(idOrg,idStatus);
        } catch (Exception e) {
           return 0;
        }
    }
}
