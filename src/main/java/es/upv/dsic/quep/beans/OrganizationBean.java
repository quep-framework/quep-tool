/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.MenuDao;
import es.upv.dsic.quep.dao.MenuDaoImplement;
import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.dsic.quep.model.Stakeholder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import javax.inject.Named;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped

public class OrganizationBean implements Serializable {

    private Organization organization = new Organization();
    private List<Organization> lstOrganization;
    private boolean bandOrganization = false;
    private String nameOrganization="";
    

    

    @Inject
    private AccessBean accessBean;

    public OrganizationBean() {
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
        RoleStakeholder rs= (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
        
        Role role = new Role();
        //role = (Role) AccessBean.getSessionObj("role");
        role =rs.getRole();

        Stakeholder stk = new Stakeholder();
        //stk = (Stakeholder) AccessBean.getSessionObj("stakeholder");
        stk=rs.getStakeholder();

        lstOrganization = new ArrayList<>(0);
        lstOrganization = roleStkImpl.getListOrganization(stk.getId(), role.getId());
        
        bandOrganization = lstOrganization.size() > 1;
        
        if (!bandOrganization){
            nameOrganization = lstOrganization.get(0).getName();
        }
    }

  
    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Organization> getLstOrganization() {
        return lstOrganization;
    }

    public void setLstOrganization(List<Organization> lstOrganization) {
        this.lstOrganization = lstOrganization;
    }

    public AccessBean getAccessBean() {
        return accessBean;
    }

    public void setAccessBean(AccessBean accessBean) {
        this.accessBean = accessBean;
    }

    public boolean isBandOrganization() {
        return bandOrganization;
    }

    public void setBandOrganization(boolean bandOrganization) {
        this.bandOrganization = bandOrganization;
    }

    public String getNameOrganization() {
        return nameOrganization;
    }

    public void setNameOrganization(String nameOrganization) {
        this.nameOrganization = nameOrganization;
    }

    
    
}
