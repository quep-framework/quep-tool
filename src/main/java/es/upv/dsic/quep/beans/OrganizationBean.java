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
import javax.enterprise.inject.spi.BeanManager;
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
@Named("organizationBean")
@SessionScoped
public class OrganizationBean implements Serializable {

    private Organization organization;
    private List<Organization> lstOrganization;
    private boolean bandOrganization = false;
    private String nameOrganization = "";
    
    @Inject
    BeanManager manager;
    
    @Inject
    private LoginBean loginBean;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private QuestionnaireDynamicBean questionnaireDynamicBean;

    @Inject
    private ResultsChartViewBean resultsChartViewBean;

    //@Inject
    //private RoleStakeholder roleStakeholder;
    public OrganizationBean() {
      
    }

    @PostConstruct
    private void init() {
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();

        //lstOrganization = new ArrayList<>(0);
        lstOrganization = roleStkImpl.getListOrganization(loginBean.getStakeholder().getId(), loginBean.getRole().getId());
        //lstOrganization = roleStkImpl.getListOrganization(roleStakeholder.getStakeholder().getId(), roleStakeholder.getRole().getId());

        bandOrganization = lstOrganization.size() > 1;

        if (!bandOrganization) {
            nameOrganization = lstOrganization.get(0).getName();

        }
        //if (accessBean.getSessionObj("organization") == null) {
        if (this.organization == null) {
            this.organization = lstOrganization.get(0);
        }
        //accessBean.setSessionObj("organizationBean", this);
    }

    //***
    public void changeListener(ValueChangeEvent event) throws IOException {
        Object newValue = event.getNewValue();
        setOrganization((Organization) newValue);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());

        if (loginBean.checkUserByOrganization(loginBean.getRole().getId(), organization.getId())) {
            String str = navigationBean.getCurrentPage();
            if (str.contains("/QuEP-Tool/admin/frmMaturityLevelsResults.xhtml")) {
                resultsChartViewBean.init();
                //resultsChartViewBean.
                /*AjaxBehavior ajaxBehavior = (AjaxBehavior) FacesContext.getCurrentInstance().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
                ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxListener());
                ajaxBehavior.setTransient(true);*/

                //txtComments.addClientBehavior("change", ajaxBehavior);
            } else if (str.contains("/QuEP-Tool/all/frmQuestionnaireDynamic.xhtml")) {
                //questionnaireDynamicBean = new QuestionnaireDynamicBean();
                questionnaireDynamicBean.init();
            }
        }
    }

    /* public class CustomAjaxListener implements AjaxBehaviorListener {

        @Override
        public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
            //SelectOneRadio rb = (SelectOneRadio) event.getComponent();
            //Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
            questionnaireDynamicBean = new QuestionnaireDynamicBean();
        }
    }*/
    public Organization getOrganization(int id) {
        Organization oOrganization = null;
        for (Organization org : lstOrganization) {
            if (id == org.getId()) {
                oOrganization = org;
                break;
            }
        }
        return oOrganization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Organization> getLstOrganization() {
        return lstOrganization;
    }

    public void setLstOrganization(List<Organization> lstOrganization) {
        this.lstOrganization = lstOrganization;
    }

    public boolean isBandOrganization() {
        return bandOrganization;
    }

    public boolean getBandOrganization() {
        return bandOrganization;
    }

    public void setBandOrganization(boolean bandOrganization) {
        this.bandOrganization = bandOrganization;
    }

    public String getNameOrganization() {
        return nameOrganization;
    }

    public void setNameOrganization(String nameOrganization) {
        this.nameOrganization = nameOrganization;
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

    public ResultsChartViewBean getResultsChartViewBean() {
        return resultsChartViewBean;
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

    public void setResultsChartViewBean(ResultsChartViewBean resultsChartViewBean) {
        this.resultsChartViewBean = resultsChartViewBean;
    }

}
