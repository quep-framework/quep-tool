/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuepQuestionDaoImplement;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestionId;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.es.dsic.quep.utils.Result;
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
public class ResultsByFilterDataTableBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    @Inject
    private OrganizationBean organizationBean;

    private Organization oOrganization = null;
    private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ = new ArrayList<QuestionnaireQuepQuestion>();
    //private List<QQQuestionRoleStakeholder> lstQuestionnaireQQ = new ArrayList<QQQuestionRoleStakeholder>();

    public ResultsByFilterDataTableBean() {

    }

    @PostConstruct
    public void init() {
        oOrganization = organizationBean.getOrganization();        
        
        QuestioannaireDaoImplement qqdi = new QuestioannaireDaoImplement();
        lstQuestionnaireQQ = qqdi.getQuestionnairesQQbyOrg(oOrganization.getId());
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
    
    

    /*public List<QQQuestionRoleStakeholder> getLstQuestionnaireQQ() {
        return lstQuestionnaireQQ;
    }

    public void setLstQuestionnaireQQ(List<QQQuestionRoleStakeholder> lstQuestionnaireQQ) {
        this.lstQuestionnaireQQ = lstQuestionnaireQQ;
    }
    
    
    

    public static class QQQuestionRoleStakeholder {

        private RoleStakeholder rolestk;
        private QuestionnaireQuepQuestion questionnaireQQ;

        public QQQuestionRoleStakeholder() {
        }

        public RoleStakeholder getRolestk() {
            return rolestk;
        }

        public void setRolestk(RoleStakeholder rolestk) {
            this.rolestk = rolestk;
        }

        public QuestionnaireQuepQuestion getQuestionnaireQQ() {
            return questionnaireQQ;
        }

        public void setQuestionnaireQQ(QuestionnaireQuepQuestion questionnaireQQ) {
            this.questionnaireQQ = questionnaireQQ;
        }

        
    }*/

    public List<QuestionnaireQuepQuestion> getLstQuestionnaireQQ() {
        return lstQuestionnaireQQ;
    }

    public void setLstQuestionnaireQQ(List<QuestionnaireQuepQuestion> lstQuestionnaireQQ) {
        this.lstQuestionnaireQQ = lstQuestionnaireQQ;
    }

}
