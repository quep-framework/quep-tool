/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuepQuestionDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
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
public class RptQQResponseOptionBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private List<QuepQuestionResponseOption> lstQQResponseOption = new ArrayList<QuepQuestionResponseOption>();
    //private List<QQQuestionRoleStakeholder> lstQQResponseOption = new ArrayList<QQQuestionRoleStakeholder>();

    public RptQQResponseOptionBean() {

    }

    @PostConstruct
    public void init() {        
        QuepQuestionDaoImplement qqdai = new QuepQuestionDaoImplement();
        lstQQResponseOption = qqdai.getLstQQuestionResponseOption();
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
    public List<QuepQuestionResponseOption> getLstQQResponseOption() {
        return lstQQResponseOption;
    }

    public void setLstQQResponseOption(List<QuepQuestionResponseOption> lstQQResponseOption) {
        this.lstQQResponseOption = lstQQResponseOption;
    }
    
    

}
