/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.dsic.quep.model.Stakeholder;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface RoleStakeholderDao {
    
    public Role getRole(Stakeholder stk);
    public Organization getOrganization(int idStakeholder, int idRole);
    public List<Organization> getListOrganization(int idStakeholder, int idRole);
    public RoleStakeholder getRoleByOrganization(int role, int organization); 
    
    public RoleStakeholder getRoleStakeholder(String user, String password);
    
    public List<Role>  getLstRoles();
    
}
