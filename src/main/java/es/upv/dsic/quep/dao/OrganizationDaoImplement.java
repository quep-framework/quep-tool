/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Stakeholder;
import es.upv.dsic.quep.hibernate.HibernateUtil;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author agna8685
 */
public class OrganizationDaoImplement implements OrganizationDao {

   
    @Override
    public Organization getOrganization(Stakeholder stk) {
        Session session = null;
        List<RoleStakeholder> listRS = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            //int idRole = 6;            
            Query query = session.createQuery("from RoleStakeholder where id.idRole=6'" + "" + "' and id.idStakeholder = '" + stk.getId() + "'");
            listRS = (List<RoleStakeholder>) query.list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
        if (listRS!=null) {
            return listRS.get(0).getOrganization();
        } else {
            return null;
        }
    }
    

}
