/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.MaturityLevelDao;
import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.Stakeholder;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author agna8685
 */
@Named
        //(value = "maturityLevelBean")
//@ViewScoped
//@ManagedBean
@SessionScoped
public class MaturityLevelBean implements Serializable {

     @Inject
    private AccessBean accessBean;
     
    private MaturityLevel maturityLevel = new MaturityLevel();
    private List<MaturityLevel> maturityLevels;

    /**
     * Creates a new instance of MaturityLevelBean
     */
    public MaturityLevelBean() {
        maturityLevel = new MaturityLevel();
        setMaturityLevelsList();
    }

    /**
     * @return the maturityLevel
     */
    public MaturityLevel getMaturityLevel() {
        return maturityLevel;
    }

    /**
     * @param maturityLevel the maturityLevel to set
     */
    public void setMaturityLevel(MaturityLevel maturityLevel) {
        this.maturityLevel = maturityLevel;
    }

    /**
     * @return the maturityLevels
     */
    public List<MaturityLevel> getMaturityLevels() {
        return maturityLevels;
    }

    /**
     * @param maturityLevels the maturityLevels to set
     */
    public void setMaturityLevels(List<MaturityLevel> maturityLevels) {     
        this.maturityLevels = maturityLevels;
    }

    public void insert() {
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        setIMaturityLevel();
        linkDao.insertMaturityLevel(maturityLevel);
        maturityLevel = new MaturityLevel();  
        //request();
        setMaturityLevelsList();
    }
    
    public void setMaturityLevelsList(){
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        maturityLevels=linkDao.getMaturityLevels();
    }
    
    public void setIMaturityLevel(){
        Stakeholder stk = new Stakeholder();
        stk = (Stakeholder) AccessBean.getSessionObj("stakeholder");
        maturityLevel.setCreationUser(stk.getEmail());
        maturityLevel.setFechaCreado(new Date());
        maturityLevel.setAudit("I");
        maturityLevel.setActive(1);
    }
    
    public void setUMaturityLevel(){
        Stakeholder stk = new Stakeholder();
        stk = (Stakeholder) AccessBean.getSessionObj("stakeholder");
        maturityLevel.setActualizado(stk.getEmail());
        maturityLevel.setModificationDate(new Date());
        maturityLevel.setAudit("U");
    }

    public void update() {
        setUMaturityLevel();
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        linkDao.updateMaturityLevel(maturityLevel);
        maturityLevel = new MaturityLevel();
     
    }

    public void delete() {
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        linkDao.deleteMaturityLevel(maturityLevel);
        maturityLevel = new MaturityLevel();
       setMaturityLevelsList();
    }
    
    
    public void request() {
        RequestContext context = RequestContext.getCurrentInstance();
        //context.addCallbackParam("add", true);    //basic parameter
        //context.addCallbackParam("user", user);     //pojo as json
        
        //update panel
        context.update("form:formInsert");
         
        //scroll to panel
        //context.scrollTo("form:formShowMaturityLevels");
         
        //add facesmessage
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success", "Success"));
    }

}
