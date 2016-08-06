/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

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

    public String logout(){
        loggedIn = false;
        
        FacesMessage msg = new FacesMessage("Logout success!","INFO MSG");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return navigationBean.redirectToLogin();
    }
    
    private boolean checkUser(String pUsername, String pPassword) {
        if (pUsername.equals("oalvear") && pPassword.equals("oscar")) {
            return true;
        }
        return false;
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
}
