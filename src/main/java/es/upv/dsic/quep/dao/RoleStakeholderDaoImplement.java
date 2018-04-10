/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Stakeholder;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class RoleStakeholderDaoImplement implements RoleStakeholderDao {

   
    
    @Override
    public RoleStakeholder getRoleStakeholder(String user, String password) {
        Session session = null;
        List<RoleStakeholder> listR = null; 
        //List<RoleStakeholderId> listRS = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
             Query queryR = session.createQuery("from RoleStakeholder where active=1 and stakeholder.email='" + user + "' and stakeholder.password = '" + password + "'");
     
            listR = (List<RoleStakeholder>) queryR.list();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (listR != null) {
            RoleStakeholder r =  listR.get(0);
            return r;
        } else {
            return null;
        }
    }
    
     @Override
    public RoleStakeholder getRoleByOrganization(int role, int organization) {
        Session session = null;
        List<RoleStakeholder> listR = null; 
        //List<RoleStakeholderId> listRS = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
             Query queryR = session.createQuery("from RoleStakeholder where active=1 and  role.id = " + role + " and organization.id="+organization);
     
            listR = (List<RoleStakeholder>) queryR.list();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return  listR.get(0);             
    }


    @Override
    public List<Role> getLstRoles() {
        Session session = null;
        List<Role> listR = null; 
        //List<RoleStakeholderId> listRS = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
             Query queryR = session.createQuery("from Role where active=1 ");
     
            listR = (List<Role>) queryR.list();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return  listR;             
    }
    
    @Override
    public Role getRole(Stakeholder stk) {
        Session session = null;
        List<Role> listR = null; 
        //List<RoleStakeholderId> listRS = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query queryR = session.createQuery("select rs.role from RoleStakeholder rs where rs.active=1 and rs.id.idStakeholder='" + stk.getId() + "'");
            listR = (List<Role>) queryR.list();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (listR != null) {
            Role r =  listR.get(0);
            return r;
        } else {
            return null;
        }
    }

    @Override
    public Organization getOrganization(int idStakeholder, int idRole) {
        Session session = null;
        List<Organization> listO = null; 
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query queryRS = session.createQuery("select rs.organization from RoleStakeholder rs where rs.active=1 and rs.id.idStakeholder='" + idStakeholder + "' and rs.id.idRole='"+idRole + "'");
            listO = (List<Organization>) queryRS.list();            

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (listO != null) {            
            Organization o = listO.get(0);
            return o;
        } else {
            return null;
        }
    }

    @Override
    public List<Organization> getListOrganization(int idStakeholder, int idRole) {
        Session session = null;
        List<Organization> listO = null; 
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query queryRS = session.createQuery("select rs.organization from RoleStakeholder rs where rs.active=1 and rs.id.idStakeholder='" + idStakeholder + "' and rs.id.idRole='"+idRole + "'");
            listO = (List<Organization>) queryRS.list();            

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (listO != null) {                        
            return listO;
        } else {
            return null;
        }
    }
    

}
