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
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class ResponseDaoImplement implements ResponseDao {

   
    @Override
    public List<Response> getLstResponse(){
        Session session = null;
        List<Response> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Response where active=1");
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
