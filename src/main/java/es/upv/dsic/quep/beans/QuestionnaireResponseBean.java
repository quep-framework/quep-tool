/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

//import es.upv.dsic.quep.dao.QuestionnaireResponseDao;
//import es.upv.dsic.quep.dao.QuestionnaireResponseDaoImplement;
import es.upv.dsic.quep.dao.QuestionnaireResponseDao;
import es.upv.dsic.quep.dao.QuestionnaireResponseDaoImplement;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author agna8685
 */
@Named
//(value = "oQRBean")
//@ViewScoped
//@ManagedBean
@SessionScoped
public class QuestionnaireResponseBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private QuestionnaireResponse oQR = new QuestionnaireResponse();
    private List<QuestionnaireResponse> lstOQR;

    /**
     * Creates a new instance of QuestionnaireResponseBean
     */
    public QuestionnaireResponseBean() {

    }

    @PostConstruct
    private void init() {
        oQR = new QuestionnaireResponse();
       setQuestionnaireResponsesList();
    }

    public QuestionnaireResponse getoQR() {
        return oQR;
    }

    public void setoQR(QuestionnaireResponse oQR) {
        this.oQR = oQR;
    }

    public List<QuestionnaireResponse> getLstOQR() {
        return lstOQR;
    }

    public void setLstOQR(List<QuestionnaireResponse> lstOQR) {
        this.lstOQR = lstOQR;
    }
    

    public void insert() {
        QuestionnaireResponseDao linkDao = new QuestionnaireResponseDaoImplement();
        setIQuestionnaireResponse();
        linkDao.insertQuestionnaireResponse(oQR);
        oQR = new QuestionnaireResponse();
        //request();
        setQuestionnaireResponsesList();
    }

    public void setQuestionnaireResponsesList() {
        QuestionnaireResponseDao linkDao = new QuestionnaireResponseDaoImplement();
        lstOQR = linkDao.getQuestionnaireResponses();
    }

    public void setIQuestionnaireResponse() {
        oQR.setCreationUser(loginBean.getStakeholder().getEmail());
        oQR.setCreationDate(new Date());
        oQR.setAudit("I");
        oQR.setActive(1);
    }

    public void setUQuestionnaireResponse() {
        oQR.setModificationUser(loginBean.getStakeholder().getEmail());
        oQR.setModificationDate(new Date());
        oQR.setAudit("U");
    }

    public void update() {
        setUQuestionnaireResponse();
        QuestionnaireResponseDao linkDao = new QuestionnaireResponseDaoImplement();
        linkDao.updateQuestionnaireResponse(oQR);
        oQR = new QuestionnaireResponse();

    }

    public void delete() {
       QuestionnaireResponseDao linkDao = new QuestionnaireResponseDaoImplement();
       linkDao.deleteQuestionnaireResponse(oQR);
        oQR = new QuestionnaireResponse();
        setQuestionnaireResponsesList();
    }

    public void request() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:formInsert");
    }

}
