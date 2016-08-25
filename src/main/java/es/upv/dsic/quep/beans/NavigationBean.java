/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author agna8685
 */
@Named
@ApplicationScoped

public class NavigationBean implements Serializable {

    public static String toLogin() {
        return "/frmInitPage.xhtml";
    }

    public static String redirectToLogin() {
        return "/frmInitPage.xhtml?faces-redirect=true";
    }

    public static String toUser() {
        return "/all/frmMainPage.xhtml";
    }

    public static String redirectToUser() {
        return "/all/frmMainPage.xhtml?faces-redirect=true";
    }

}