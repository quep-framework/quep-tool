/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.MenuDao;
import es.upv.dsic.quep.dao.MenuDaoImplement;
import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.model.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGroup;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItems;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

import javax.faces.model.SelectItem;

import javax.inject.Named;
import javax.ws.rs.core.Form;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;


import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectoneradio.SelectOneRadio;

import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped

public class QuestionnaireDynamicBean implements Serializable {

    private TabView tabview;
    private Tab tab;
    private PanelGrid panel;
    //private SelectOneRadio rB;
    private SelectOneRadio rB;
    private OutputLabel lbl;
    private OutputLabel lbl1;
    // private OutputLabel lblItem;
    private InputTextarea txtA;
    private SelectManyCheckbox selManyChk;
    private PanelGrid pnl;

    @PostConstruct
    public void init() {
        panel = new PanelGrid();
        panel.setId("p1");

        tabview = new TabView();
        tabview.setId("tv1");
        lbl = new OutputLabel();
        lbl.setValue("Tabview");
        tab = new Tab();
        tab.setId("t1");
        tab.setTitle("Q1-Q5");

        pnl = new PanelGrid();
        pnl.setId("pnl");
        pnl.setColumns(2);

        lbl1 = new OutputLabel();
        lbl1.setValue("Q1. Has the organization performed any emergency drill?");
        pnl.getChildren().add(lbl1);
       
        rB = new SelectOneRadio();
        rB.setId("rB");
        List<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem("Yes", "Yes"));
        items.add(new SelectItem("No", "No"));
        UISelectItems selectItems = new UISelectItems();
        selectItems.setValue(items);
        
       /* FacesMessage message = null;
         message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Welcome", "welcome");*/
         
        rB.getChildren().add(selectItems);
        pnl.getChildren().add(rB);

        
       /* form = new UIForm();
        form.setId("frmRB");
        form.getChildren().clear();
         Application application = FacesContext.getCurrentInstance()
                .getApplication();
        AjaxBehavior ajax = new AjaxBehavior();
        ajax.addAjaxBehaviorListener(new AjaxBehaviorListenerImpl(createActionMethodExpression("#{questionnaireDynamicBean.processAction}"), ajax.getListener()));
        SelectOneRadio radio = (SelectOneRadio) application.createComponent(SelectOneRadio.COMPONENT_TYPE);
        radio.setId("radioId");
        ValueExpression vExp = createValueExpression("#{questionnaireDynamicBean.selectedValue}", String.class);
        radio.setValueExpression("value", vExp);
        radio.addClientBehavior("change", ajax);
        List<SelectItem> items1 = new ArrayList<SelectItem>();
        items1.add(new SelectItem("Yes1", "Yes1"));
        items1.add(new SelectItem("No1", "No1"));
        UISelectItems selectItems1 = new UISelectItems();
        selectItems1.setValue(items1);
        radio.getChildren().add(selectItems1);
        form.getChildren().add(radio);
        pnl.getChildren().add(form);*/

        txtA = new InputTextarea();
        txtA.setTitle("Response:");
        txtA.setLabel("Response:");
        txtA.setId("txtA1");
        pnl.getChildren().add(txtA);

        tab.getChildren().add(pnl);

        tabview.getChildren().add(lbl);
        tabview.getChildren().add(tab);
        // tabview.getChildren().add(rB);

        panel.getChildren().add(tabview);
    }

    /*public List<SelectItem> getListItems() {
       
        List<SelectItem> lst = new ArrayList<>();
        SelectItem i = new SelectItem();
        i.setDescription("Yes");
        i.setValue("Yes");
        i.setLabel("Yes");
        i.setDisabled(false);
        lst.add(i);

        i.setValue("No");
        i.setDescription("No");
        i.setLabel("No");
        i.setDisabled(false);
        lst.add(i);

        return lst;
    }*/
   /* public MethodExpression createActionMethodExpression(String name) {
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ELContext elContext = facesCtx.getELContext();
        return facesCtx
                .getApplication()
                .getExpressionFactory()
                .createMethodExpression(elContext, name, String.class,
                        new Class[]{});
    }

    public ValueExpression createValueExpression(String valueExpression,
            Class<?> valueType) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context
                .getApplication()
                .getExpressionFactory()
                .createValueExpression(context.getELContext(), valueExpression,
                        valueType);
    }

    public void processAction() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Info", "SelectedValue = " + selectedValue));
    }

    private String selectedValue;
    private UIForm form;

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public UIForm getForm() {
        return form;
    }

    public void setForm(UIForm form) {
        this.form = form;
    }*/

    public TabView getTabview() {
        return tabview;
    }

    public void setTabview(TabView tabview) {
        this.tabview = tabview;
    }

    public Tab getTab() {
        return tab;
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public PanelGrid getPanel() {
        return panel;
    }

    public void setPanel(PanelGrid panel) {
        this.panel = panel;
    }

    public SelectOneRadio getrB() {
        return rB;
    }

    public void setrB(SelectOneRadio rB) {
        this.rB = rB;
    }

    public OutputLabel getLbl() {
        return lbl;
    }

    public void setLbl(OutputLabel lbl) {
        this.lbl = lbl;
    }

    public OutputLabel getLbl1() {
        return lbl1;
    }

    public void setLbl1(OutputLabel lbl1) {
        this.lbl1 = lbl1;
    }

    public InputTextarea getTxtA() {
        return txtA;
    }

    public void setTxtA(InputTextarea txtA) {
        this.txtA = txtA;
    }

    public SelectManyCheckbox getSelManyChk() {
        return selManyChk;
    }

    public void setSelManyChk(SelectManyCheckbox selManyChk) {
        this.selManyChk = selManyChk;
    }

    public PanelGrid getPnl() {
        return pnl;
    }

    public void setPnl(PanelGrid pnl) {
        this.pnl = pnl;
    }

    
}
