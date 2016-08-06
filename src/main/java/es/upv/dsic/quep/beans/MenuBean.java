/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.MenuDao;
import es.upv.dsic.quep.dao.MenuDaoImplement;
import es.upv.dsic.quep.model.Menu;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

//import javax.ejb.EJB;

import javax.inject.Named;
//import javax.faces.view.ViewScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author agna8685
 */
@Named
//(value = "menuBean")
@SessionScoped
//@ViewScoped

public class MenuBean implements Serializable {

    private List<Menu> lstMenu;
    private Menu menu = new Menu();
    private MenuModel model;

    public MenuBean() {
        init();
    }

    //@PostConstruct
    public void init() {
        this.getListMenuRol();
        model = new DefaultMenuModel();
        this.establecerpermisos();
    }

    /**
     * @return the maturityLevels
     */
    public void getListMenuRol() {
        MenuDao linkDao = new MenuDaoImplement();        
        //Role r=(Role) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("stakeholder");
        int idRol = 6;
        this.lstMenu = linkDao.getMenuRol(idRol);
    }

    public void establecerpermisos() {
        for (Menu m : lstMenu) {
            if (m.getType().equals('S')) {
                DefaultSubMenu firstSubmenu = new DefaultSubMenu(m.getName());
                for (Menu i : lstMenu) {
                    Menu submenu = i.getMenu();
                    if (submenu != null) {
                        if (submenu.getId() == m.getId()) {
                            DefaultMenuItem item = new DefaultMenuItem(i.getName());
                            item.setUrl(i.getUrl());
                            firstSubmenu.addElement(item);
                        }
                    }
                }
                model.addElement(firstSubmenu);
            } else if (m.getMenu() == null) {
                DefaultMenuItem item = new DefaultMenuItem(m.getName());
                item.setUrl(m.getUrl());
                model.addElement(item);
            }
        }
    }
    
 
    public List<Menu> getLstMenu() {
        return lstMenu;
    }

    public void setLstMenu(List<Menu> lstMenu) {
        this.lstMenu = lstMenu;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

}
