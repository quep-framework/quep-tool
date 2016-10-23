package es.upv.dsic.quep.beans;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuestionType;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.model.QuestionnaireResponseId;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.ResponseId;
import es.upv.dsic.quep.model.ResponseOption;
import es.upv.dsic.quep.model.RoleStakeholder;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.confirmdialog.ConfirmDialog;
import org.primefaces.component.growl.Growl;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.tabview.Tab;
import org.primefaces.component.tabview.TabView;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.event.FlowEvent;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped

public class QuestionnaireDynamicBean implements Serializable {

    //private TabView tabview;
    private Calendar cal;
    //private Tab panel;
    private PanelGrid panel;
    private PanelGrid panelfrm;

    private SelectOneMenu cmb;
    private SelectOneRadio rB;
    private SelectManyCheckbox chk;
    private InputText txt;
    //private OutputLabel lblTabview;
    private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ;
    private Map<Principle, List<QuestionnaireQuepQuestion>> lstMapQuestionnaireQQ;

    private UIForm form;
    private CommandButton btnSave;
    private Growl growl;
    private ConfirmDialog confirmdialog;

    private Wizard wizard;
    private Tab tabwizad;
    private PanelGrid pnlwizad;

    private UISelectItems selectItems;

    private QuestioannaireDaoImplement qdi = new QuestioannaireDaoImplement();

    private Response oResponse;
    private QuestionnaireResponse oQResponse;
    
    List<QuestionnaireResponse> lstQuestionnaireResponse = new ArrayList<QuestionnaireResponse>();

    @Inject
    private AccessBean accessBean;

    private RoleStakeholder oRoleStakeholder = null;

    public QuestionnaireDynamicBean() {
        try {
            oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
            buildQuestionnaire();
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }

    }
    

    public void buildQuestionnaire() {           
        lstQuestionnaireQQ = new ArrayList<QuestionnaireQuepQuestion>();
        lstQuestionnaireQQ = qdi.getQuestionnairesQQRoleSthk(oRoleStakeholder.getRole().getId(), oRoleStakeholder.getStakeholder().getId());
        lstMapQuestionnaireQQ = setMapQuestionnaireQQ(lstQuestionnaireQQ);

        if (lstMapQuestionnaireQQ != null) {
            panel = new PanelGrid();
            panel.setId("pnlMain");

            form = new UIForm();
            form.setId("frmQ");

            panelfrm = new PanelGrid();
            panelfrm.setId("pnlMain");
            
            AccordionPanel apnl = new AccordionPanel();
            apnl.setId("tvQ");
            apnl.setMultiple(true);

            for (Map.Entry<Principle, List<QuestionnaireQuepQuestion>> entry : lstMapQuestionnaireQQ.entrySet()) {                                
                Principle key = entry.getKey();
                List<QuestionnaireQuepQuestion> lstQQQ = entry.getValue();
                
                Tab tabapnl=new Tab();
                tabapnl.setId("tabapnl_"+ String.valueOf(key.getId()));
                tabapnl.setTitle(key.getAbbreviation());
                
                PanelGrid panelPrinciple = new PanelGrid();
                panelPrinciple.setColumns(2);
                panelPrinciple.getChildren().add(createPrinciplePanel(key, lstQQQ));
                
                tabapnl.getChildren().add(panelPrinciple);
                apnl.getChildren().add(tabapnl);
            }

            btnSave = new CommandButton();
            growl = new Growl();
            growl.setId("message");
            growl.setShowDetail(true);
            btnSave.setId("cmdSave");
            btnSave.setValue("Save");

            btnSave.addActionListener(new ActionListener() {
                @Override
                public void processAction(ActionEvent event) throws AbortProcessingException {
                    save(event);
                }
            });
            btnSave.setUpdate("message,frmQ");
            panelfrm.getChildren().add(growl);
            panelfrm.getChildren().add(btnSave);
            form.getChildren().add(apnl);
            form.getChildren().add(panelfrm);
            panel.getChildren().add(form);

        }
    }

    public Map<Principle, List<QuestionnaireQuepQuestion>> setMapQuestionnaireQQ(List<QuestionnaireQuepQuestion> lstQQQ) {
        List<Principle> lstPri = qdi.getPrinciples(oRoleStakeholder.getRole().getId(), oRoleStakeholder.getStakeholder().getId());
        Map<Principle, List<QuestionnaireQuepQuestion>> MapQQQ = new HashMap<Principle, List<QuestionnaireQuepQuestion>>();

        for (Iterator<Principle> itPri = lstPri.iterator(); itPri.hasNext();) {
            Principle opri = itPri.next();
            List<QuestionnaireQuepQuestion> lstAuxQQQ = new ArrayList<QuestionnaireQuepQuestion>();
            for (Iterator<QuestionnaireQuepQuestion> itQQQM = lstQQQ.iterator(); itQQQM.hasNext();) {
                QuestionnaireQuepQuestion qqqm = itQQQM.next();
                if (qqqm.getQuepQuestion().getPractice().getPrinciple().getId() == opri.getId()) {
                    lstAuxQQQ.add(qqqm);
                }
            }
            MapQQQ.put(opri, lstAuxQQQ);
        }
        return MapQQQ;
    }

    private PanelGrid createPrinciplePanel(Principle key, List<QuestionnaireQuepQuestion> lstQQQ) {
        PanelGrid panel = new PanelGrid();
        int j = 0;
        panel.setId("tab_" + String.valueOf(key.getId()));
        
        //tab.setTitle(key.getName());        
        for (Iterator<QuestionnaireQuepQuestion> it = lstQQQ.iterator(); it.hasNext();) {
            QuepQuestion qq = it.next().getQuepQuestion();
            panel.getChildren().add(createQuestionPanel(qq, j));
            panel.getChildren().add(createResponseOptionPanel(qq));
            j++;
        }
        return panel;
    }

    private PanelGrid createQuestionPanel(QuepQuestion qq, int j) {
        PanelGrid pnlQuestion = new PanelGrid();
        //create dinamic component (label question)
        pnlQuestion.setId("pnlQuestion_" + String.valueOf(qq.getId()));
        pnlQuestion.setColumns(1);
        pnlQuestion.setStyleClass("panelNoBorder");
        OutputLabel lblQuestion = new OutputLabel();
        lblQuestion.setId("lblTabview" + String.valueOf(qq.getId()));
        lblQuestion.setValue(String.valueOf(j + 1) + ". " + qq.getDescription());
        pnlQuestion.getChildren().add(lblQuestion);
        return pnlQuestion;
    }

    private PanelGrid createResponseOptionPanel(QuepQuestion qq) {
        PanelGrid pnlResponseOption = new PanelGrid();
        OutputLabel lblTxtComments;
        InputTextarea txtComments;

        //create dinamic component (response option)
        pnlResponseOption = new PanelGrid();
        pnlResponseOption.setId("pnlResponseOption_" + String.valueOf(qq.getId()));
        pnlResponseOption.setColumns(2);
        pnlResponseOption.setStyleClass("panelNoBorder");

        //static component
        OutputLabel lblResponse = new OutputLabel();
        lblResponse.setValue("Response:");
        lblResponse.setId("lblResponse_" + String.valueOf(qq.getId()));
        lblResponse.setFor("idRO_" + String.valueOf(qq.getId()));
        pnlResponseOption.getChildren().add(lblResponse);

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

            pnlResponseOption.getChildren().add(rB);
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

            pnlResponseOption.getChildren().add(selectItems);

            chk.getChildren().add(selectItems);
            chk.setColumns(qt.getItemNumber());

            pnlResponseOption.getChildren().add(chk);
        } else if (qt.getName().toLowerCase().trim().contains("text")) {
            txt = new InputText();
            txt.setId("idRO_" + String.valueOf(qq.getId()));

            pnlResponseOption.getChildren().add(txt);
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

            pnlResponseOption.getChildren().add(cmb);
        }

        //text area component (comments)
        if (qq.getHasComment() == 1) {
            lblTxtComments = new OutputLabel();
            lblTxtComments.setValue("Comments:");
            pnlResponseOption.getChildren().add(lblTxtComments);
            txtComments = new InputTextarea();

            txtComments.setAccesskey("txtComments_" + String.valueOf(qq.getId()));
            txtComments.setId("txtComments_" + String.valueOf(qq.getId()));
            //txtComments.setValue(#{this..});
            pnlResponseOption.getChildren().add(txtComments);
        }

        //has page number
        if (qq.getHasPageNumber() == 1) {
            OutputLabel lblTxtPageNumber = new OutputLabel();
            lblTxtPageNumber.setValue("Page Number:");
            pnlResponseOption.getChildren().add(lblTxtPageNumber);
            InputText txtPageNumber = new InputText();
            txtPageNumber.setId("txtPageNumber_" + String.valueOf(qq.getId()));
            pnlResponseOption.getChildren().add(txtPageNumber);
        }

        return pnlResponseOption;
    }

    public Map<Integer, String> updateQuestionnaireResponse(int band) {
        lstQuestionnaireResponse = new ArrayList<QuestionnaireResponse>();
        lstQuestionnaireResponse = qdi.getQuestionnaireResponse(oRoleStakeholder.getRole().getId(), oRoleStakeholder.getStakeholder().getId());
        for (QuestionnaireResponse qr : lstQuestionnaireResponse) {
            if (qr.getStatus()!=band &&  band==1){
                qr.setStatus(band);
            }
            else if(qr.getStatus()==0){
                qr.setStatus(2);
            }
        }                
        return qdi.insertQuestionnaireResponse(lstQuestionnaireResponse);
    }

   
     public void completed(ActionEvent ae) {
        try {
            saveResponse(1);

        } catch (Exception e) {

        }
    }
   
    
    public void saveResponse(int band) {
        List<Response> lstRsp = new ArrayList<Response>();

        Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        Map<String, Object> obj_requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        //FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap()
        String prefixQ = "frmQ:tvQ:";

        for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
            QuestionnaireQuepQuestion qqq = it.next();

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

        Map<Integer, String> mSave = qdi.insertResponse(lstRsp);
        if (mSave.containsKey(1) && updateQuestionnaireResponse(band).containsKey(1)) {
            addMessage("Save", "Response have been saving.", 1);
        } else {
            addMessage("Error", "Please try again later." , 0);
        }
    }

    public void save(ActionEvent ae) {
        try {
            saveResponse(0);

        } catch (Exception e) {

        }
    }

    public void addMessage(String summary, String detail, int band) {
        if (band == 1) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else if (band == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private boolean skip;

    public String onFlowProcess(FlowEvent event) {
        if (skip) {
            skip = false;   //reset in case user goes back
            return "confirm";
        } else {
            return event.getNewStep();
        }
    }

    
    
    public void configureQuestionnaireResponse() {
        List<QuestionnaireResponse> lstQRsp = new ArrayList<QuestionnaireResponse>();
        for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
            QuestionnaireQuepQuestion qqq = it.next();
            oQResponse = new QuestionnaireResponse();
            QuestionnaireResponseId qrId = new QuestionnaireResponseId(qqq.getQuestionnaire().getId().getId(),
                    qqq.getQuepQuestion().getPractice().getId(),
                    oRoleStakeholder.getStakeholder().getId(), oRoleStakeholder.getRole().getId(),
                    oRoleStakeholder.getOrganization().getId());

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
        }
    }

   /* public TabView getTabview() {
        return tabview;
    }

    public void setTabview(TabView tabview) {
        this.tabview = tabview;
    }*/

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
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

   /* public OutputLabel getLbl() {
        return lblTabview;
    }

    public void setLbl(OutputLabel lblTabview) {
        this.lblTabview = lblTabview;
    }*/

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

   /*public OutputLabel getLblTabview() {
        return lblTabview;
    }

    public void setLblTabview(OutputLabel lblTabview) {
        this.lblTabview = lblTabview;
    }*/

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

    public UIForm getForm() {
        return form;
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    public PanelGrid getPanelfrm() {
        return panelfrm;
    }

    public void setPanelfrm(PanelGrid panelfrm) {
        this.panelfrm = panelfrm;
    }

    public Growl getGrowl() {
        return growl;
    }

    public void setGrowl(Growl growl) {
        this.growl = growl;
    }

    public ConfirmDialog getConfirmdialog() {
        return confirmdialog;
    }

    public void setConfirmdialog(ConfirmDialog confirmdialog) {
        this.confirmdialog = confirmdialog;
    }

    public Wizard getWizard() {
        return wizard;
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public Tab getTabwizad() {
        return tabwizad;
    }

    public void setTabwizad(Tab tabwizad) {
        this.tabwizad = tabwizad;
    }

    public PanelGrid getPnlwizad() {
        return pnlwizad;
    }

    public void setPnlwizad(PanelGrid pnlwizad) {
        this.pnlwizad = pnlwizad;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public Map<Principle, List<QuestionnaireQuepQuestion>> getLstMapQuestionnaireQQ() {
        return lstMapQuestionnaireQQ;
    }

    public void setLstMapQuestionnaireQQ(Map<Principle, List<QuestionnaireQuepQuestion>> lstMapQuestionnaireQQ) {
        this.lstMapQuestionnaireQQ = lstMapQuestionnaireQQ;
    }

    public List<QuestionnaireResponse> getLstQuestionnaireResponse() {
        return lstQuestionnaireResponse;
    }

    public void setLstQuestionnaireResponse(List<QuestionnaireResponse> lstQuestionnaireResponse) {
        this.lstQuestionnaireResponse = lstQuestionnaireResponse;
    }
    
    

}
