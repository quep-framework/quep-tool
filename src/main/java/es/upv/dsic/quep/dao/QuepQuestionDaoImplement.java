/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Stakeholder;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResilience;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class QuepQuestionDaoImplement implements QuepQuestionDao {

    @Override
    public List<QuepQuestion> getLstQuepQuestion() {
        Session session = null;
        List<QuepQuestion> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestion where active=1");
            list = (List<QuepQuestion>) query.list();
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
    public List<QuepQuestionResponseOption> getLstQQuestionResponseOption() {
        Session session = null;
        List<QuepQuestionResponseOption> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestionResponseOption where active=1");
            list = (List<QuepQuestionResponseOption>) query.list();
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
    public List<QuepQuestionTechnique> getLstQQuestionTechniques() {
        Session session = null;
        List<QuepQuestionTechnique> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestionTechnique where active=1");
            list = (List<QuepQuestionTechnique>) query.list();
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
    public QuepQuestionResilience getQuepQuestionResilience(int idPrinciple, int idPractice, int idQuestion) {
        Session session = null;
        List<QuepQuestionResilience> list = new ArrayList<QuepQuestionResilience>();
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from QuepQuestionResilience where active=1 \n"
                    + "and idPractice='" + idPractice + "'" + "\n"
                    + "and quepQuestion.id='" + idQuestion + "'" + "\n"
                    + "and idPrinciple='" + idPrinciple + "'" + "\n");
            list = (List<QuepQuestionResilience>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list.get(0);
    }

}
