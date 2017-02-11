/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.ResponseOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class QuestioannaireDaoImplement implements QuestionnaireQQDao {

    @Override
    public List<QuestionnaireQuepQuestion> getQuestionnairesQQbyRole(int idRole) {
        Session session = null;
        List<QuestionnaireQuepQuestion> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuestionnaireQuepQuestion where active=1 and id.idRole=" + idRole);
            list = (List<QuestionnaireQuepQuestion>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }
    
    @Override
    public List<QuestionnaireQuepQuestion> getQuestionnairesQQRole(int idRole,int idOrg){
        Session session = null;
        List<QuestionnaireQuepQuestion> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select qqq\n"
                    + "from QuestionnaireQuepQuestion qqq, RoleStakeholder rs \n"
                    + "where qqq.id.idRole='" + idRole + "'" + " and qqq.active=1 \n"
                     + " and rs.id.idOrganization='" + idOrg + "'\n"
                    + " and rs.id.idRole=qqq.id.idRole ");
             /*Query query = session.createQuery("select qqq\n"
                    + "from QuestionnaireResponse  qr, QuestionnaireQuepQuestion qqq\n"
                    + "where qr.id.idRole='" + idRole + "'" + "and qr.id.idStakeholder='" + idStk + "'" +"\n"
                    + "and qr.id.idQuestionnaire =qqq.id.idQuestionnaire\n"
                    + "and qr.id.idPractice = qqq.id.idPractice\n"
                    + "and qr.id.idRole = qqq.id.idRole and qqq.active=1 and qr.active=1 ");*/
            list = (List<QuestionnaireQuepQuestion>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }
    
    @Override
    public List<QuestionnaireResponse> getQuestionnaireResponse(int idRole,int idStk){
        Session session = null;
        List<QuestionnaireResponse> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select qr\n"
                    + "from QuestionnaireResponse  qr \n"
                    + "where qr.active=1 and qr.id.idRole='" + idRole + "'" + "and qr.id.idStakeholder='" + idStk + "'" );
            list = (List<QuestionnaireResponse>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }
    
   

    @Override
    public List<Principle> getPrinciples(int idRole,int idOrg) {
        Session session = null;
        List<Principle> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            //Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle from QuestionnaireQuepQuestion qqq where qqq.active=1 and qqq.id.idRole='" + idRole + "'");
            Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle\n"
                    + "from  QuestionnaireQuepQuestion qqq, RoleStakeholder rs \n"
                    + "where qqq.id.idRole='" + idRole + "'" + " and qqq.active=1\n"
                    + " and rs.id.idOrganization='" + idOrg + "'\n"
                    + " and rs.id.idRole=qqq.id.idRole ");
            list = (List<Principle>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    @Override
    public List<ResponseOption> getResponseOptions(int idqq) {
        Session session = null;
        List<ResponseOption> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select qqro.responseOption from QuepQuestionResponseOption qqro  where qqro.active=1 and qqro.id.idQuepQuestion='" + idqq + "'");
            list = (List<ResponseOption>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    @Override
    public ResponseOption getResponseOption(int idqq, int idro) {
        Session session = null;
        ResponseOption oRO = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select qqro.responseOption from QuepQuestionResponseOption qqro  where qqro.active=1 and qqro.id.idQuepQuestion='" + idqq + "'" + " and qqro.id.idResponseOption='" + idro + "'");
            oRO = (ResponseOption) query.list().get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return oRO;
    }

     @Override
    public List<QuepQuestionResponseOption> getQuepQuestionResponseOption(int idqq) {
        Session session = null;
        List<QuepQuestionResponseOption> lstQQRO = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestionResponseOption qqro  where qqro.active=1 and qqro.id.idQuepQuestion='" + idqq + "'");//cmb
            lstQQRO = (List<QuepQuestionResponseOption>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return lstQQRO;
    }

    @Override
    public List<QuepQuestionTechnique> getQuepQuestionTechnique(int idqq) {
        Session session = null;
        List<QuepQuestionTechnique> lstQQT = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestionTechnique qqt  where qqt.active=1 and qqt.quepQuestion.id='" + idqq + "'");//cmb
            lstQQT = (List<QuepQuestionTechnique>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return lstQQT;
    }

    
   /* @Override
    public Map<Integer, String> insertResponse(Map<QuestionnaireResponse, List<Response>> mapLastResponse, Map<QuestionnaireResponse, List<Response>> mapPreviousResponse) {
        Map<Integer, String> mMessage = new HashMap<Integer, String>();
        if (mapPreviousResponse != null && mapPreviousResponse.size() > 0) {
            mMessage = deletePreviousResponse(mapPreviousResponse);
            if (mMessage.containsKey(1)) {
                mMessage = insertLastResponse(mapLastResponse);
            }
        } else {
            mMessage = insertLastResponse(mapLastResponse);
        }
        /*Session session = null;
        try {
            //delete all
            if (mapPreviousResponse != null && mapPreviousResponse.size()>0) {
                session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                for (Map.Entry<QuestionnaireResponse, List<Response>> mr_p : mapPreviousResponse.entrySet()) {
                    QuestionnaireResponse key = mr_p.getKey();
                    List<Response> values = mr_p.getValue();
                    if (values != null && values.size() > 0) {

                        //deleted responses
                        for (Response oResponse : values) {
                            session.delete(oResponse);
                        }
                        session.delete(key);
                    }
                }
                session.getTransaction().commit();
                session.close();
            }

            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (Map.Entry<QuestionnaireResponse, List<Response>> mr_l : mapLastResponse.entrySet()) {
                QuestionnaireResponse key = mr_l.getKey();
                List<Response> values = mr_l.getValue();
                if (values != null && values.size() > 0) {
                    session.save(key);

                    //new save responses
                    for (Response oResponse : values) {
                        session.save(oResponse);
                    }
                }
            }
            session.getTransaction().commit();
            mMessage.put(1, null);
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            mMessage.put(0, e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }*/
       /* return mMessage;
    }*/
    
     @Override
    public Map<Integer, String> insertResponse(Map<QuestionnaireResponse, List<Response>> mapLastResponse, Map<QuestionnaireResponse, List<Response>> mapPreviousResponse) {
        Map<Integer, String> mMessage = new HashMap<Integer, String>();
        Session session = null;
        try {
            boolean band;
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            //comprobar que al menos exista un dato
            if (mapPreviousResponse != null && mapPreviousResponse.size() > 0) {
                for (Map.Entry<QuestionnaireResponse, List<Response>> mr_l : mapLastResponse.entrySet()) {
                    QuestionnaireResponse oQR_last = mr_l.getKey();
                    List<Response> lstR_last = mr_l.getValue();
                    band = false;

                    for (Map.Entry<QuestionnaireResponse, List<Response>> mr_p : mapPreviousResponse.entrySet()) {
                        QuestionnaireResponse oQR_prev = mr_p.getKey();
                        List<Response> lstR_prev = mr_p.getValue();

                        if (oQR_prev.getId().getIdOrganization() == oQR_last.getId().getIdOrganization()
                                && oQR_prev.getId().getIdPractice() == oQR_last.getId().getIdPractice()
                                && oQR_prev.getId().getIdQuestionnaire() == oQR_last.getId().getIdQuestionnaire()
                                && oQR_prev.getId().getIdRole() == oQR_last.getId().getIdRole()
                                && oQR_prev.getId().getIdStakeholder() == oQR_last.getId().getIdStakeholder()) {
                            band = true;
                            //new save responses                            
                        }

                        if (lstR_prev != null && lstR_prev.size() > 0) {
                            for (Response oResponsePrev : lstR_prev) {
                                session.delete(oResponsePrev); //--> cambiar por update
                            }
                        }
                    }

                    if (band) {                    
                        session.update(oQR_last);
                    }

                    for (Response oResponseLast : lstR_last) {
                        session.save(oResponseLast);
                    }
                }
            }
            else{
                for (Map.Entry<QuestionnaireResponse, List<Response>> mr_l : mapLastResponse.entrySet()) {
                    QuestionnaireResponse oQR_last = mr_l.getKey();
                    List<Response> lstR_last = mr_l.getValue();
                    
                    session.save(oQR_last);
                    
                    for (Response oResponseLast : lstR_last) {
                        session.save(oResponseLast);
                    }
                }
            }

            session.getTransaction().commit();
            mMessage.put(1, null);
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            mMessage.put(0, e.getMessage());
       /* }catch (Exception e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
            mMessage.put(0, e.getMessage());*/
        }
        finally {
            if (session != null) {
                session.close();
            }
        }
        return mMessage;
    }
    
    @Override
    public List<Response> getListResponse(int idRole, int idStk, int idOrg) {
        Session session = null;
        List<Response> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select r\n"
                    + "from Response r \n"
                    + "where r.active=1 and r.id.idRole='" + idRole + "'"
                    + " and r.id.idStakeholder='" + idStk + "'"
                    + " and r.id.idOrganization='" + idOrg + "'");
            list = (List<Response>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }
    
   

}
