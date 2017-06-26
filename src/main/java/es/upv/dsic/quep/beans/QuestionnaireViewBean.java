/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.Organization;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author agna8685
 */
@Named("questionaireViewBean")
@SessionScoped
public class QuestionnaireViewBean implements Serializable {
    
            
    @Inject
    private LoginBean loginBean;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private QuestionnaireDynamicBean questionnaireDynamicBean;

    @Inject
    private MaturityLevelResultsChartViewBean maturityLevelResultsChartViewBean;
    
    @Inject
    private PrincipleResultsChartViewBean PrincipleResultsChartViewBean;

    public QuestionnaireViewBean() {
      
    }

    @PostConstruct
    private void init() {
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
       
    }

  

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public QuestionnaireDynamicBean getQuestionnaireDynamicBean() {
        return questionnaireDynamicBean;
    }


    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public void setQuestionnaireDynamicBean(QuestionnaireDynamicBean questionnaireDynamicBean) {
        this.questionnaireDynamicBean = questionnaireDynamicBean;
    } 

    public MaturityLevelResultsChartViewBean getMaturityLevelResultsChartViewBean() {
        return maturityLevelResultsChartViewBean;
    }

    public void setMaturityLevelResultsChartViewBean(MaturityLevelResultsChartViewBean maturityLevelResultsChartViewBean) {
        this.maturityLevelResultsChartViewBean = maturityLevelResultsChartViewBean;
    }

    public PrincipleResultsChartViewBean getPrincipleResultsChartViewBean() {
        return PrincipleResultsChartViewBean;
    }

    public void setPrincipleResultsChartViewBean(PrincipleResultsChartViewBean PrincipleResultsChartViewBean) {
        this.PrincipleResultsChartViewBean = PrincipleResultsChartViewBean;
    }


    
}
