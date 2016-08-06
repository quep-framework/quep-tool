/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.persistence.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class MaturityLevelDaoImplement implements MaturityLevelDao {

    @Override
    public List<MaturityLevel> getMaturityLevels() {
        Session session = null;
        List<MaturityLevel> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from MaturityLevel");
            list = (List<MaturityLevel>) query.list();
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
    public void insertMaturityLevel(MaturityLevel ml) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(ml);
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
    public void updateMaturityLevel(MaturityLevel ml) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.update(ml);
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
    public void deleteMaturityLevel(MaturityLevel ml) {
        Session session = null;
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(ml);
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
