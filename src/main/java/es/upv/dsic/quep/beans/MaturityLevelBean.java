/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.MaturityLevelDao;
import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
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
//(value = "maturityLevelBean")
//@ViewScoped
//@ManagedBean
@SessionScoped
public class MaturityLevelBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private MaturityLevel maturityLevel = new MaturityLevel();
    private List<MaturityLevel> maturityLevels;

    /**
     * Creates a new instance of MaturityLevelBean
     */
    public MaturityLevelBean() {

    }

    @PostConstruct
    private void init() {
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

    public void setMaturityLevelsList() {
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        maturityLevels = linkDao.getMaturityLevels();
    }

    public void setIMaturityLevel() {
        maturityLevel.setCreationUser(loginBean.getStakeholder().getEmail());
        maturityLevel.setFechaCreado(new Date());
        maturityLevel.setAudit("I");
        maturityLevel.setActive(1);
    }

    public void setUMaturityLevel() {
        maturityLevel.setActualizado(loginBean.getStakeholder().getEmail());
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
        context.update("form:formInsert");
    }

}
