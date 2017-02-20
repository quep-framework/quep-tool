/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.MaturityLevelPractice;
import es.upv.dsic.quep.model.Principle;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class PrincipleDaoImplement implements PrincipleDao {

    @Override
    public List<Principle> getPrinciple() {
        Session session = null;
        List<Principle> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Principle where active=1");
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
    public Principle getPrinciple(int id) {
        Session session = null;
        Principle oPri = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Principle where active=1 and id="+id);
            oPri = (Principle) query.list().get(0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return oPri;
    }
    
}
