/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Stakeholder;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface StakeholderDao {
    
    public List<Stakeholder> getStakeholders();
    public void insertStakeholder(Stakeholder stk);
    public void updateStakeholder(Stakeholder stk);
    public void deleteStakeholder(Stakeholder stk);
    public Stakeholder login(String user, String password);
    public Stakeholder logout(String user);
}
