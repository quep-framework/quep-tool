/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

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

    private boolean loggedIn = false;
    private boolean admin = false;

    private Stakeholder stakeholder = new Stakeholder();
    private Role role = new Role();

    public String login() {
        try {
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
        } catch (Exception e) {
            return null;
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

    public boolean checkUser(String pUsername, String pPassword) {
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
        RoleStakeholder roleStk = roleStkImpl.getRoleStakeholder(pUsername, pPassword);
        stakeholder = roleStk.getStakeholder();

        if (stakeholder != null) {
            role = new Role();
            role = roleStkImpl.getRole(stakeholder);
            return true;
        } else {
            return false;
        }
        //TODO: Check bean
    }

    public boolean checkUserByOrganization(int iRole, int organization) {
        ///////*******************                
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
        RoleStakeholder roleStk = roleStkImpl.getRoleByOrganization(iRole, organization);
        stakeholder = roleStk.getStakeholder();

        if (stakeholder != null) {
            role = new Role();
            role = roleStkImpl.getRole(stakeholder);
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

}
