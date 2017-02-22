/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuepQuestionDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

//import org.primefaces.component.menuitem.MenuItem;
/**
 *
 * @author agna8685
 */
@Named
@SessionScoped
//@RequestScoped
public class RptQQTechniquesBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private List<QuepQuestionTechnique> lstQQTechnique = new ArrayList<QuepQuestionTechnique>();
    //private List<QQQuestionRoleStakeholder> lstQQResponseOption = new ArrayList<QQQuestionRoleStakeholder>();

    public RptQQTechniquesBean() {

    }

    @PostConstruct
    public void init() {        
        QuepQuestionDaoImplement qqdai = new QuepQuestionDaoImplement();
        lstQQTechnique = qqdai.getLstQQuestionTechniques();
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<QuepQuestionTechnique> getLstQQTechnique() {
        return lstQQTechnique;
    }

    public void setLstQQTechnique(List<QuepQuestionTechnique> lstQQTechnique) {
        this.lstQQTechnique = lstQQTechnique;
    }   

}
