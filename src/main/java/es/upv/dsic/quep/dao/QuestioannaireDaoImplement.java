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
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.ResponseOption;
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
            Query query = session.createQuery("from QuestionnaireQuepQuestion where id.idRole=" + idRole);
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
    public List<QuestionnaireQuepQuestion> getQuestionnairesQQRoleSthk(int idRole,int idStk){
        Session session = null;
        List<QuestionnaireQuepQuestion> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select qqq\n"
                    + "from QuestionnaireResponse  qr, QuestionnaireQuepQuestion qqq\n"
                    + "where qr.id.idRole='" + idRole + "'" + "and qr.id.idStakeholder='" + idStk + "'" +"\n"
                    + "and qr.id.idQuestionnaire =qqq.id.idQuestionnaire\n"
                    + "and qr.id.idPractice = qqq.id.idPractice\n"
                    + "and qr.id.idRole = qqq.id.idRole ");
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
                    + "where qr.id.idRole='" + idRole + "'" + "and qr.id.idStakeholder='" + idStk + "'" );
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
    public List<Principle> getPrinciples(int idRole,int idStk) {
        Session session = null;
        List<Principle> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            //Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle from QuestionnaireQuepQuestion qqq where qqq.id.idRole='" + idRole + "'");
            Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle\n"
                    + "from QuestionnaireResponse  qr, QuestionnaireQuepQuestion qqq\n"
                    + "where qr.id.idRole='" + idRole + "'" + "and qr.id.idStakeholder='" + idStk + "'" +"\n"
                    + "and qr.id.idQuestionnaire =qqq.id.idQuestionnaire\n"
                    + "and qr.id.idPractice = qqq.id.idPractice\n"
                    + "and qr.id.idRole = qqq.id.idRole ");
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
            Query query = session.createQuery("select qqro.responseOption from QuepQuestionResponseOption qqro  where qqro.id.idQuepQuestion='" + idqq + "'");
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
            Query query = session.createQuery("select qqro.responseOption from QuepQuestionResponseOption qqro  where qqro.id.idQuepQuestion='" + idqq + "'" + " and qqro.id.idResponseOption='" + idro + "'");
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

    public List<QuepQuestionResponseOption> getQuepQuestionResponseOption(int idqq) {
        Session session = null;
        List<QuepQuestionResponseOption> lstQQRO = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestionResponseOption qqro  where qqro.id.idQuepQuestion='" + idqq + "'");
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
    public Map<Integer, String> insertResponse(List<Response> lstResponse) {
        Session session = null;
        Map<Integer, String> mMessage = new HashMap<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (Iterator<Response> itR = lstResponse.iterator(); itR.hasNext();) {
                Response oResponse = itR.next();
                session.save(oResponse);
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
            return mMessage;
        }
    }

    @Override
    public Map<Integer, String> insertQuestionnaireResponse(List<QuestionnaireResponse> lstQResponse) {
        Session session = null;
        Map<Integer, String> mMessage = new HashMap<>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();

            for (QuestionnaireResponse oQResponse : lstQResponse) {
                switch (oQResponse.getStatus()) {
                    case 0:
                        session.save(oQResponse);
                        break;
                    case 2:
                        session.update(oQResponse);
                        break;
                    case 3:
                        session.delete(oQResponse);
                        break;
                    default:
                        break;
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
            return mMessage;
        }
    }


}
