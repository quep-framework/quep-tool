/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.Response;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class ResultsDaoImplement implements ResultsDao {

    @Inject private HibernateUtil hibernateUtil;
           
   @Override
    public List<QuepQuestionResponseOption> getQuepQuestionResponseOption(int idOrg) {
        Session session = null;
        List<QuepQuestionResponseOption> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            //Query query = session.createQuery("select distinct qqq.quepQuestion.practice.principle from QuestionnaireQuepQuestion qqq where qqq.active=1 and qqq.id.idRole='" + idRole + "'");
            Query query = session.createQuery("select distinct qqro\n"
                    + "from  QuestionnaireQuepQuestion qqq, QuepQuestionResponseOption qqro,\n"
                    + "RoleStakeholder rs \n"
                    + "where qqq.active=1 and qqro.active=1 and rs.active=1\n"
                    + "and rs.id.idOrganization='" + idOrg + "'" + "\n"
                    + "and qqq.id.idQuepQuestion=qqro.id.idQuepQuestion\n"
                    + "and qqq.questionnaire.role.id=rs.id.idRole");
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
    public List<Response> getListResponse(int idOrg) {
        Session session = null;
        List<Response> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("select r\n"
                    + "from Response r \n"
                    + "where r.active=1 \n"
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
