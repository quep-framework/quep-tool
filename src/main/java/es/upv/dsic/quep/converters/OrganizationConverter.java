/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.converters;

/**
 *
 * @author agna8685
 */

import es.upv.dsic.quep.beans.OrganizationBean;
import es.upv.dsic.quep.model.Organization;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
 

@SessionScoped
@FacesConverter("organizationConverter")
public class OrganizationConverter implements Converter {
 
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                OrganizationBean org = (OrganizationBean) fc.getExternalContext().getApplicationMap().get("organizationBean");
                return org.getLstOrganization().get(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        }
        else {
            return null;
        }
    }
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Organization) object).getId());
        }
        else {
            return null;
        }
    }   
}         