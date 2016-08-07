/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.dsic.quep.model.Stakeholder;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface RoleStakeholderDao {
    
    public RoleStakeholder getRoleStakeholder(Stakeholder stk);
}
