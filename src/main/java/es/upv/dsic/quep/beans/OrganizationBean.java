/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.dsic.quep.model.Stakeholder;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import javax.inject.Named;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped

public class OrganizationBean implements Serializable {

    private Organization organization;
    private List<Organization> lstOrganization;
    private boolean bandOrganization = false;
    private String nameOrganization="";
    
    
    
    @Inject
    private AccessBean accessBean;

    @Inject
    private QuestionnaireDynamicBean questionnaireDynamicBean;
    
    //@Inject
    //private RoleStakeholder roleStakeholder;

    public OrganizationBean() {
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
        RoleStakeholder rs= (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
        
        Role role = new Role();
        role = (Role) AccessBean.getSessionObj("role");
        role =rs.getRole();

        Stakeholder stk = new Stakeholder();
        stk = (Stakeholder) AccessBean.getSessionObj("stakeholder");
        //stk=rs.getStakeholder();

        //lstOrganization = new ArrayList<>(0);
        lstOrganization = roleStkImpl.getListOrganization(rs.getStakeholder().getId(), rs.getRole().getId());
        //lstOrganization = roleStkImpl.getListOrganization(roleStakeholder.getStakeholder().getId(), roleStakeholder.getRole().getId());
        
        bandOrganization = lstOrganization.size() > 1;
        
        if (!bandOrganization){
            nameOrganization = lstOrganization.get(0).getName();
            
        }
        if(AccessBean.getSessionObj("organization")==null) {
            AccessBean.setSessionObj("organization", lstOrganization.get(0)); 
            this.organization = lstOrganization.get(0);
        }
        AccessBean.setSessionObj("organizationBean", this); 
        // AccessBean.setSessionObj("organization", organization);                
    }

  //***
    public String changeListener(ValueChangeEvent event) throws IOException {
        //Organization oldValue = (Organization) event.getOldValue();
        Object newValue =  event.getNewValue();   
        //AccessBean.setSessionObj("organization", newValue); 
        //FacesContext context = FacesContext.getCurrentInstance();
        Object oldValue = AccessBean.getSessionObj("organization");
        //context.getExternalContext().getSessionMap().put("organization", newValue);
        
        AccessBean.setSessionObj("organization", newValue);
        setOrganization((Organization)newValue);
        questionnaireDynamicBean = new QuestionnaireDynamicBean();
        return NavigationBean.toUser();
        //AccessBean.setSessionObj("organization", newValue); 
        
        //ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
    
    
     public Organization getOrganization(int id) {
         Organization oOrganization=null;
         for (Organization org : lstOrganization) {
             if (id==org.getId()){
                 oOrganization = org;
                 break;
             }
         }
      return oOrganization;
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
