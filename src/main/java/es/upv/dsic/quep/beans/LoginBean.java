/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.StakeholderDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.Stakeholder;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;
import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped

public class LoginBean implements Serializable {

    @Inject
    private CredentialBean credentialBean;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private AccessBean accessBean;
    
   /* @Inject
    private RoleStakeholder roleStakeholder;*/

    private boolean loggedIn = false;
    private boolean admin = false;

    private Stakeholder stakeholder = new Stakeholder();
    private Role role = new Role();
    

  
    //private boolean bandOrganization = false;

    public String login() {
        setLoggedIn(checkUser(credentialBean.getUsername(), credentialBean.getPassword()));
        FacesMessage msg = new FacesMessage("Login error!", "ERROR MSG");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("loggedIn", loggedIn);
        //RequestContext.getCurrentInstance().addCallbackParam("bandOrganization", bandOrganization);

        if (loggedIn) {
            return navigationBean.redirectToUser();
        } else {
            return navigationBean.toLogin();
        }
    }

    public String logout() {
        loggedIn = false;

        FacesMessage msg = new FacesMessage("Logout success!", "INFO MSG");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return navigationBean.redirectToLogin();
    }

    private boolean checkUser(String pUsername, String pPassword) {
        ///////*******************
        //REVISAR MAPEO Y CAMBIARLO lazy="false"
        //////
        //stakeholder = new Stakeholder();
        //StakeholderDaoImplement linkDao = new StakeholderDaoImplement();
        //stakeholder = linkDao.login(pUsername, pPassword);
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
        RoleStakeholder roleStk= roleStkImpl.getRoleStakeholder(pUsername, pPassword);
        //roleStakeholder = roleStkImpl.getRoleStakeholder(pUsername, pPassword);
        stakeholder = roleStk.getStakeholder();
        
               
        AccessBean.setSessionObj("stakeholder", stakeholder);
        if (stakeholder != null) {
            //RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
            role = new Role();
            role = roleStkImpl.getRole(stakeholder);
            //role = roleStk.getRole();
            AccessBean.setSessionObj("role", role);            
            AccessBean.setSessionObj("roleStakeholder", roleStk); 
            return true;
        } else {
            return false;
        }
        //TODO: Check bean
    }
    
         
    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public CredentialBean getCredentialBean() {
        return credentialBean;
    }

    public void setCredentialBean(CredentialBean credentialBean) {
        this.credentialBean = credentialBean;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public Stakeholder getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AccessBean getAccessBean() {
        return accessBean;
    }

    public void setAccessBean(AccessBean accessBean) {
        this.accessBean = accessBean;
    }


    /*
    public boolean isBandOrganization() {
        return bandOrganization;
    }

    public void setBandOrganization(boolean bandOrganization) {
        this.bandOrganization = bandOrganization;
    }*/
    
   /* public String getUserName() {
        if (stakeholder != null)
        return  "usr";//stakeholder.getEmail().substring(0,stakeholder.getEmail().indexOf("@"));
        else return "";
    }
*/
    

    
}
