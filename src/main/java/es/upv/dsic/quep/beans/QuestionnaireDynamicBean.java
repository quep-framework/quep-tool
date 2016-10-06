/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import com.sun.org.apache.xpath.internal.operations.Div;
import es.upv.dsic.quep.dao.MenuDao;
import es.upv.dsic.quep.dao.MenuDaoImplement;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuestionType;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
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
import java.util.Iterator;
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
import javax.inject.Inject;

import javax.inject.Named;
import javax.ws.rs.core.Form;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.column.Column;
import org.primefaces.component.columngroup.ColumnGroup;
import org.primefaces.component.datatable.DataTable;

import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.row.Row;
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
    private Calendar cal;
    private Tab tab;
    private PanelGrid panel;

    //private SelectOneRadio rB;
    private SelectOneRadio rB;
    private OutputLabel lbl;
    private OutputLabel lbl1;
    private OutputLabel lblTxtA;
    private OutputLabel lblRsp;
    // private OutputLabel lblItem;
    private InputTextarea txtA;
    private SelectManyCheckbox selManyChk;
    private PanelGrid pnl;
    private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ;

    // @PostConstruct
    // public void init() {
    @Inject
    private AccessBean accessBean;

    public QuestionnaireDynamicBean() {
        //  @PostConstruct
        //public void init() {
        //QuestioannaireDaoImplement qDaoImp = new QuestioannaireDaoImplement();
        Role role = new Role();

        role = (Role) AccessBean.getSessionObj("role");

        lstQuestionnaireQQ = new ArrayList<QuestionnaireQuepQuestion>();
        QuestioannaireDaoImplement qdi = new QuestioannaireDaoImplement();
        lstQuestionnaireQQ = qdi.getQuestionnairesQQbyRole(role.getId());
        int size = lstQuestionnaireQQ.size();
        int i = 0;

        if (lstQuestionnaireQQ != null) {

            panel = new PanelGrid();
            panel.setId("p1");

            tabview = new TabView();
            tabview.setId("tv1");
            lbl = new OutputLabel();
            lbl.setValue("Tabview");
            tab = new Tab();
            tab.setId("t1");
            tab.setTitle("Q1-Q5");

            for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
                QuestionnaireQuepQuestion next = it.next();

                PanelGrid pnl0 = new PanelGrid();
                pnl0.setId("pnl" + String.valueOf(i));
                pnl0.setColumns(1);
                pnl0.setStyleClass("panelNoBorder");

                QuepQuestion qq = new QuepQuestion();
                qq = next.getQuepQuestion();

                //dinamic component
                lbl1 = new OutputLabel();
                lbl1.setId("lbl1"+i);
                lbl1.setValue(qq.getDescription());
                pnl0.getChildren().add(lbl1);
                
                pnl = new PanelGrid();
                pnl.setId("pnl"+i);
                pnl.setColumns(2);
                pnl.setStyleClass("panelNoBorder");

                //static component
                OutputLabel lbl3 = new OutputLabel();
                lbl3.setValue("Response:");
                pnl.getChildren().add(lbl3);

                //dinamic component
                QuestionType qt=new QuestionType();
                qt=qq.getQuestionType();
                
                
                rB = new SelectOneRadio();
                rB.setId("rB");
                List<SelectItem> items = new ArrayList<SelectItem>();
                items.add(new SelectItem("Yes", "Yes"));
                items.add(new SelectItem("No", "No"));
                UISelectItems selectItems = new UISelectItems();
                selectItems.setValue(items);
                rB.getChildren().add(selectItems);
                pnl.getChildren().add(rB);

                lblTxtA = new OutputLabel();
                lblTxtA.setValue("Comments:");
                pnl.getChildren().add(lblTxtA);

                txtA = new InputTextarea();
                txtA.setTitle("Response:");
                txtA.setLabel("Response:");
                txtA.setId("txtA1");
                pnl.getChildren().add(txtA);
                
                tab.getChildren().add(pnl0);
                tab.getChildren().add(pnl);
            
                i++;

            }

            

            tabview.getChildren().add(lbl);
            tabview.getChildren().add(tab);

            panel.getChildren().add(tabview);
        }
    }

    public TabView getTabview() {
        return tabview;
    }

    public void setTabview(TabView tabview) {
        this.tabview = tabview;
    }

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
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

    public OutputLabel getLblTxtA() {
        return lblTxtA;
    }

    public void setLblTxtA(OutputLabel lblTxtA) {
        this.lblTxtA = lblTxtA;
    }

    public OutputLabel getLblRsp() {
        return lblRsp;
    }

    public void setLblRsp(OutputLabel lblRsp) {
        this.lblRsp = lblRsp;
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

    public List<QuestionnaireQuepQuestion> getLstQuestionnaireQQ() {
        return lstQuestionnaireQQ;
    }

    public void setLstQuestionnaireQQ(List<QuestionnaireQuepQuestion> lstQuestionnaireQQ) {
        this.lstQuestionnaireQQ = lstQuestionnaireQQ;
    }

    public AccessBean getAccessBean() {
        return accessBean;
    }

    public void setAccessBean(AccessBean accessBean) {
        this.accessBean = accessBean;
    }

}
