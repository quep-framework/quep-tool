/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author agna8685
 */
@Named
//(value = "userBean")
//@ManagedBean
@RequestScoped
//@ViewScoped
//@ManagedBean
//@SessionScoped
//@Session
//@Named("userBean")

public class CredentialBean implements Serializable {

    private static final long serialVersionUID = -2152389656664659476L;

    private String username;
    private String password;

    public CredentialBean() {
    }

    /* @PostConstruct
    public void init() {
        stakeholder = new Stakeholder();
    }*/
    public void closeSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }


    /* public String redireccionarLogin(Boolean band){
        if (band)
        return "frmMainPage";
        else return "frmInitPage";
    }*/
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

}
