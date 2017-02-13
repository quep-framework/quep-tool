/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import static es.upv.dsic.quep.beans.QuestionnaireDynamicBean.prefixIdRO;
import static es.upv.dsic.quep.beans.QuestionnaireDynamicBean.prefixQ;
import static es.upv.dsic.quep.beans.QuestionnaireDynamicBean.prefixTxtComments;
import es.upv.dsic.quep.dao.RoleStakeholderDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.dsic.quep.model.Stakeholder;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.selectoneradio.SelectOneRadio;

/**
 *
 * @author agna8685
 */
@Named
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
    private AccessBean accessBean;

    @Inject
    private QuestionnaireDynamicBean questionnaireDynamicBean;
    
    @Inject
    private ResultsChartViewBean resultsChartViewBean;

    //@Inject
    //private RoleStakeholder roleStakeholder;
    public OrganizationBean() {
        RoleStakeholderDaoImplement roleStkImpl = new RoleStakeholderDaoImplement();
        RoleStakeholder rs = (RoleStakeholder) accessBean.getSessionObj("roleStakeholder");

        Role role = new Role();
        role = (Role) accessBean.getSessionObj("role");
        role = rs.getRole();

        Stakeholder stk = new Stakeholder();
        stk = (Stakeholder) accessBean.getSessionObj("stakeholder");
        //stk=rs.getStakeholder();

        //lstOrganization = new ArrayList<>(0);
        lstOrganization = roleStkImpl.getListOrganization(rs.getStakeholder().getId(), rs.getRole().getId());
        //lstOrganization = roleStkImpl.getListOrganization(roleStakeholder.getStakeholder().getId(), roleStakeholder.getRole().getId());

        bandOrganization = lstOrganization.size() > 1;

        if (!bandOrganization) {
            nameOrganization = lstOrganization.get(0).getName();

        }
        if (accessBean.getSessionObj("organization") == null) {
            accessBean.setSessionObj("organization", lstOrganization.get(0));
            this.organization = lstOrganization.get(0);
        } else {
            this.organization = (Organization) accessBean.getSessionObj("organization");
        }

        accessBean.setSessionObj("organizationBean", this);                     
    }

    //***
    public void changeListener(ValueChangeEvent event) throws IOException {        
        Object newValue = event.getNewValue();                
        Object oldValue = accessBean.getSessionObj("organization");        
        accessBean.setSessionObj("organization", newValue);
        setOrganization((Organization) newValue);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        
        Role oRole = (Role) accessBean.getSessionObj("role");
        if (loginBean.checkUserByOrganization(oRole.getId(), organization.getId())) {
            String str=navigationBean.getCurrentPage();
            if (str.contains("/QuEP-Tool/admin/frmMaturityLevelsResults.xhtml")){
                resultsChartViewBean = new ResultsChartViewBean();
                //resultsChartViewBean.
                /*AjaxBehavior ajaxBehavior = (AjaxBehavior) FacesContext.getCurrentInstance().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
                ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxListener());
                ajaxBehavior.setTransient(true);*/
                
                //txtComments.addClientBehavior("change", ajaxBehavior);
            }
            else if (str.contains("/QuEP-Tool/all/frmQuestionnaireDynamic.xhtml")){
                questionnaireDynamicBean = new QuestionnaireDynamicBean();
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

}
