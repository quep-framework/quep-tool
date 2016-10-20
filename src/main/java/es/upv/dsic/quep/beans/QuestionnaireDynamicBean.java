/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuestionType;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.model.QuestionnaireResponseId;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.ResponseId;
import es.upv.dsic.quep.model.ResponseOption;
import es.upv.dsic.quep.model.Role;
import es.upv.dsic.quep.model.RoleStakeholder;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import javax.inject.Named;

import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.inputtext.InputText;

import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;

import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;

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

    private SelectOneMenu cmb;
    private SelectOneRadio rB;
    private SelectManyCheckbox chk;
    private InputText txt;
    private OutputLabel lblTabview;
    private OutputLabel lblQuestion;
    private OutputLabel lblTxtComments;
    private InputTextarea txtComments;
    private PanelGrid pnlResponseOptionC2;
    private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ;

    UISelectItems selectItems;

    QuestioannaireDaoImplement qdi = new QuestioannaireDaoImplement();

    private CommandButton btnSave;
    private Response oResponse;
    private QuestionnaireResponse oQResponse;

    @Inject
    private AccessBean accessBean;

    private RoleStakeholder oRoleStakeholder = null;

    /* public QuestionnaireDynamicBean() {
        try {
            oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
            buildQuestionnaire();
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

    }*/
    @PostConstruct
    public void init() {
        /**
         * This map contains all the params you submitted from the html form
         */

        try {
            oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
            buildQuestionnaire();

        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

    public void buildQuestionnaire() {
        Role role = new Role();
        role = oRoleStakeholder.getRole();

        lstQuestionnaireQQ = new ArrayList<QuestionnaireQuepQuestion>();

        lstQuestionnaireQQ = qdi.getQuestionnairesQQbyRole(role.getId());

        int i = 0;

        if (lstQuestionnaireQQ != null) {

            panel = new PanelGrid();
            panel.setId("pnlMain");

            tabview = new TabView();
            tabview.setId("tvQ");
            lblTabview = new OutputLabel();
            lblTabview.setValue("Tabview");

            for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {

                //create tabs
                if (i < lstQuestionnaireQQ.size() && (i % 5 == 0)) {
                    tab = new Tab();
                    tab.setId("tab1_" + String.valueOf(i));
                    tab.setTitle("Q" + String.valueOf(i + 1) + "-Q" + String.valueOf(i + 5));
                }

                QuestionnaireQuepQuestion next = it.next();
                QuepQuestion qq = new QuepQuestion();
                qq = next.getQuepQuestion();

                //create dinamic component (label question)
                PanelGrid pnlResponseOptionC1 = new PanelGrid();
                pnlResponseOptionC1.setId("pnlResponseOptionC1_" + String.valueOf(i));
                pnlResponseOptionC1.setColumns(1);
                pnlResponseOptionC1.setStyleClass("panelNoBorder");
                lblQuestion = new OutputLabel();
                lblQuestion.setId("lblTabview" + String.valueOf(i));
                lblQuestion.setValue(String.valueOf(i + 1) + ". " + qq.getDescription());
                pnlResponseOptionC1.getChildren().add(lblQuestion);

                //create dinamic component (response option)
                pnlResponseOptionC2 = new PanelGrid();
                pnlResponseOptionC2.setId("pnlResponseOptionC2_" + String.valueOf(i));
                pnlResponseOptionC2.setColumns(2);
                pnlResponseOptionC2.setStyleClass("panelNoBorder");

                //static component
                OutputLabel lblResponse = new OutputLabel();
                lblResponse.setValue("Response:");
                lblResponse.setId("lblResponse_" + String.valueOf(qq.getId()));
                lblResponse.setFor("idRO_" + String.valueOf(qq.getId()));
                pnlResponseOptionC2.getChildren().add(lblResponse);

                //response option components
                QuestionType qt = new QuestionType();
                qt = qq.getQuestionType();

                //create radio button
                if (qt.getName().toLowerCase().trim().contains("radio")) {
                    rB = new SelectOneRadio();
                    rB.setId("idRO_" + String.valueOf(qq.getId()));

                    List<SelectItem> items = new ArrayList<SelectItem>();
                    List<ResponseOption> lstqQRO = new ArrayList<ResponseOption>(0);
                    lstqQRO = qdi.getResponseOptions(qq.getId());

                    for (Iterator<ResponseOption> it1 = lstqQRO.iterator(); it1.hasNext();) {
                        ResponseOption ro = it1.next();
                        items.add(new SelectItem(ro.getId(), ro.getName()));
                    }

                    selectItems = new UISelectItems();
                    selectItems.setId("idItms_" + String.valueOf(qq.getId()));
                    selectItems.setValue(items);
                    rB.getChildren().add(selectItems);
                    rB.setColumns(qt.getItemNumber());

                    pnlResponseOptionC2.getChildren().add(rB);
                } else if (qt.getName().toLowerCase().trim().contains("check")) {
                    chk = new SelectManyCheckbox();
                    chk.setId("idRO_" + String.valueOf(qq.getId()));
                    
                    List<ResponseOption> lstqQRO = new ArrayList<ResponseOption>(0);
                    lstqQRO = qdi.getResponseOptions(qq.getId());

                    List<SelectItem> items = new ArrayList<SelectItem>();
                    
                    for (Iterator<ResponseOption> it1 = lstqQRO.iterator(); it1.hasNext();) {
                        ResponseOption ro = it1.next();
                        items.add(new SelectItem(ro.getId(), ro.getName()));
                    }

                    selectItems = new UISelectItems();
                    selectItems.setId("idItms_" + String.valueOf(qq.getId()));
                    selectItems.setValue(items);

                    pnlResponseOptionC2.getChildren().add(selectItems);

                    chk.getChildren().add(selectItems);
                    chk.setColumns(qt.getItemNumber());

                    pnlResponseOptionC2.getChildren().add(chk);
                } else if (qt.getName().toLowerCase().trim().contains("text")) {
                    txt = new InputText();
                    txt.setId("idRO_" + String.valueOf(qq.getId()));

                    pnlResponseOptionC2.getChildren().add(txt);
                } else if (qt.getName().toLowerCase().trim().contains("combo")) {
                    cmb = new SelectOneMenu();
                    cmb.setId("idRO_" + String.valueOf(qq.getId()));

                    List<SelectItem> items = new ArrayList<SelectItem>();
                    List<ResponseOption> lstqQRO = new ArrayList<ResponseOption>(0);
                    lstqQRO = qdi.getResponseOptions(qq.getId());

                    for (Iterator<ResponseOption> it1 = lstqQRO.iterator(); it1.hasNext();) {
                        ResponseOption ro = it1.next();
                        items.add(new SelectItem(ro.getId(), ro.getName()));
                    }

                    selectItems = new UISelectItems();
                    selectItems.setValue(items);

                    cmb.getChildren().add(selectItems);

                    pnlResponseOptionC2.getChildren().add(cmb);
                }

                //text area component (comments)
                if (qq.getHasComment() == 1) {
                    lblTxtComments = new OutputLabel();
                    lblTxtComments.setValue("Comments:");
                    pnlResponseOptionC2.getChildren().add(lblTxtComments);
                    txtComments = new InputTextarea();

                    txtComments.setAccesskey("txtComments_" + String.valueOf(qq.getId()));
                    txtComments.setId("txtComments_" + String.valueOf(qq.getId()));
                    //txtComments.setValue(#{this..});
                    pnlResponseOptionC2.getChildren().add(txtComments);
                }

                //has page number
                if (qq.getHasPageNumber() == 1) {
                    OutputLabel lblTxtPageNumber = new OutputLabel();
                    lblTxtPageNumber.setValue("Page Number:");
                    pnlResponseOptionC2.getChildren().add(lblTxtPageNumber);
                    InputText txtPageNumber = new InputText();
                    txtPageNumber.setId("txtPageNumber_" + String.valueOf(qq.getId()));
                    pnlResponseOptionC2.getChildren().add(txtPageNumber);
                }

                tab.getChildren().add(pnlResponseOptionC1);
                tab.getChildren().add(pnlResponseOptionC2);

                i++;
            }

            tabview.getChildren().add(lblTabview);
            tabview.getChildren().add(tab);

            panel.getChildren().add(tabview);
        }
    }

   /* public void save(ActionEvent actionEvent) {
        saveResponse();
    }*/

    public void saveResponse() {
        List<Response> lstRsp = new ArrayList<Response>();
        List<QuestionnaireResponse> lstQRsp = new ArrayList<QuestionnaireResponse>();

        Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        Map<String, Object> obj_requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        //FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap()
        String prefixQ = "frmQ:tvQ:";

        for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
            QuestionnaireQuepQuestion qqq = it.next();

            //QuestionnaireResponse
            oQResponse = new QuestionnaireResponse();
            QuestionnaireResponseId qrId = new QuestionnaireResponseId(qqq.getQuestionnaire().getId().getId(),
                    qqq.getQuepQuestion().getPractice().getId(),
                    oRoleStakeholder.getStakeholder().getId(), oRoleStakeholder.getRole().getId(),
                    oRoleStakeholder.getOrganization().getId(), qqq.getQuepQuestion().getId());

            oQResponse.setId(qrId);
            oQResponse.setOrganization(oRoleStakeholder.getOrganization());
            oQResponse.setQuestionnaire(qqq.getQuestionnaire());
            oQResponse.setCreationUser(oRoleStakeholder.getStakeholder().getEmail());
            oQResponse.setCreationDate(new Date());
            oQResponse.setActive(1);
            oQResponse.setAudit("I");
            oQResponse.setIdPrinciple(qqq.getIdPrinciple());
            oQResponse.setComputedValue(0);
            lstQRsp.add(oQResponse);
            
            //Response            
            
            String sQId = String.valueOf(qqq.getQuepQuestion().getId());
            String[] lstRO = (String[]) requestParams.get(prefixQ + "idRO_" + sQId);
            for (String sRO : lstRO) {
                oResponse = new Response();
                ResponseId rId = new ResponseId(oRoleStakeholder.getStakeholder().getId(),
                        qqq.getQuepQuestion().getId(),
                        qqq.getQuepQuestion().getPractice().getId(),
                        qqq.getQuestionnaire().getId().getId(),
                        oRoleStakeholder.getRole().getId(),
                        oRoleStakeholder.getOrganization().getId(),
                        Integer.parseInt(sRO));
                oResponse.setId(rId);

                oResponse.setResponseOption(qdi.getResponseOption(qqq.getQuepQuestion().getId(), Integer.parseInt(sRO)));
                oResponse.setResponseOption_1(qdi.getResponseOption(qqq.getQuepQuestion().getId(), Integer.parseInt(sRO)).getName());

                oResponse.setQuestionnaireQuepQuestion(qqq);
                oResponse.setStakeholder(oRoleStakeholder.getStakeholder());
                oResponse.setIdPrinciple(qqq.getIdPrinciple());

                if (qqq.getQuepQuestion().getHasComment() == 1) {
                    oResponse.setComment(requestParams.get(prefixQ + "txtComments_" + sQId)[0]);
                }

                if (qqq.getQuepQuestion().getHasPageNumber() == 1) {
                    oResponse.setPagenumber(requestParams.get(prefixQ + "txtPageNumber_" + sQId)[0]);
                }

                oResponse.setComputedValue(0);
                oResponse.setCreationUser(oRoleStakeholder.getStakeholder().getEmail());
                oResponse.setCreationDate(new Date());
                oResponse.setActive(1);
                oResponse.setAudit("I");
                
                oResponse.setQuestionnaireResponse(oQResponse);           
                lstRsp.add(oResponse);

            }
        }

        Map<Integer,String> mSave = qdi.insertResponse(lstRsp, lstQRsp);
        if (mSave.containsKey(1)){
            addMessage("Save", "Response have been saving.",1);
        }
        else{
             addMessage("Error", "Please try again later."+ mSave.get(0),0);
        }
        bandSave=true;
    }
    
    boolean bandSave = false;
    
     public void save(ActionEvent ae) {
         try {
             if (!bandSave)
             saveResponse();
             
         } catch (Exception e) {
             
         }
    }
     
    public void addMessage(String summary, String detail,int band) {
        if (band==1){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
        }
        else if (band==0){
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
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
        return lblTabview;
    }

    public void setLbl(OutputLabel lblTabview) {
        this.lblTabview = lblTabview;
    }

    public OutputLabel getLbl1() {
        return lblQuestion;
    }

    public void setLbl1(OutputLabel lblQuestion) {
        this.lblQuestion = lblQuestion;
    }

    public OutputLabel getLblTxtA() {
        return lblTxtComments;
    }

    public void setLblTxtA(OutputLabel lblTxtComments) {
        this.lblTxtComments = lblTxtComments;
    }

    public InputTextarea getTxtA() {
        return txtComments;
    }

    public void setTxtA(InputTextarea txtComments) {
        this.txtComments = txtComments;
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

    public RoleStakeholder getoRoleStakeholder() {
        return oRoleStakeholder;
    }

    public void setoRoleStakeholder(RoleStakeholder oRoleStakeholder) {
        this.oRoleStakeholder = oRoleStakeholder;
    }

    public SelectManyCheckbox getChk() {
        return chk;
    }

    public void setChk(SelectManyCheckbox chk) {
        this.chk = chk;
    }

    public OutputLabel getLblTabview() {
        return lblTabview;
    }

    public void setLblTabview(OutputLabel lblTabview) {
        this.lblTabview = lblTabview;
    }

    public OutputLabel getLblQuestion() {
        return lblQuestion;
    }

    public void setLblQuestion(OutputLabel lblQuestion) {
        this.lblQuestion = lblQuestion;
    }

    public OutputLabel getLblTxtComments() {
        return lblTxtComments;
    }

    public void setLblTxtComments(OutputLabel lblTxtComments) {
        this.lblTxtComments = lblTxtComments;
    }

    public InputTextarea getTxtComments() {
        return txtComments;
    }

    public void setTxtComments(InputTextarea txtComments) {
        this.txtComments = txtComments;
    }

    public PanelGrid getPnlResponseOptionC2() {
        return pnlResponseOptionC2;
    }

    public void setPnlResponseOptionC2(PanelGrid pnlResponseOptionC2) {
        this.pnlResponseOptionC2 = pnlResponseOptionC2;
    }

    public InputText getTxt() {
        return txt;
    }

    public void setTxt(InputText txt) {
        this.txt = txt;
    }

    public SelectOneMenu getCmb() {
        return cmb;
    }

    public void setCmb(SelectOneMenu cmb) {
        this.cmb = cmb;
    }

    public CommandButton getBtnSave() {
        return btnSave;
    }

    public void setBtnSave(CommandButton btnSave) {
        this.btnSave = btnSave;
    }

    public Response getoResponse() {
        return oResponse;
    }

    public void setoResponse(Response oResponse) {
        this.oResponse = oResponse;
    }

    public QuestioannaireDaoImplement getQdi() {
        return qdi;
    }

    public void setQdi(QuestioannaireDaoImplement qdi) {
        this.qdi = qdi;
    }

    public QuestionnaireResponse getoQResponse() {
        return oQResponse;
    }

    public void setoQResponse(QuestionnaireResponse oQResponse) {
        this.oQResponse = oQResponse;
    }

    public UISelectItems getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(UISelectItems selectItems) {
        this.selectItems = selectItems;
    }

}
