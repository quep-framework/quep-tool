/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.OrganizationDaoImplement;
import es.upv.dsic.quep.dao.StakeholderDao;
import es.upv.dsic.quep.dao.StakeholderDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Stakeholder;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

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

    private String nameStakeholder = "";
    private Stakeholder stakeholder = new Stakeholder();
    
    private String organization = "";

    public String login() {
        setLoggedIn(checkUser(credentialBean.getUsername(), credentialBean.getPassword()));
        FacesMessage msg = new FacesMessage("Login error!", "ERROR MSG");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().addCallbackParam("loggedIn", loggedIn);

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
        stakeholder = new Stakeholder();
        StakeholderDao linkDao = new StakeholderDaoImplement();
        stakeholder = linkDao.login(pUsername, pPassword);
        if (stakeholder != null) {//pUsername.equals(this.stakeholder.get) && pPassword.equals("oscar")) {
            setNameStakeholder(this.stakeholder.getName() + " " + this.stakeholder.getLastName());
            OrganizationDaoImplement orgDao = new OrganizationDaoImplement();
            setOrganization(orgDao.getOrganization(stakeholder).getName());
            return true;
        } else {
            return false;
        }
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

    public String getNameStakeholder() {
        return nameStakeholder;
    }

    public void setNameStakeholder(String nameStakeholder) {
        this.nameStakeholder = nameStakeholder;
    }

    public Stakeholder getStakeholder() {
        return stakeholder;
    }

    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    

}
