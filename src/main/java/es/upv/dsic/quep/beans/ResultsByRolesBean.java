/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuepQuestionDaoImplement;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestionId;
import es.upv.dsic.quep.model.Role;
import es.upv.es.dsic.quep.utils.ResponseEstimate;
import es.upv.es.dsic.quep.utils.Result;
import es.upv.es.dsic.quep.utils.Results;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class ResultsByRolesBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    @Inject
    private OrganizationBean organizationBean;

    private Organization oOrganization = null;
    //private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ = new ArrayList<QuestionnaireQuepQuestion>();
    //private Map<Role, ResponseEstimate> mRoleResults = new HashMap<Role, ResponseEstimate>();
    private List<RoleResult> lstRoleResults = null;

    public ResultsByRolesBean() {
    }

    @PostConstruct
    public void init() {
        oOrganization = organizationBean.getOrganization();
        initList();
    }

    public void initList() {
        Results results = new Results();
        Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions = new HashMap<QuepQuestion, ResponseEstimate>();
        mapSumQuepQuestions = new HashMap<QuepQuestion, ResponseEstimate>();
        mapSumQuepQuestions = results.calculateQuestions(oOrganization.getId());

        Map<Role, ResponseEstimate> mRoleResponseEstimate = new HashMap<Role, ResponseEstimate>();
        mRoleResponseEstimate = results.calculateRolesBySumQuestions(mapSumQuepQuestions, oOrganization.getId());
        
        Map<Role, Result> mRoleResult = new HashMap<Role, Result>();
        mRoleResult= getResultsbyRole(mRoleResponseEstimate);

        List<RoleResult> lstRoleResultsAux = new ArrayList<RoleResult>();
        
        for (Map.Entry<Role, Result> entry : mRoleResult.entrySet()) {
            Role role = entry.getKey();
            Result result = entry.getValue();
            
            RoleResult oRR = new RoleResult();
            oRR.setResult(result);
            oRR.setRole(role);
            lstRoleResultsAux.add(oRR);
        }
        
        lstRoleResults = new ArrayList<RoleResult>();
        lstRoleResults.addAll(lstRoleResultsAux);
        //lstRoleResults=lstRoleResultsAux;
        
      /*  for (RoleResult roleResult : lstRoleResultsAux) {
            lstRoleResults.add(roleResult);
        */
    }
    
    public Map<Role, Result> getResultsbyRole(Map<Role, ResponseEstimate> mRoleResponseEstimate) {
        try {
            Map<Role, Result> mRoleResult = new HashMap<Role, Result>();
            if (mRoleResponseEstimate.size() > 0 && mRoleResponseEstimate != null) {
                for (Map.Entry<Role, ResponseEstimate> mapPri : mRoleResponseEstimate.entrySet()) {
                    Role role = mapPri.getKey();
                    ResponseEstimate rsp = mapPri.getValue();
                    Result oResult = new Result();
                    BigDecimal dComplete = rsp.getAvg();
                    oResult.setComplete(dComplete);
                    oResult.setPerComplete(getResultsPerComplete(dComplete));
                    mRoleResult.put(role, oResult);
                }
            }
            return mRoleResult;
        } catch (Exception e) {
            return null;
        }
    }
    
    public BigDecimal getResultsPerComplete(BigDecimal dResult) {
        try {
            BigDecimal dResultPerComplete;
            dResultPerComplete = dResult.subtract(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(-1));
            return dResultPerComplete;
        } catch (Exception e) {
            return null;
        }
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

    public List<RoleResult> getLstRoleResults() {
        return lstRoleResults;
    }

    public void setLstRoleResults(List<RoleResult> lstRoleResults) {
        this.lstRoleResults = lstRoleResults;
    }

   

    public class RoleResult {

        private Role role;
        private Result result;

        public RoleResult() {
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }

    }

}
