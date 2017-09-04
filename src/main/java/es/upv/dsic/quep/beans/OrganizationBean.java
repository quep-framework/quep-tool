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
import java.util.logging.Level;
import java.util.logging.Logger;
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
@Named("organizationBean")
@SessionScoped
public class OrganizationBean implements Serializable {

    private Organization organization;
    private List<Organization> lstOrganization;
    private boolean bandOrganization = false;
    private String nameOrganization = "";

    @Inject
    private LoginBean loginBean;

    @Inject
    private NavigationBean navigationBean;

    @Inject
    private CredentialBean credentialBean;

    @Inject
    private QuestionnaireDynamicBean questionnaireDynamicBean;

    @Inject
    private MaturityLevelResultsChartViewBean maturityLevelResultsChartViewBean;

    @Inject
    private PrincipleResultsChartViewBean PrincipleResultsChartViewBean;

    @Inject
    private PrincipleResilienceChartViewBean PrincipleResilienceChartViewBean;

    public OrganizationBean() {

    }

    @PostConstruct
    private void init() {
        try {
            
            RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
            lstOrganization = roleStkImpl.getListOrganization(loginBean.getStakeholder().getId(), loginBean.getRole().getId());
            bandOrganization = lstOrganization.size() > 1;

            if (!bandOrganization) {
                nameOrganization = lstOrganization.get(0).getName();

            }
            if (this.organization == null) {
                this.organization = lstOrganization.get(0);
            }
        
        } catch (Exception e) {
            validateOrganization();
        }

    }

  /*  public void validateSession() {

    }*/

    //***
     public void changeListener(ValueChangeEvent event) throws IOException {
        Object newValue = event.getNewValue();
        setOrganization((Organization) newValue);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());

        if (loginBean.checkUserByOrganization(loginBean.getRole().getId(), organization.getId())) {
            String str = navigationBean.getCurrentPage();
           /* if (str.contains("/QuEP-Tool/reports/frmMaturityLevelsResults.xhtml") || 
                    str.contains("/QuEP-Tool/reports/frmPrinciplesResults.xhtml") ||
                  str.contains("/QuEP-Tool/reports/frmPrinciplesResilienceResults.xhtml")
                    || str.contains("/QuEP-Tool/all/frmQuestionnaireDynamic.xhtml")) {*/
           if (str.contains("/reports/frmMaturityLevelsResults.xhtml") || 
                    str.contains("/reports/frmPrinciplesResults.xhtml") ||
                  str.contains("/reports/frmPrinciplesResilienceResults.xhtml")
                    || str.contains("/all/frmQuestionnaireDynamic.xhtml")) {
                if (questionnaireDynamicBean.isbSessionQuestionnaire()) {
                    questionnaireDynamicBean.init();
                }
                if (PrincipleResultsChartViewBean.isbSessionPrincipleResult()) {
                    PrincipleResultsChartViewBean.init();
                }
                if (maturityLevelResultsChartViewBean.isbSessionMLResults()) {
                    maturityLevelResultsChartViewBean.init();
                }
                if (PrincipleResilienceChartViewBean.isbSessionPrincipleResilienceResult()) {
                    PrincipleResilienceChartViewBean.init();
                }
            }
        }
    }
    /*public void changeListener(ValueChangeEvent event) throws IOException {
        Object newValue = event.getNewValue();
        setOrganization((Organization) newValue);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());

        if (loginBean.checkUserByOrganization(loginBean.getRole().getId(), organization.getId())) {
            String str = navigationBean.getCurrentPage();
            if (str.contains("/QuEP-Tool/reports/frmMaturityLevelsResults.xhtml")) {
                maturityLevelResultsChartViewBean.init();                
                ///
                PrincipleResultsChartViewBean.init();
                PrincipleResilienceChartViewBean.init();                
            } else if (str.contains("/QuEP-Tool/reports/frmPrinciplesResults.xhtml")) {
                PrincipleResultsChartViewBean.init();
                //
                maturityLevelResultsChartViewBean.init();
                PrincipleResilienceChartViewBean.init();                
            } else if (str.contains("/QuEP-Tool/reports/frmPrinciplesResilienceResults.xhtml")) {
                PrincipleResilienceChartViewBean.init();
                //
                PrincipleResultsChartViewBean.init();
                maturityLevelResultsChartViewBean.init();
            } else if (str.contains("/QuEP-Tool/all/frmQuestionnaireDynamic.xhtml")) {
                questionnaireDynamicBean.init();
            }
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
        try {
            return bandOrganization;
        } catch (Exception e) {
             validateOrganization();
            return false;
        }
        
    }

    public boolean getBandOrganization() {
        try {
            return bandOrganization;
        } catch (Exception e) {
            validateOrganization();
            return false;
        }        
    }
    
    public void validateOrganization(){
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String str=navigationBean.getRequestContextPath();
            credentialBean.closeSession();
            try {
                ec.redirect(str);
            } catch (IOException ex) {
                Logger.getLogger(OrganizationBean.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    public PrincipleResilienceChartViewBean getPrincipleResilienceChartViewBean() {
        return PrincipleResilienceChartViewBean;
    }

    public void setPrincipleResilienceChartViewBean(PrincipleResilienceChartViewBean PrincipleResilienceChartViewBean) {
        this.PrincipleResilienceChartViewBean = PrincipleResilienceChartViewBean;
    }

    public CredentialBean getCredentialBean() {
        return credentialBean;
    }

    public void setCredentialBean(CredentialBean credentialBean) {
        this.credentialBean = credentialBean;
    }

}
