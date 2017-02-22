/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuepQuestionDaoImplement;
import es.upv.dsic.quep.dao.ResponseDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
import es.upv.dsic.quep.model.Response;
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
public class RptResponseBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private List<Response> lstResponse = new ArrayList<Response>();
    //private List<QQQuestionRoleStakeholder> lstQQResponseOption = new ArrayList<QQQuestionRoleStakeholder>();

    public RptResponseBean() {

    }

    @PostConstruct
    public void init() {        
        ResponseDaoImplement qqdai = new ResponseDaoImplement();
        lstResponse = qqdai.getLstResponse();
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public List<Response> getLstResponse() {
        return lstResponse;
    }

    public void setLstResponse(List<Response> lstResponse) {
        this.lstResponse = lstResponse;
    }

    

}
