/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.MaturityLevelPractice;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface MaturityLevelDao {
    
    public List<MaturityLevel> getMaturityLevels();
    public MaturityLevel getMaturityLevel(int id);
    public void insertMaturityLevel(MaturityLevel ml);
    public void updateMaturityLevel(MaturityLevel ml);
    public void deleteMaturityLevel(MaturityLevel ml);
    
    public List<MaturityLevelPractice> getMaturityLevelsPractice() ;
}
