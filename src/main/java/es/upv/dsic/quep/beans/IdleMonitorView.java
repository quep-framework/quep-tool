/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author agna8685
 */
@Named
//@RequestScoped
@SessionScoped

public class IdleMonitorView implements Serializable {
    
    @Inject
    private NavigationBean navigationBean;
    
    @Inject
    private CredentialBean credentialBean;
     
    
    public void onIdle() throws IOException{
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, 
                                        "Inactivo.", "¿Sigues ahí?"));      
         ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
         String str = navigationBean.getRequestContextPath();        
        credentialBean.closeSession();                
        ec.redirect(str);   
    }
 
    public void onActive() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                                        "Bienvenido", "¡Ha sido un largo coffee break!"));
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public CredentialBean getCredentialBean() {
        return credentialBean;
    }

    public void setCredentialBean(CredentialBean credentialBean) {
        this.credentialBean = credentialBean;
    }

   
    
    
    
}
