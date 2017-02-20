/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.Practice;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class PracticeDaoImplement implements PracticeDao {
    @Override
    public List<Practice> getPractice() {
        Session session = null;
        List<Practice> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Practice where active=1");
            list = (List<Practice>) query.list();
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
    public Practice getPracticeByID(int id) {
        Session session = null;
        Practice pra = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Practice where active=1 and id="+id);
            pra = (Practice) query.list().get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return pra;
    }   
}
