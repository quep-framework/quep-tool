/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
//@RequestScoped
public class ResultsQuestionnaireResponsesBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    @Inject
    private OrganizationBean organizationBean;

    private Organization oOrganization = null;
    private List<QuestionnaireResponse> lstQuestionnaireResponse = new ArrayList<QuestionnaireResponse>();
    //private List<QQQuestionRoleStakeholder> lstQuestionnaireResponse = new ArrayList<QQQuestionRoleStakeholder>();

    public ResultsQuestionnaireResponsesBean() {

    }

    @PostConstruct
    public void init() {
        oOrganization = organizationBean.getOrganization();        
        
        QuestioannaireDaoImplement qqdi = new QuestioannaireDaoImplement();
        lstQuestionnaireResponse = qqdi.getLstQuestionnaireResponse(oOrganization.getId());
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public OrganizationBean getOrganizationBean() {
        return organizationBean;
    }

    public void setOrganizationBean(OrganizationBean organizationBean) {
        this.organizationBean = organizationBean;
    }

    public Organization getoOrganization() {
        return oOrganization;
    }

    public void setoOrganization(Organization oOrganization) {
        this.oOrganization = oOrganization;
    }    

    public List<QuestionnaireResponse> getLstQuestionnaireResponse() {
        return lstQuestionnaireResponse;
    }

    public void setLstQuestionnaireResponse(List<QuestionnaireResponse> lstQuestionnaireResponse) {
        this.lstQuestionnaireResponse = lstQuestionnaireResponse;
    }
    

   

}
