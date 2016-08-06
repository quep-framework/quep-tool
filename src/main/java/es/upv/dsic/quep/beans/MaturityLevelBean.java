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
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author agna8685
 */
@Named(value = "maturityLevelBean")
@ViewScoped
public class MaturityLevelBean implements Serializable {

    private MaturityLevel maturityLevel = new MaturityLevel();
    private List<MaturityLevel> maturityLevels;

    /**
     * Creates a new instance of MaturityLevelBean
     */
    public MaturityLevelBean() {
        maturityLevel = new MaturityLevel();
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        setMaturityLevels(linkDao.getMaturityLevels());
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
        linkDao.insertMaturityLevel(maturityLevel);
        maturityLevel = new MaturityLevel();
    }

    public void update() {
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        linkDao.updateMaturityLevel(maturityLevel);
        maturityLevel = new MaturityLevel();
    }

    public void delete() {
        MaturityLevelDao linkDao = new MaturityLevelDaoImplement();
        linkDao.deleteMaturityLevel(maturityLevel);
        maturityLevel = new MaturityLevel();
    }

}
