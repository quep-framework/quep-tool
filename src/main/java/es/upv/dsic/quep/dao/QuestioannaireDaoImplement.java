/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import java.util.List;
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
            Query query = session.createQuery("from QuestionnaireQuepQuestion where id.idRole="+idRole);
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


}
