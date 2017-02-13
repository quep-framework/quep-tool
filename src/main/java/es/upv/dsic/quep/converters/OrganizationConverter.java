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
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
//@FacesConverter("organizationConverter")
@Named
public class OrganizationConverter implements Converter, Serializable {

    @Inject
    private OrganizationBean organizationBean;

    public OrganizationConverter() {

    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                //OrganizationBean orgB = (OrganizationBean) fc.getExternalContext().getSessionMap().get("organizationBean");
                //return orgB.getOrganization(Integer.parseInt(value));

                return organizationBean.getOrganization(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
            }
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return String.valueOf(((Organization) object).getId());
        } else {
            return null;
        }
    }
}
