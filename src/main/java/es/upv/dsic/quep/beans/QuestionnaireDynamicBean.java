package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
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
import java.util.TreeMap;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.inject.Inject;
import javax.inject.Named;
import org.omg.CORBA.portable.StreamableValue;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.column.Column;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.confirmdialog.ConfirmDialog;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.growl.Growl;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.overlaypanel.OverlayPanel;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.tabview.Tab;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped

public class QuestionnaireDynamicBean implements Serializable {

    private QuestioannaireDaoImplement qdi = new QuestioannaireDaoImplement();

    private List<Response> lstResponse;
    private List<QuestionnaireResponse> lstQuestionnaireResponse;
    private List<QuestionnaireResponse> lstAuxQuestionnaireResponse;
    private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ;
    

    private Map<Principle, List<QuestionnaireQuepQuestion>> lstMapQuestionnaireQQ;

    private RoleStakeholder oRoleStakeholder = null;
    private QuestionnaireResponse oQuestionnaireResponse;

    private PanelGrid panel = new PanelGrid();

    public static final  String prefixQ = "frmQ:tvQ:";
    public static final  String prefixIdRO = "idRO_";
    public static final  String prefixTxtPageNumber = "txtPageNumber_";
    public static final String prefixTxtComments = "txtComments_";

    @Inject
    private AccessBean accessBean;

    public QuestionnaireDynamicBean() {
        try {
            oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
            try {
                buildQuestionnaire();   
            } catch (Exception e) {
                addMessage("Error", "You don't have questionnaires available. Please contact Administrator or quep.framework@gmail.com ", 0);
            }            
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

    /* ------- 
     Inicialización de listas (recuperación de datos)
    - lstQuestionnaireResponse: lista que recupera el cuestionario de respuestas en caso de tenerlas
    - lstResponse: lista de respuestas asociadas al cuestionario de respuestas
    - lstQuestionnaireQQ: lista que recupera el cuestionarios de preguntas configurados para el rol actual
    - lstMapQuestionnaireQQ: Hashmap que almacena como key o clave el principio asociado a una lista de cuestionario de preguntas "lstQuestionnaireQQ"
    ---*/
    public void initList() {
        lstMapQuestionnaireQQ = new HashMap<Principle, List<QuestionnaireQuepQuestion>>();
        lstQuestionnaireQQ = new ArrayList<QuestionnaireQuepQuestion>();
        lstResponse = new ArrayList<Response>();
        lstQuestionnaireResponse = new ArrayList<QuestionnaireResponse>();

        lstQuestionnaireResponse = qdi.getQuestionnaireResponse(oRoleStakeholder.getRole().getId(), oRoleStakeholder.getStakeholder().getId());

        lstResponse = qdi.getListResponse(
                oRoleStakeholder.getRole().getId(),
                oRoleStakeholder.getStakeholder().getId(),
                oRoleStakeholder.getOrganization().getId());

        lstQuestionnaireQQ = qdi.getQuestionnairesQQRole(oRoleStakeholder.getRole().getId(), oRoleStakeholder.getOrganization().getId());
        lstMapQuestionnaireQQ = setMapQuestionnaireQQ(lstQuestionnaireQQ);
    }

    /* ------- 
    Construcción del Cuestionario para el rol logeado
    Se crean todos los componentes (botones, tabs, paneles, ...) dinámicamente según los datos recuperados en las listas
    Se llama al proceso que crea un panel por cada uno de los principios 
    ---*/
    public void buildQuestionnaire() {
        initList();
        if (lstMapQuestionnaireQQ != null) {
            panel = new PanelGrid();
            panel.setId("pnlMain");

            UIForm form = new UIForm();
            form.setId("frmQ");

            PanelGrid panelfrm = new PanelGrid();
            panelfrm.setId("pnlMain");

            Growl growl = new Growl();
            growl.setId("message");
            growl.setShowDetail(true);

            ///**************************
            //Sección de Command Button Save y Complete              
            CommandButton btnSave = new CommandButton();
            btnSave.setId("cmdSave");
            btnSave.setValue("Save");
            btnSave.setTitle("Save current response");
            btnSave.setIcon("ui-icon-disk");

            btnSave.addActionListener(new ActionListener() {
                @Override
                public void processAction(ActionEvent event) throws AbortProcessingException {
                    save(event);
                }
            });
            btnSave.setUpdate("message,frmQ");
            panelfrm.getChildren().add(btnSave);                
          
            CommandButton btnDlg = new CommandButton();
            btnDlg.setId("cmdComplete");
            btnDlg.setValue("Send");
            btnDlg.setTitle("Send final answer");
            btnDlg.setOnclick("PF('dlg').show();");
            btnDlg.setType("button");
            btnDlg.setIcon("ui-icon-mail-open");
            
            ConfirmDialog dlgCD =new ConfirmDialog();
            dlgCD.setHeader("Confirm send final reponse");
            dlgCD.setWidgetVar("dlg");
            dlgCD.setMessage("Are you Sure?");  
            //dlgCD.setGlobal(true);
            dlgCD.setShowEffect("fade");
            dlgCD.setHideEffect("fade");
            CommandButton yesComplete = new CommandButton();
            yesComplete.setValue("Yes");
            yesComplete.setIcon("ui-icon-check");
            yesComplete.setStyleClass("ui-confirmdialog-yes");
            yesComplete.setOncomplete("PF('dlg').hide();");            
            yesComplete.addActionListener(new ActionListener() {
                @Override
                public void processAction(ActionEvent event) throws AbortProcessingException {
                    completed(event);
                }
            });                        
            yesComplete.setUpdate("message"); 
            CommandButton noComplete = new CommandButton();
            noComplete.setValue("No");
            noComplete.setIcon("ui-icon-close");
            noComplete.setStyleClass("ui-confirmdialog-no");
            noComplete.setOnclick("PF('dlg').hide(); return false;"); 
                                
            dlgCD.getChildren().add(yesComplete);
            dlgCD.getChildren().add(noComplete);
            
            panelfrm.getChildren().add(btnDlg);
            panelfrm.getChildren().add(dlgCD);
            panelfrm.getChildren().add(growl);//message 
            
            form.getChildren().add(panelfrm);
                           
            ///**************************
            //Sección del panel correspondiente a la construcción de las preguntas de cuestionario
            AccordionPanel apnl = new AccordionPanel();
            apnl.setId("tvQ");
            apnl.setMultiple(true);

            for (Map.Entry<Principle, List<QuestionnaireQuepQuestion>> entry : lstMapQuestionnaireQQ.entrySet()) {
                Principle key = entry.getKey();
                List<QuestionnaireQuepQuestion> lstQQQ = entry.getValue();

                Tab tabapnl = new Tab();
                tabapnl.setId("tabapnl_" + String.valueOf(key.getId()));
                tabapnl.setTitle(key.getAbbreviation() + "." + key.getName());

                PanelGrid panelPrinciple = new PanelGrid();
                panelPrinciple.setColumns(2);
                panelPrinciple.getChildren().add(createPrinciplePanel(key, lstQQQ));

                tabapnl.getChildren().add(panelPrinciple);
                apnl.getChildren().add(tabapnl);
            }

            form.getChildren().add(apnl);
            panel.getChildren().add(form);

        }
    }

    
    public void destroyWorld() {
        addMessage("System Error", "Please try again later.",1);
    }
  
    
    public void save(ActionEvent ae) {
        try {
            saveResponse(2);
        } catch (Exception e) {
            addMessage("Error", "Please contact Administrator", 0);
        }
    }

    public void completed(ActionEvent ae) {
        try {
            if (validateComplete() == 1) {
                saveResponse(1);
            } else {
                addMessage("Error", "Please fill all requiered fields, which is marked with *.", 2);
            }
        } catch (Exception e) {
            addMessage("Error", "Please contact Administrator", 0);
        }
        //Test Agna
    }

    /* ------- 
    Se crea un panel por cada uno de los principios existentes en el hashmap lstMapQuestionnaireQQ
    a) Se llama al proceso de creación del panel de las preguntas 
    b) Se llama al proceso de creación del panel de las opciones de respuesta
    ---*/
    private PanelGrid createPrinciplePanel(Principle key, List<QuestionnaireQuepQuestion> lstQQQ) {
        PanelGrid panel = new PanelGrid();
        int j = 0;
        panel.setId("tab_" + String.valueOf(key.getId()));

        //tab.setTitle(key.getName());
        for (QuestionnaireQuepQuestion qqq : lstQQQ) {
            int idQQ = qqq.getQuepQuestion().getId();     
            List<QuepQuestionTechnique> lstTechnique =  getLstTechniques(idQQ);
            String sTechnique = getsTechniques(lstTechnique);
            String sResilience = getsResilience(lstTechnique);
            panel.getChildren().add(createQuestionPanel(qqq.getQuepQuestion(), j,sTechnique, sResilience));
            panel.getChildren().add(createResponseOptionPanel(qqq,sTechnique));
            j++;
        }
        return panel;
    }
    
    

    /* ------- 
     a) Creación del panel correspondiente a cada una de las preguntas obtenidas en el hashmap
        de questionario de preguntas según el rol
    ---*/
    private PanelGrid createQuestionPanel(QuepQuestion qq, int j, String sTechnique, String sResilience ) {
        PanelGrid pnlQuestion = new PanelGrid();
        //create dinamic component (label question)
        pnlQuestion.setId("pnlQuestion_" + String.valueOf(qq.getId()));
        pnlQuestion.setColumns(4);
        pnlQuestion.setStyleClass("panelNoBorder");
        OutputLabel lblQuestion = new OutputLabel();
        lblQuestion.setId("lblTabview" + String.valueOf(qq.getId()));
        lblQuestion.setValue(String.valueOf(j + 1) + ". " + qq.getDescription());

        OutputLabel lblMandatory = new OutputLabel();
        lblMandatory.setId("lblMandatory" + String.valueOf(qq.getId()));
        if (qq.getIsMandatory() == 1) {
            lblMandatory.setStyleClass("mandatory-astk");
            lblMandatory.setValue("*");
        } else {
            lblMandatory.setValue("");
        }
        
        pnlQuestion.getChildren().add(lblQuestion);
        pnlQuestion.getChildren().add(lblMandatory);

        //show techniques like info        
        if (!sTechnique.equals("")) {
            CommandButton btnTech = new CommandButton();
            btnTech.setId("btnTech_" + String.valueOf(qq.getId()));
            //btnTech.setValue("Tech");
            btnTech.setIcon("ui-icon-info");
            btnTech.setType("button");
            btnTech.setTitle("Best Practices");
            OverlayPanel pnlTech = new OverlayPanel();
            pnlTech.setId("pnlTech_" + String.valueOf(qq.getId()));
            pnlTech.setFor("btnTech_" + String.valueOf(qq.getId()));
            pnlTech.setHideEffect("fade");
            
            /*DataTable dtTech= new DataTable();
            dtTech.setId("dtTech_"+ String.valueOf(qq.getId())); //var="movies" value="#{movieView.movieList}" rows="5" paginator="true">
            //dtTech.setValue("#{questionnaireDynamicBean.lstTechnique}");
            //dtTech.setVar("dtTech");
            dtTech.setRows(2);
            //dtTech.setPaginator(true);
            Column clTech1 = new Column();
            clTech1.setId("cl1_dtTech_"+ String.valueOf(qq.getId()));
            clTech1.setHeaderText("Techniques");     */       
            PanelGrid pnlBP = new PanelGrid();
            pnlBP.setId("pnlBP_" + String.valueOf(qq.getId()));
            pnlBP.setColumns(1);
            OutputLabel oTech = new OutputLabel();
            oTech.setId("oTech_" + String.valueOf(qq.getId()));
            oTech.setValue("Techniques: "+sTechnique); // value="#{movies.genres}" 
            oTech.setEscape(false);
            oTech.setStyle("white-space:pre;");
            //clTech1.getChildren().add(oTech);
            
            /*Column clTech2 = new Column();
            clTech2.setId("cl2_dtTech_"+ String.valueOf(qq.getId()));
            clTech2.setHeaderText("Resilience Characteristic");     */       
            OutputLabel oTech2 = new OutputLabel();
            oTech2.setId("oTech2_" + String.valueOf(qq.getId()));
            oTech2.setValue("Resilience: "+sResilience);
            oTech2.setEscape(false);
            oTech2.setStyle("white-space:pre;");
            //clTech2.getChildren().add(oTech2);
            
            //dtTech.getChildren().add(clTech1);
            //dtTech.getChildren().add(clTech2);  
            pnlBP.getChildren().add(oTech);
            pnlBP.getChildren().add(oTech2); 
            
            pnlTech.getChildren().add(pnlBP);
            
            //pnlTech.getChildren().add(dtTech);
            pnlQuestion.getChildren().add(btnTech);
            pnlQuestion.getChildren().add(pnlTech);
        }
        return pnlQuestion;
    }

    /* ------- 
     b) Creación del panel correspondiente de las Opciones de Respuesta para cada pregunta
    ---*/
    private PanelGrid createResponseOptionPanel(QuestionnaireQuepQuestion qqq, String sTechnique) {
        PanelGrid pnlResponseOption = new PanelGrid();
        OutputLabel lblTxtComments;
        InputTextarea txtComments;

        //create dinamic component (response option)
        pnlResponseOption = new PanelGrid();
        pnlResponseOption.setId("pnlResponseOption_" + String.valueOf(qqq.getQuepQuestion().getId()));
        pnlResponseOption.setColumns(3);
        pnlResponseOption.setStyleClass("panelNoBorder");

        //static component
        OutputLabel lblResponse = new OutputLabel();
        lblResponse.setValue("Response:");
        lblResponse.setId("lblResponse_" + String.valueOf(qqq.getQuepQuestion().getId()));
        lblResponse.setFor(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));
        pnlResponseOption.getChildren().add(lblResponse);

        //response option components
        QuestionType qt = new QuestionType();
        qt = qqq.getQuepQuestion().getQuestionType();

        //get current response if there are
        List<Response> lstCurrentResponses = new ArrayList<Response>();
        if (lstResponse != null && lstResponse.size() > 0) {
            lstCurrentResponses = getCurrentResponses(qqq);
        }

        //create radio button
        if (qt.getName().toLowerCase().trim().contains("radio")) {
            SelectOneRadio rB = new SelectOneRadio();
            rB.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));

            UISelectItems selectItems = new UISelectItems();
            selectItems = setSelectItems(qqq);

            String sRBValue="";
            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                //rB.setSelectItems(setCurrentResponseOption(lstCurrentResponses));  
                sRBValue = setCurrentResponseOption(lstCurrentResponses);
                rB.setValue(sRBValue);                
            }

            rB.getChildren().add(selectItems);
            rB.setColumns(qt.getItemNumber());

            ///////--------------------------------------------------------------------------------  
                //if there Techniques 
            
            Fieldset fs = new Fieldset();
            fs.setId("fs_" + String.valueOf(qqq.getQuepQuestion().getId()));
            fs.setLegend("Technique");
            fs.setToggleable(true);
            fs.setToggleSpeed(500);
            PanelGrid pnfs = new PanelGrid();
            pnfs.setId("pnfs_" + String.valueOf(qqq.getQuepQuestion().getId()));            
            pnfs.setColumns(1);
            if (!sTechnique.equals("")) {
                OutputLabel ofs = new OutputLabel();
                ofs.setId("ofs_" + String.valueOf(qqq.getQuepQuestion().getId()));
                ofs.setValue(sTechnique);
                ofs.setStyle("white-space:pre;");
                pnfs.getChildren().add(ofs);

                OutputLabel lblfsAux = new OutputLabel();
                lblfsAux.setValue("");
                lblfsAux.setId("lblfsAux_" + String.valueOf(qqq.getQuepQuestion().getId()));
                fs.getChildren().add(pnfs);
                fs.getChildren().add(lblfsAux);
            }
            rB.setOnchange("validateQuestion(this.id,'" + prefixQ + "','" + prefixIdRO + "');");
            pnlResponseOption.getChildren().add(rB);
            pnlResponseOption.getChildren().add(fs);            
            fs.setStyle("visibility: hidden");

        } else if (qt.getName().toLowerCase().trim().contains("check")) {
            SelectManyCheckbox chk = new SelectManyCheckbox();
            chk.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));

            UISelectItems selectItems = new UISelectItems();
            selectItems = setSelectItems(qqq);

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                chk.setValue(setLstCurrentResponseOption(lstCurrentResponses));
            }

            chk.getChildren().add(selectItems);
            chk.setColumns(qt.getItemNumber());

            pnlResponseOption.getChildren().add(chk);
        } else if (qt.getName().toLowerCase().trim().contains("text")) {
            InputText txt = new InputText();
            txt.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                txt.setValue(lstCurrentResponses.get(0).getResponseOption().getName());
            }

            pnlResponseOption.getChildren().add(txt);
        } else if (qt.getName().toLowerCase().trim().contains("combo")) {
            SelectOneMenu cmb = new SelectOneMenu();
            cmb.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));

            UISelectItems selectItems = new UISelectItems();
            selectItems = setSelectItems(qqq);

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                cmb.setValue(lstCurrentResponses.get(0).getResponseOption().getId());
            }

            cmb.getChildren().add(selectItems);

            pnlResponseOption.getChildren().add(cmb);
        }
        
        ///
        if (qqq.getQuepQuestion().getHasComment() == 1) {
            lblTxtComments = new OutputLabel();
            lblTxtComments.setId("lblComments_" + String.valueOf(qqq.getQuepQuestion().getId()));
            lblTxtComments.setValue("Comments:");
            pnlResponseOption.getChildren().add(lblTxtComments);
            txtComments = new InputTextarea();
            txtComments.setAccesskey(prefixTxtComments + String.valueOf(qqq.getQuepQuestion().getId()));
            txtComments.setId(prefixTxtComments + String.valueOf(qqq.getQuepQuestion().getId()));

            ///////--------------------------------------------------------------------------------
            AjaxBehavior ajaxBehavior = (AjaxBehavior) FacesContext.getCurrentInstance().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
            ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxListener());
            ajaxBehavior.setTransient(true);
            txtComments.addClientBehavior("change", ajaxBehavior);
            /////
            //txtComments.addClientBehavior(prefixQ, behavior);

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                txtComments.setValue(lstCurrentResponses.get(0).getComment());
            }
            pnlResponseOption.getChildren().add(txtComments);
        }
        if (qqq.getQuepQuestion().getHasPageNumber() == 1) {
            OutputLabel lblTxtPageNumber = new OutputLabel();
            lblTxtPageNumber.setId("lblPageNumber_" + String.valueOf(qqq.getQuepQuestion().getId()));
            lblTxtPageNumber.setValue("Page Number:");
            pnlResponseOption.getChildren().add(lblTxtPageNumber);
            InputText txtPageNumber = new InputText();
            txtPageNumber.setId(prefixTxtPageNumber + String.valueOf(qqq.getQuepQuestion().getId()));

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                txtPageNumber.setValue(lstCurrentResponses.get(0).getPagenumber());
            }
            pnlResponseOption.getChildren().add(txtPageNumber);
        }

        return pnlResponseOption;
    }
    
    public String getsTechniques(List<QuepQuestionTechnique> lstQQTech){
        String strTechniques="";        
        for (QuepQuestionTechnique qqTechnique : lstQQTech) {
            try {
            strTechniques= strTechniques +"* "+ qqTechnique.getTechnique().getDescription() +"&#10;";           
            } catch (Exception e) {
            }
        }
        return strTechniques;
    }
    
    public String getsResilience(List<QuepQuestionTechnique> lstQQTech) {
        String sResilience = "";

        for (QuepQuestionTechnique qqTechnique : lstQQTech) {
            try {
                sResilience = sResilience + "* " + qqTechnique.getTechnique().getResilienceCharacteristic().getDescription() + "&#10;";
            } catch (Exception e) {
            }
        }    
    return sResilience ;
    }

    
     public List<QuepQuestionTechnique> getLstTechniques(int idQQ){                  
        return qdi.getQuepQuestionTechnique(idQQ);
    }

    
    public class CustomAjaxListener implements AjaxBehaviorListener {

        @Override
        public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
            SelectOneRadio rb = (SelectOneRadio) event.getComponent();
            //Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
            String srbId = rb.getId().trim().split(prefixIdRO)[1];

            if (rb.getValue().toString().equals("2")) {
                Map<String, Object> requestParamsCookie = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
                Map<String, Object> requestParamsObj = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
                Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
                String[] lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + srbId);
                String s = requestParams.get(prefixQ + prefixTxtComments + srbId)[0];

                Object oCookie = requestParamsCookie.get(prefixQ + prefixTxtComments + srbId);
                Object obj = requestParamsObj.get(prefixQ + prefixTxtComments + srbId);

                InputTextarea txtComments = new InputTextarea();
                txtComments.setId(prefixQ + prefixTxtComments + srbId);
                txtComments.setRendered(false);

            }
        }
    }

    public UISelectItems setSelectItems(QuestionnaireQuepQuestion qqq) {
        List<SelectItem> items = new ArrayList<SelectItem>();
        List<ResponseOption> lstqQRO = new ArrayList<ResponseOption>(0);
        lstqQRO = qdi.getResponseOptions(qqq.getQuepQuestion().getId());

        for (Iterator<ResponseOption> it1 = lstqQRO.iterator(); it1.hasNext();) {
            ResponseOption ro = it1.next();
            items.add(new SelectItem(ro.getId(), ro.getName()));
        }
        UISelectItems sitms = new UISelectItems();
        sitms.setValue(items);

        return sitms;
    }

    public List<Response> getCurrentResponses(QuestionnaireQuepQuestion qqq) {
        boolean b = false;
        List<Response> lResponses = new ArrayList<Response>();

        for (Response r : lstResponse) {
            if (r.getId().getIdRole() == qqq.getId().getIdRole()
                    && r.getId().getIdQuestionnaire() == qqq.getId().getIdQuestionnaire()
                    && r.getId().getIdQuepQuestion() == qqq.getId().getIdQuepQuestion()
                    && r.getId().getIdPractice() == qqq.getId().getIdPractice()) {
                lResponses.add(r);
            }
        }
        return lResponses;
    }

    public String[] setLstCurrentResponseOption(List<Response> lstCurrentResponses) {
        String[] ls = new String[lstCurrentResponses.size()];
        for (int i = 0; i < lstCurrentResponses.size(); i++) {
            String s = String.valueOf(lstCurrentResponses.get(i).getResponseOption().getId());
            ls[i] = s;
        }
        return ls;
    }

    public String setCurrentResponseOption(List<Response> lstCurrentResponses) {
        String s = String.valueOf(lstCurrentResponses.get(0).getResponseOption().getId());
        return s;
    }

    public Map<Principle, List<QuestionnaireQuepQuestion>> setMapQuestionnaireQQ(List<QuestionnaireQuepQuestion> lstQQQ) {
        List<Principle> lstPri = qdi.getPrinciples(oRoleStakeholder.getRole().getId(), oRoleStakeholder.getOrganization().getId());
        // Map<Principle, List<QuestionnaireQuepQuestion>> MapQQQ = new HashMap<Principle, List<QuestionnaireQuepQuestion>>();
        Map<Principle, List<QuestionnaireQuepQuestion>> MapQQQ = new HashMap<Principle, List<QuestionnaireQuepQuestion>>();

        for (Principle opri : lstPri) {
            List<QuestionnaireQuepQuestion> lstAuxQQQ = new ArrayList<QuestionnaireQuepQuestion>();
            for (QuestionnaireQuepQuestion qqqm : lstQQQ) {
                if (qqqm.getQuepQuestion().getPractice().getPrinciple().getId() == opri.getId()) {
                    lstAuxQQQ.add(qqqm);
                }
            }
            MapQQQ.put(opri, lstAuxQQQ);
        }
        //sort map
        Map<Principle, List<QuestionnaireQuepQuestion>> treeMap = new TreeMap<Principle, List<QuestionnaireQuepQuestion>>(MapQQQ);
        return treeMap;
    }

    public Map<QuestionnaireResponse, List<Response>> setMapResponses(List<Response> lstR, List<QuestionnaireResponse> lstQR) {
        Map<QuestionnaireResponse, List<Response>> mapR = new HashMap<QuestionnaireResponse, List<Response>>();

        for (QuestionnaireResponse oQR : lstQR) {
            List<Response> lstAuxR = new ArrayList<Response>();
            for (Response r : lstR) {
                if (r.getId().getIdRole() == oQR.getId().getIdRole()
                        && r.getId().getIdStakeholder() == oQR.getId().getIdStakeholder()
                        && r.getId().getIdQuestionnaire() == oQR.getId().getIdQuestionnaire()
                        && r.getId().getIdPractice() == oQR.getId().getIdPractice()
                        && r.getId().getIdOrganization() == oQR.getId().getIdOrganization()) {
                    lstAuxR.add(r);
                }
            }
            mapR.put(oQR, lstAuxR);
        }
        return mapR;
    }

    public int validateComplete() {
        //verificar que todas las preguntas hayan sido respondidas completas
        /* '0 None
            1 Completed
            2 InProgress
            3 Deleted';*/
        boolean band = false;
        int size = 0;
        int lessSize = 0;
        Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
            QuestionnaireQuepQuestion qqq = it.next();
            if (qqq.getQuepQuestion().getIsMandatory() == 0) {
                lessSize++;
            } else {
                String sQId = String.valueOf(qqq.getQuepQuestion().getId());
                String[] lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + sQId);
                if (lstRO != null) {
                    for (String sRO : lstRO) {
                        if (sRO != null && sRO != "") {
                            size++;
                        }
                    }
                }
            }
        }
        if (lstQuestionnaireQQ.size() - lessSize == size) {
            return 1;
        } else {
            return 2;
        }
    }

    /*
      Procedimiento que guarda las respuestas de las preguntas
     */
    public void saveResponse(int band) {       
            List<Response> lstRsp = new ArrayList<Response>();
            Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();

            lstAuxQuestionnaireResponse = new ArrayList<QuestionnaireResponse>();
            for (QuestionnaireResponse qr : lstQuestionnaireResponse) {
                lstAuxQuestionnaireResponse.add(qr);
            }
            //lstAuxQuestionnaireResponse=lstQuestionnaireResponse;

            for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
                QuestionnaireQuepQuestion qqq = it.next();

                //QUestionnaire Response            
                setObjectQuestionnaireResponse(qqq, band);

                //Response           
                String sQId = String.valueOf(qqq.getQuepQuestion().getId());
                String[] lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + sQId);
                if (lstRO != null) {
                    for (String sRO : lstRO) {
                        Response oResponse = new Response();
                        ResponseId rId = new ResponseId(oRoleStakeholder.getStakeholder().getId(),
                                qqq.getQuepQuestion().getId(),
                                qqq.getQuepQuestion().getPractice().getId(),
                                qqq.getQuestionnaire().getId().getId(),
                                oRoleStakeholder.getRole().getId(),
                                oRoleStakeholder.getOrganization().getId(),
                                Integer.parseInt(sRO));
                        oResponse.setId(rId);

                        ResponseOption ro = new ResponseOption();
                        ro = qdi.getResponseOption(qqq.getQuepQuestion().getId(), Integer.parseInt(sRO));
                        oResponse.setResponseOption(ro);
                        oResponse.setResponseOption_1(ro.getName());

                        oResponse.setQuestionnaireQuepQuestion(qqq);
                        oResponse.setStakeholder(oRoleStakeholder.getStakeholder());
                        oResponse.setIdPrinciple(qqq.getIdPrinciple());

                        if (qqq.getQuepQuestion().getHasComment() == 1) {
                            oResponse.setComment(requestParams.get(prefixQ + prefixTxtComments + sQId)[0]);
                        }

                        if (qqq.getQuepQuestion().getHasPageNumber() == 1) {
                            oResponse.setPagenumber(requestParams.get(prefixQ + prefixTxtPageNumber + sQId)[0]);
                        }

                        oResponse.setComputedValue(0);
                        oResponse.setCreationUser(oRoleStakeholder.getStakeholder().getEmail());
                        oResponse.setCreationDate(new Date());
                        oResponse.setActive(1);
                        oResponse.setAudit("I");

                        oResponse.setQuestionnaireResponse(oQuestionnaireResponse);

                        if (searchPreviousResponse(oResponse)) {
                            oResponse.setModificationUser(oRoleStakeholder.getStakeholder().getEmail());
                            oResponse.setModificationDate(new Date());
                        }

                        lstRsp.add(oResponse);
                    }
                }
            }

            Map<Integer, String> mSave = qdi.insertResponse(setMapResponses(lstRsp, lstAuxQuestionnaireResponse), setMapResponses(lstResponse, lstQuestionnaireResponse));
            if (mSave.containsKey(1)) {
                addMessage("Save", "Response have been saving.", 1);
                buildQuestionnaire();
            } else {
                addMessage("Error", "Please try again later.", 0);
            }      
    }

    public void addMessage(String summary, String detail, int band) {
        switch (band) {
            case 1: {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
                FacesContext.getCurrentInstance().addMessage(null, message);
                break;
            }
            case 0: {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
                FacesContext.getCurrentInstance().addMessage(null, message);
                break;
            }
            case 2: {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
                FacesContext.getCurrentInstance().addMessage(null, message);
                break;
            }            
            default:
                break;
        }
    }

    public boolean searchPreviousResponse(Response rsp) {
        boolean band = false;
        if (lstResponse.size() > 0) {
            for (Response r : lstResponse) {
                if (rsp.getId().getIdStakeholder() == r.getId().getIdStakeholder()
                        && rsp.getId().getIdRole() == r.getId().getIdRole()
                        && rsp.getId().getIdOrganization() == r.getId().getIdOrganization()
                        && rsp.getId().getIdPractice() == r.getId().getIdPractice()
                        && rsp.getId().getIdQuestionnaire() == r.getId().getIdQuestionnaire()
                        && rsp.getId().getIdQuepQuestion() == r.getId().getIdQuepQuestion()
                        && rsp.getId().getIdResponseOption() == r.getId().getIdResponseOption()) {
                    band = true;
                    break;
                }
            }
        }
        return band;
    }

    public boolean searchPreviousQuestionnaireResponse(QuestionnaireQuepQuestion qqq) {
        boolean band = false;
        if (lstQuestionnaireResponse.size() > 0) {
            for (QuestionnaireResponse qr : lstQuestionnaireResponse) {
                if (oRoleStakeholder.getStakeholder().getId() == qr.getId().getIdStakeholder()
                        && oRoleStakeholder.getRole().getId() == qr.getId().getIdRole()
                        && oRoleStakeholder.getOrganization().getId() == qr.getId().getIdOrganization()
                        && qqq.getQuestionnaire().getPractice().getId() == qr.getId().getIdPractice()
                        && qqq.getQuestionnaire().getId().getId() == qr.getId().getIdQuestionnaire()) {
                    lstAuxQuestionnaireResponse.remove(qr);
                    oQuestionnaireResponse = new QuestionnaireResponse();
                    oQuestionnaireResponse = qr;
                    band = true;
                    break;
                }
            }
        }
        return band;
    }

    public boolean searchAuxQuestionnaireResponse(QuestionnaireQuepQuestion qqq) {
        boolean band = false;
        if (lstAuxQuestionnaireResponse.size() > 0) {
            for (QuestionnaireResponse qr : lstAuxQuestionnaireResponse) {
                if (oRoleStakeholder.getStakeholder().getId() == qr.getId().getIdStakeholder()
                        && oRoleStakeholder.getRole().getId() == qr.getId().getIdRole()
                        && oRoleStakeholder.getOrganization().getId() == qr.getId().getIdOrganization()
                        && qqq.getQuestionnaire().getPractice().getId() == qr.getId().getIdPractice()
                        && qqq.getQuestionnaire().getId().getId() == qr.getId().getIdQuestionnaire()) {
                    lstAuxQuestionnaireResponse.remove(qr);
                    oQuestionnaireResponse = new QuestionnaireResponse();
                    oQuestionnaireResponse = qr;
                    band = true;
                    break;
                }
            }
        }
        return band;
    }

    public void setObjectQuestionnaireResponse(QuestionnaireQuepQuestion qqq, int band) {
        if (searchPreviousQuestionnaireResponse(qqq)) {
            //exista en la lista actual o auxiliar (update memoria)
            oQuestionnaireResponse.setStatus(band);
            oQuestionnaireResponse.setModificationUser(oRoleStakeholder.getStakeholder().getEmail());
            oQuestionnaireResponse.setModificationDate(new Date());
        } else if (searchAuxQuestionnaireResponse(qqq) == false) {
            oQuestionnaireResponse = new QuestionnaireResponse();
            QuestionnaireResponseId qrId = new QuestionnaireResponseId(
                    qqq.getQuestionnaire().getId().getId(),
                    qqq.getQuepQuestion().getPractice().getId(),
                    oRoleStakeholder.getStakeholder().getId(), oRoleStakeholder.getRole().getId(),
                    oRoleStakeholder.getOrganization().getId());

            oQuestionnaireResponse.setId(qrId);
            oQuestionnaireResponse.setOrganization(oRoleStakeholder.getOrganization());
            oQuestionnaireResponse.setQuestionnaire(lstQuestionnaireQQ.get(0).getQuestionnaire());
            oQuestionnaireResponse.setCreationUser(oRoleStakeholder.getStakeholder().getEmail());
            oQuestionnaireResponse.setCreationDate(new Date());
            oQuestionnaireResponse.setActive(1);
            oQuestionnaireResponse.setAudit("I");
            oQuestionnaireResponse.setIdPrinciple(lstQuestionnaireQQ.get(0).getIdPrinciple());
            oQuestionnaireResponse.setComputedValue(0);
            oQuestionnaireResponse.setStatus(0);
        }
        lstAuxQuestionnaireResponse.add(oQuestionnaireResponse);
    }

    public List<QuestionnaireResponse> getLstAuxQuestionnaireResponse() {
        return lstAuxQuestionnaireResponse;
    }

    public void setLstAuxQuestionnaireResponse(List<QuestionnaireResponse> lstAuxQuestionnaireResponse) {
        this.lstAuxQuestionnaireResponse = lstAuxQuestionnaireResponse;
    }

    public QuestionnaireResponse getoQuestionnaireResponse() {
        return oQuestionnaireResponse;
    }

    public void setoQuestionnaireResponse(QuestionnaireResponse oQuestionnaireResponse) {
        this.oQuestionnaireResponse = oQuestionnaireResponse;
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

    public QuestioannaireDaoImplement getQdi() {
        return qdi;
    }

    public void setQdi(QuestioannaireDaoImplement qdi) {
        this.qdi = qdi;
    }

    public Map<Principle, List<QuestionnaireQuepQuestion>> getLstMapQuestionnaireQQ() {
        return lstMapQuestionnaireQQ;
    }

    public void setLstMapQuestionnaireQQ(Map<Principle, List<QuestionnaireQuepQuestion>> lstMapQuestionnaireQQ) {
        this.lstMapQuestionnaireQQ = lstMapQuestionnaireQQ;
    }

    public List<Response> getLstResponse() {
        return lstResponse;
    }

    public void setLstResponse(List<Response> lstResponse) {
        this.lstResponse = lstResponse;
    }

    public PanelGrid getPanel() {
        return panel;
    }

    public void setPanel(PanelGrid panel) {
        this.panel = panel;
    }

    public List<QuestionnaireResponse> getLstQuestionnaireResponse() {
        return lstQuestionnaireResponse;
    }

    public void setLstQuestionnaireResponse(List<QuestionnaireResponse> lstQuestionnaireResponse) {
        this.lstQuestionnaireResponse = lstQuestionnaireResponse;
    }

}
