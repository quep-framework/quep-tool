/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import java.util.List;
import javax.inject.Inject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class MenuDaoImplement implements MenuDao {

    @Inject private HibernateUtil hibernateUtil;
    
    @Override
    public List<Menu> getMenu() {
        Session session = null;
        List<Menu> list = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Menu");
            list = (List<Menu>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }

    /*    @Override
      public List<Menu> getSubMenu(int idRol) {
        Session session = null;
        List<Menu> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Menu where role.id="+idRol);
            list = (List<Menu>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return list;
    }*/
    @Override
    public List<Menu> getMenuRol(int idRol) {
        Session session = null;
        List<Menu> list = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from Menu where role.id=" + idRol);
            list = (List<Menu>) query.list();
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
