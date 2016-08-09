/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped
public class AccessBean implements Serializable{
    
    public static Object getSessionObj(String id) {
   return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(id);
}

public static void setSessionObj(String id,Object obj){
   FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(id, obj);
}
    
}
