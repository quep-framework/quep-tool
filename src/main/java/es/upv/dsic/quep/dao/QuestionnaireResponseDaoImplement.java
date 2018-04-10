/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class QuestionnaireResponseDaoImplement implements QuestionnaireResponseDao {

    @Override
    public List<QuestionnaireResponse> getQuestionnaireResponses() {
        Session session = null;
        List<QuestionnaireResponse> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuestionnaireResponse where active=1");
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
    public QuestionnaireResponse getQuestionnaireResponse(int id) {
        Session session = null;
        QuestionnaireResponse oM = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuestionnaireResponse where active=1 ");
            oM = (QuestionnaireResponse) query.list().get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return oM;
    }    

    @Override
    public void insertQuestionnaireResponse(QuestionnaireResponse oQR) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(oQR);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
         
        }
        finally{
            if(session!=null)
                session.close();
        }
    }

    @Override
    public void updateQuestionnaireResponse(QuestionnaireResponse oQR) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(oQR);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
         
        }
        finally{
            if(session!=null)
                session.close();
        }    }

    @Override
    public void deleteQuestionnaireResponse(QuestionnaireResponse oQR) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(oQR);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
         
        }
        finally{
            if(session!=null)
                session.close();
        }    }

}
