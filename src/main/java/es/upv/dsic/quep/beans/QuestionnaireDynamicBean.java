package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.PracticeDaoImplement;
import es.upv.dsic.quep.dao.QuestioannaireDaoImplement;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Practice;
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
import java.io.IOException;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.confirmdialog.ConfirmDialog;
import org.primefaces.component.fieldset.Fieldset;
import org.primefaces.component.growl.Growl;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.overlaypanel.OverlayPanel;
import org.primefaces.component.panel.Panel;
import org.primefaces.component.panelgrid.PanelGrid;

import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.component.separator.Separator;
import org.primefaces.component.separator.UISeparator;
import org.primefaces.component.spacer.Spacer;
import org.primefaces.component.tabview.Tab;

/**
 *
 * @author agna8685
 */
@Named
//@RequestScoped
@SessionScoped

public class QuestionnaireDynamicBean implements Serializable {

    @Inject
    private OrganizationBean organizationBean;

    private QuestioannaireDaoImplement qdi = new QuestioannaireDaoImplement();

    private List<Response> lstResponse;
    
    private List<QuestionnaireResponse> lstQuestionnaireResponse;
    private List<QuestionnaireResponse> lstAuxQuestionnaireResponse;
    private List<QuestionnaireQuepQuestion> lstQuestionnaireQQ;

    private Map<Principle, List<QuestionnaireQuepQuestion>> lstMapQuestionnaireQQ;

    private Organization oOrganization = null;
    private QuestionnaireResponse oQuestionnaireResponse;

    private PanelGrid panel = new PanelGrid();

    public static final String prefixQ = "frmQ:tvQ:";
    public static final String prefixIdRO = "idRO_";
    public static final String prefixTxtPageNumber = "txtPageNumber_";
    public static final String prefixTxtComments = "txtComments_";

    @Inject
    private LoginBean loginBean;
    
    private boolean bSessionQuestionnaire=false;        

    public QuestionnaireDynamicBean() {

    }

    @PostConstruct
    public void init() {
        try {
            oOrganization = organizationBean.getOrganization();
            try {
                buildQuestionnaire();
                bSessionQuestionnaire=true;
            } catch (Exception e) {
                addMessage("Error", "You don't have questionnaires available. Please contact Administrator or quep.framework@gmail.com ", 0);
            }
        } catch (Exception e) {
            System.out.printf(e.getMessage());
        }
    }

    /* ------- 
     InicializaciÛn de listas (recuperaciÛn de datos)
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

        lstQuestionnaireResponse = qdi.getQuestionnaireResponse(loginBean.getRole().getId(), loginBean.getStakeholder().getId(), oOrganization.getId());

        lstResponse = qdi.getListResponse(
                loginBean.getRole().getId(),
                loginBean.getStakeholder().getId(),
                oOrganization.getId());

        //Currently: Questionnaire is set up to all organization. 
        //Todos los cuestionarios se encuentra configurados para evaluar a cualquier organizacion
        lstQuestionnaireQQ = qdi.getQuestionnairesQQbyRole(loginBean.getRole().getId());
        lstMapQuestionnaireQQ = setMapQuestionnaireQQ(lstQuestionnaireQQ);
    }

    /* ------- 
    ConstrucciÛn del Cuestionario para el rol logeado
    Se crean todos los componentes (botones, tabs, paneles, ...) din·micamente seg˙n los datos recuperados en las listas
    Se llama al proceso que crea un panel por cada uno de los principios 
    ---*/
    UIForm form;

    public UIForm getForm() {
        return form;
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    public void buildQuestionnaire() {
        initList();
        if (lstMapQuestionnaireQQ != null) {
            panel = new PanelGrid();
            //panel.setId("panel");

            form = new UIForm();
            form.setId("frmQ");

            PanelGrid panelfrm = new PanelGrid();
            panelfrm.setId("pnlMain");

            Growl growl = new Growl();
            growl.setId("message");
            growl.setShowDetail(true);

            ///**************************
            //SecciÛn de Command Button Save y Complete              
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

            ConfirmDialog dlgCD = new ConfirmDialog();
            dlgCD.setHeader("Confirm send final reponse");
            dlgCD.setWidgetVar("dlg");
            dlgCD.setMessage("Are you Sure?");
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

            //** desabilitar en caso que el cuestionario se haya enviado o este completo
            if (lstQuestionnaireResponse.size() > 0) {
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    btnDlg.setDisabled(true);
                    btnSave.setDisabled(true);
                }
            }

            form.getChildren().add(panelfrm);

            ///**************************
            //SecciÛn del panel correspondiente a la construcciÛn de las preguntas de cuestionario
            AccordionPanel apnl = new AccordionPanel();
            apnl.setId("tvQ");
            apnl.setMultiple(true);
            apnl.setStyleClass("panelNoBorder");
            apnl.setStyle("width:1024px;");
            for (Map.Entry<Principle, List<QuestionnaireQuepQuestion>> entry : lstMapQuestionnaireQQ.entrySet()) {
                Principle oPrinciple = entry.getKey();
                List<QuestionnaireQuepQuestion> lstQQQ = entry.getValue();

                //Creacion de panel agrupado por Principios
                Tab tabapnl = new Tab();
                tabapnl.setId("tabapnl_" + String.valueOf(oPrinciple.getId()));
                tabapnl.setTitle(oPrinciple.getAbbreviation() + "." + oPrinciple.getName());

                PanelGrid panelPrinciple = new PanelGrid();
                panelPrinciple.setColumns(2);
                panelPrinciple.getChildren().add(createPrinciplePanel(oPrinciple, lstQQQ));
                panelPrinciple.setStyle("width:1024px;");

                tabapnl.getChildren().add(panelPrinciple);
                apnl.getChildren().add(tabapnl);
            }

            form.getChildren().add(apnl);
            panel.getChildren().add(form);

        }
    }

    public void destroyWorld() {
        addMessage("System Error", "Please try again later.", 1);
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
    }

    /* ------- 
    Se crea un panel por cada uno de los principios existentes en el hashmap lstMapQuestionnaireQQ
    a) Se llama al proceso de creaciÛn del panel de las preguntas 
    b) Se llama al proceso de creaciÛn del panel de las opciones de respuesta
    ---*/
    
    private PanelGrid createPrinciplePanel(Principle key, List<QuestionnaireQuepQuestion> lstQQQ) {
        PanelGrid panel = new PanelGrid();
        int j = 0;
        //int i =0;
        panel.setId("tab_" + String.valueOf(key.getId()));

        //group by practice 
         List<Practice> lstPractice= new ArrayList<Practice>();
         PracticeDaoImplement pdi= new PracticeDaoImplement();
         lstPractice = pdi.getPractice();
         
         for (Practice op : lstPractice) {
             OutputLabel olGP= new OutputLabel();              
             PanelGrid panelQQQ = new PanelGrid();
             j=0;             
             for (QuestionnaireQuepQuestion qqq : lstQQQ) {
                 if (op.getId() == qqq.getQuepQuestion().getPractice().getId()) {
                     if (j == 0) {
                         /*OutputLabel olSpaceGP = new OutputLabel();
                         olSpaceGP.setValue("Blank Space");
                         olSpaceGP.setStyle("color:white;");
                         panel.getChildren().add(olSpaceGP);*/
                         olGP.setValue("<p><br/> Practice: " + op.getName()+"</p>");
                         olGP.setStyle("color: #638493;  font-weight: bold;");
                         olGP.setEscape(false);
                         panel.getChildren().add(olGP);
                         UISeparator UIsp = new UISeparator();                          
                         panel.getChildren().add(UIsp);
                     }
                     int idQQ = qqq.getQuepQuestion().getId();
                     List<QuepQuestionTechnique> lstTechnique = getLstTechniques(idQQ);
                     String[] sTechnique = getSTechniques(lstTechnique);
                     panelQQQ.getChildren().add(createQuestionPanel(qqq.getQuepQuestion(), j, getSTechniquesTxt(lstTechnique), getsResilienceTxt(lstTechnique)));
                     panelQQQ.getChildren().add(createResponseOptionPanel(qqq, sTechnique));
                     j++;
                     //i++;
                 }
             }
             panel.getChildren().add(panelQQQ);
        }
        return panel;
    }
    
    /* ------- 
     a) CreaciÛn del panel correspondiente a cada una de las preguntas obtenidas en el hashmap
        de questionario de preguntas seg˙n el rol
    ---*/
    private PanelGrid createQuestionPanel(QuepQuestion qq, int j, String sTechnique, String sResilience) {
        PanelGrid pnlQuestion = new PanelGrid();
        //create dinamic component (label question)
        pnlQuestion.setId("pnlQuestion_" + String.valueOf(qq.getId()));
        pnlQuestion.setColumns(4);
        pnlQuestion.setStyleClass("panelNoBorder");
        OutputLabel lblQuestion = new OutputLabel();
        lblQuestion.setId("lblTabview" + String.valueOf(qq.getId()));
        lblQuestion.setValue(String.valueOf(j + 1) + ". " + qq.getDescription());
        lblQuestion.setStyle("display: block; width: 800px;"); ////////////////*****************----------
        OutputLabel lblMandatory = new OutputLabel();
        lblMandatory.setId("lblMandatory" + String.valueOf(qq.getId()));
        if (qq.getIsMandatory() == 1) {
            lblMandatory.setStyleClass("mandatory-astk");
            lblMandatory.setValue("*");
        } else {
            lblMandatory.setValue("");
        }
        lblMandatory.setStyle("width: 50px");
        pnlQuestion.getChildren().add(lblQuestion);
        pnlQuestion.getChildren().add(lblMandatory);

        //show techniques like info        
        if (!sTechnique.equals("")) {
            CommandButton btnTech = new CommandButton();
            btnTech.setId("btnTech_" + String.valueOf(qq.getId()));
            btnTech.setIcon("ui-icon-info");
            btnTech.setType("button");
            btnTech.setTitle("Best Practices");
            OverlayPanel pnlTech = new OverlayPanel();
            pnlTech.setId("pnlTech_" + String.valueOf(qq.getId()));
            pnlTech.setFor("btnTech_" + String.valueOf(qq.getId()));
            pnlTech.setHideEffect("fade");
            //pnlTech.setStyle("height:300px;");      

            PanelGrid pnlBP = new PanelGrid();
            pnlBP.setId("pnlBP_" + String.valueOf(qq.getId()));
            pnlBP.setColumns(1);
            pnlBP.setStyle(" width:600px; float:right;");

            OutputLabel oTech = new OutputLabel();
            // InputText oTech = new InputText();
            oTech.setId("oTech_" + String.valueOf(qq.getId()));
            oTech.setValue("<p>Techniques: <br/>" + sTechnique + "</p>");
            oTech.setEscape(false);
            //oTech.setStyle("white-space:pre; display:block; width:200px; height:50px; text-align:justify;");  +
            oTech.setStyle("width:600px;");
            pnlBP.getChildren().add(oTech);

            if (!sResilience.equals("")) {
                OutputLabel oTech2 = new OutputLabel();
                //InputText oTech2 = new InputText();
                oTech2.setId("oTech2_" + String.valueOf(qq.getId()));
                oTech2.setValue("<p>Resilience: <br/>" + sResilience + "</p>");
                oTech2.setEscape(false);
                pnlBP.getChildren().add(oTech2);
            }
            //oTech2.setStyle("white-space:pre; ");

            pnlTech.getChildren().add(pnlBP);

            pnlQuestion.getChildren().add(btnTech);
            pnlQuestion.getChildren().add(pnlTech);
        }
        return pnlQuestion;
    }

    /* ------- 
     b) CreaciÛn del panel correspondiente de las Opciones de Respuesta para cada pregunta
    ---*/
    private PanelGrid createResponseOptionPanel(QuestionnaireQuepQuestion qqq, String[] sTechnique) {
        boolean bandShowToggleTechnique = false;
        boolean bandHiddenPageNumber = false;
        PanelGrid pnlResponseOption = new PanelGrid();

        OutputLabel lblTxtComments;
        InputTextarea txtComments;

        //create dinamic component (response option)         
        pnlResponseOption = new PanelGrid();
        pnlResponseOption.setId("pnlResponseOption_" + String.valueOf(qqq.getQuepQuestion().getId()));
        pnlResponseOption.setColumns(3);

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

        ////////////////////////////////////////
        //Create Radio Button - Response Option
        if (qt.getName().toLowerCase().trim().contains("radio")) {
            bandShowToggleTechnique = true;
            SelectOneRadio rB = new SelectOneRadio();
            rB.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));
            UISelectItems selectItems = new UISelectItems();
            selectItems = setSelectItems(qqq);
            String sRBValue = "";
            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                //rB.setSelectItems(setCurrentResponseOption(lstCurrentResponses));  
                sRBValue = setCurrentResponseOption(lstCurrentResponses);
                rB.setValue(sRBValue);
                
                 //** band to hidden the page number
                if (Integer.parseInt(sRBValue) == 2){
                    bandHiddenPageNumber = true;
                }
            }
            rB.getChildren().add(selectItems);
            rB.setColumns(5);
            rB.setStyle("display: block; width: 400px;");

            //** desabilitar en caso que el cuestionario se haya enviado o este completo
            if (lstQuestionnaireResponse.size() > 0) {
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    rB.setDisabled(true);
                }
            }
            rB.setOnchange("validateQuestion(this.id,'" + prefixQ + "','" + prefixIdRO + "');");
            pnlResponseOption.getChildren().add(rB);

            //if there are Techniques   
            if (sTechnique.length > 0) {
                Fieldset fs = new Fieldset();
                fs.setId("fs_" + String.valueOf(qqq.getQuepQuestion().getId()));
                fs.setLegend("Technique");
                fs.setToggleable(true);
                fs.setCollapsed(true);
                fs.setToggleSpeed(500);
                PanelGrid pnfs = new PanelGrid();
                pnfs.setId("pnfs_" + String.valueOf(qqq.getQuepQuestion().getId()));
                pnfs.setColumns(1);
                //if (sTechnique.length > 0) {
                for (int i = 0; i < sTechnique.length; i++) {
                    HtmlOutputLabel ol = new HtmlOutputLabel();
                    ol.setId("ofs_" + String.valueOf(qqq.getQuepQuestion().getId()) + String.valueOf(i));
                    ol.setValue(sTechnique[i] + "</br>");
                    ol.setStyle("white-space:pre-line;width:300px;");
                    ol.setEscape(false);
                    fs.getChildren().add(ol);
                }
                fs.getChildren().add(pnfs);
                fs.setStyle("visibility: hidden; ");
                fs.setStyleClass("panelNoBorder");
                pnlResponseOption.getChildren().add(fs);
            }
            ////////////////////////////////////////    
            //Create Check List - Response Option
        } else if (qt.getName().toLowerCase().trim().contains("check")) {            
            SelectManyCheckbox chk = new SelectManyCheckbox();
            chk.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));
            chk.setLayout("grid");
            chk.setColumns(5);
            UISelectItems selectItems = new UISelectItems();
            selectItems = setSelectItems(qqq);
            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                chk.setValue(setLstCurrentResponseOption(lstCurrentResponses));
            }
            chk.getChildren().add(selectItems);            
            //chk.set
            //chk.setStyle("display: block; width: 400px;");
            
            //** desabilitar en caso que el cuestionario se haya enviado o este completo
            if (lstQuestionnaireResponse.size() > 0) {
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    chk.setDisabled(true);
                }
            }
            
            pnlResponseOption.getChildren().add(chk);

            ////////////////////////////////////////
            //Create Text - Response Option
        } else if (qt.getName().toLowerCase().trim().contains("text")) {
            InputText txt = new InputText();
            txt.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));
            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                txt.setValue(lstCurrentResponses.get(0).getResponseOption_1());
            }
            //** desabilitar en caso que el cuestionario se haya enviado o este completo
            if (lstQuestionnaireResponse.size() > 0) {
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    txt.setDisabled(true);
                }
            }
            pnlResponseOption.getChildren().add(txt);

            ////////////////////////////////////////
            //Create Combo Box - Response Option
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

            ////////////////////////////////////////
            //Create Calendar (date) - Response Option
        } else if (qt.getName().toLowerCase().trim().contains("date")) {
            Calendar cldr = new Calendar();
            cldr.setId(prefixIdRO + String.valueOf(qqq.getQuepQuestion().getId()));

            cldr.setShowOn("button");

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                cldr.setPattern("dd/MM/yyyy");
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String dateInString = lstCurrentResponses.get(0).getResponseOption_1();
                    Date date = formatter.parse(dateInString);
                    cldr.setValue(date);
                } catch (ParseException e) {
                }

                //** desabilitar en caso que el cuestionario se haya enviado o este completo
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    cldr.setDisabled(true);
                }
                /*else {
                    
                }*/
            }
            pnlResponseOption.getChildren().add(cldr);
        }

        //Verify if show techniques when is not a "radio button" response option
        if (!bandShowToggleTechnique || !(sTechnique.length > 0)) {
            OutputLabel lblAuxWoToggleTq = new OutputLabel();
            lblAuxWoToggleTq.setId("lblAuxWoToggleTq_" + String.valueOf(qqq.getQuepQuestion().getId()));
            lblAuxWoToggleTq.setValue("");
            pnlResponseOption.getChildren().add(lblAuxWoToggleTq);
        }

        ////////////////////////////////////////
        //Verify if show comments 
        if (qqq.getQuepQuestion().getHasComment() == 1) {
            lblTxtComments = new OutputLabel();
            lblTxtComments.setId("lblComments_" + String.valueOf(qqq.getQuepQuestion().getId()));
            lblTxtComments.setValue("Comments:");

            pnlResponseOption.getChildren().add(lblTxtComments);
            txtComments = new InputTextarea();
            txtComments.setAccesskey(prefixTxtComments + String.valueOf(qqq.getQuepQuestion().getId()));
            txtComments.setId(prefixTxtComments + String.valueOf(qqq.getQuepQuestion().getId()));
            txtComments.setStyle("width: 500px;");
            txtComments.setAutoResize(false);
            txtComments.setMaxlength(15000);

            //----
            AjaxBehavior ajaxBehavior = (AjaxBehavior) FacesContext.getCurrentInstance().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
            ajaxBehavior.addAjaxBehaviorListener(new CustomAjaxListener());
            ajaxBehavior.setTransient(true);
            txtComments.addClientBehavior("change", ajaxBehavior);

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                txtComments.setValue(lstCurrentResponses.get(0).getComment());
            }
            OutputLabel lblCmmAux = new OutputLabel();
            lblCmmAux.setValue("");
            lblCmmAux.setId("lblCmmAux_" + String.valueOf(qqq.getQuepQuestion().getId()));
            pnlResponseOption.getChildren().add(txtComments);
            pnlResponseOption.getChildren().add(lblCmmAux);

            //** desabilitar en caso que el cuestionario se haya enviado o este completo
            if (lstQuestionnaireResponse.size() > 0) {
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    txtComments.setDisabled(true);
                }
            }
        }

        ////////////////////////////////////////
        //Verify if show Page Number 
        if (qqq.getQuepQuestion().getHasPageNumber() == 1) {
            OutputLabel lblTxtPageNumber = new OutputLabel();
            lblTxtPageNumber.setId("lblPageNumber_" + String.valueOf(qqq.getQuepQuestion().getId()));
            lblTxtPageNumber.setValue("Page Number:");

            pnlResponseOption.getChildren().add(lblTxtPageNumber);
            InputText txtPageNumber = new InputText();
            txtPageNumber.setId(prefixTxtPageNumber + String.valueOf(qqq.getQuepQuestion().getId()));
            //txtPageNumber.setStyle("width: 50px;");
            // txtPageNumber.setAccesskey("validateQuestion(this.id,'" + prefixQ + "','" + prefixIdRO + "');");

            if (lstCurrentResponses != null && lstCurrentResponses.size() > 0) {
                txtPageNumber.setValue(lstCurrentResponses.get(0).getPagenumber());
            }
            OutputLabel lblNmPAux = new OutputLabel();
            lblNmPAux.setValue("");
            lblNmPAux.setId("lblNmPAux_" + String.valueOf(qqq.getQuepQuestion().getId()));
            pnlResponseOption.getChildren().add(txtPageNumber);
            pnlResponseOption.getChildren().add(lblNmPAux);

            //** hidden page number   
            if (bandHiddenPageNumber) {
                txtPageNumber.setStyle("visibility: hidden; ");//.setRendered(false);               
                lblTxtPageNumber.setStyle("visibility: hidden; ");               
                txtPageNumber.setValue("");
            }

            //** desabilitar en caso que el cuestionario se haya enviado o este completo
            if (lstQuestionnaireResponse.size() > 0) {
                if (lstQuestionnaireResponse.get(0).getStatus() == 1) {
                    txtPageNumber.setDisabled(true);
                }
            }
        }
        return pnlResponseOption;
    }

    public String[] getSTechniques(List<QuepQuestionTechnique> lstQQTech) {
        String[] strTechniques = new String[lstQQTech.size()];
        int i = 0;
        for (QuepQuestionTechnique qqTechnique : lstQQTech) {
            try {
                strTechniques[i] = "* " + qqTechnique.getTechnique().getDescription();
                i++;
            } catch (Exception e) {
            }
        }
        return strTechniques;
    }

    public String getSTechniquesTxt(List<QuepQuestionTechnique> lstQQTech) {
        String strTechniques = "";
        for (QuepQuestionTechnique qqTechnique : lstQQTech) {
            try {
                strTechniques = strTechniques + "* " + qqTechnique.getTechnique().getDescription() + "<br/SW>";
            } catch (Exception e) {
            }
        }
        return strTechniques;
    }

    public String getsResilienceTxt(List<QuepQuestionTechnique> lstQQTech) {
        String sResilience = "";
        for (QuepQuestionTechnique qqTechnique : lstQQTech) {
            try {
                sResilience = sResilience + "* " + qqTechnique.getTechnique().getResilienceCharacteristic().getDescription() + "<br/>";
            } catch (Exception e) {
            }
        }
        return sResilience;
    }

    public String[] getSResilience(List<QuepQuestionTechnique> lstQQTech) {
        String[] sResilience = new String[lstQQTech.size()];
        int i = 0;
        for (QuepQuestionTechnique qqTechnique : lstQQTech) {
            try {
                sResilience[i] = "* " + qqTechnique.getTechnique().getResilienceCharacteristic().getDescription();
                i++;
            } catch (Exception e) {
            }
        }
        return sResilience;
    }

    public List<QuepQuestionTechnique> getLstTechniques(int idQQ) {
        return qdi.getQuepQuestionTechnique(idQQ);
    }

    public class CustomAjaxListener implements AjaxBehaviorListener {

        @Override
        public void processAjaxBehavior(AjaxBehaviorEvent event) throws AbortProcessingException {
            SelectOneRadio rb = (SelectOneRadio) event.getComponent();
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
        List<Principle> lstPri = qdi.getPrinciples(loginBean.getRole().getId(), oOrganization.getId());
        Map<Principle, List<QuestionnaireQuepQuestion>> MapQQQ = new HashMap<Principle, List<QuestionnaireQuepQuestion>>();

        for (Principle opri : lstPri) {
            List<QuestionnaireQuepQuestion> lstAuxQQQ = new ArrayList<QuestionnaireQuepQuestion>();
            for (QuestionnaireQuepQuestion qqqm : lstQQQ) {
                if (qqqm.getIdPrinciple() == opri.getId()) {
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
        //boolean band = false;
        int size = 0;
        int lessSize = 0;
        Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {            
            QuestionnaireQuepQuestion qqq = it.next();           
            
            String sQId = String.valueOf(qqq.getQuepQuestion().getId());
            String[] lstRO = null;
            if (qqq.getQuepQuestion().getQuestionType().getName().equals("date")) {
                lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + sQId + "_input"); // recupero el valor del componente en una lista y para obtenerlo paso como parametro el id del pregunta                                       
            } else {
                lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + sQId);//recupero los ids de los response option de una pregunta
            }

            if (lstRO != null) {
                for (String sRO : lstRO) {
                    if (qqq.getQuepQuestion().getIsMandatory() == 0 ) {
                        lessSize++;                        
                        //break;
                    } else if (sRO != null && !sRO.equals("")) {
                        size++;
                    }
                    break;
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
    public void saveResponse(int band) throws IOException {
        List<Response> lstRsp = new ArrayList<Response>();
        Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();

        lstAuxQuestionnaireResponse = new ArrayList<QuestionnaireResponse>();
        for (QuestionnaireResponse qr : lstQuestionnaireResponse) {
            lstAuxQuestionnaireResponse.add(qr);
        }

        for (Iterator<QuestionnaireQuepQuestion> it = lstQuestionnaireQQ.iterator(); it.hasNext();) {
            QuestionnaireQuepQuestion qqq = it.next();
            //QUestionnaire Response            
            setObjectQuestionnaireResponse(qqq, band);

            //Response   
            String sQId = String.valueOf(qqq.getQuepQuestion().getId());
            String[] lstRO = null;
            if (qqq.getQuepQuestion().getQuestionType().getName().equals("date")) {
                lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + sQId + "_input"); // recupero el valor del componente en una lista y para obtenerlo paso como parametro el id del pregunta                                       
            } else {
                lstRO = (String[]) requestParams.get(prefixQ + prefixIdRO + sQId);
            }

            if (lstRO != null) {
                for (String sRO : lstRO) {
                    Response oResponse = new Response();
                    List<ResponseOption> lstIdsRO = new ArrayList<ResponseOption>();
                    lstIdsRO = qdi.getResponseOptions(qqq.getQuepQuestion().getId());

                    ResponseOption ro = new ResponseOption();
                    ResponseId rId = null;
                    if (lstIdsRO.size() > 1) {
                        rId = new ResponseId(loginBean.getStakeholder().getId(),
                                qqq.getQuepQuestion().getId(),
                                qqq.getQuepQuestion().getPractice().getId(),
                                qqq.getQuestionnaire().getId().getId(),
                                loginBean.getRole().getId(),
                                oOrganization.getId(),
                                Integer.parseInt(sRO));
                        ro = qdi.getResponseOption(qqq.getQuepQuestion().getId(), Integer.parseInt(sRO));
                    } else {
                        //validar si tiene una opcion de respuesta para calendar or text
                        rId = new ResponseId(loginBean.getStakeholder().getId(),
                                qqq.getQuepQuestion().getId(),
                                qqq.getQuepQuestion().getPractice().getId(),
                                qqq.getQuestionnaire().getId().getId(),
                                loginBean.getRole().getId(),
                                oOrganization.getId(),
                                lstIdsRO.get(0).getId());
                        ro = qdi.getResponseOption(qqq.getQuepQuestion().getId(), lstIdsRO.get(0).getId());
                    }
                    oResponse.setId(rId);
                    oResponse.setResponseOption(ro);

                    if (lstIdsRO.size() > 1) {
                        oResponse.setResponseOption_1(ro.getName()); //string del valor ingresado en la respuesta
                    } else {
                        oResponse.setResponseOption_1(sRO);
                    }

                    oResponse.setQuestionnaireQuepQuestion(qqq);
                    oResponse.setStakeholder(loginBean.getStakeholder());
                    oResponse.setIdPrinciple(qqq.getIdPrinciple());

                    if (qqq.getQuepQuestion().getHasComment() == 1) {
                        oResponse.setComment(requestParams.get(prefixQ + prefixTxtComments + sQId)[0]);
                    }

                    if (qqq.getQuepQuestion().getHasPageNumber() == 1) {
                        oResponse.setPagenumber(requestParams.get(prefixQ + prefixTxtPageNumber + sQId)[0]);
                    }

                    oResponse.setComputedValue(0);
                    oResponse.setCreationUser(loginBean.getStakeholder().getEmail());
                    oResponse.setCreationDate(new Date());
                    oResponse.setActive(1);
                    oResponse.setAudit("I");

                    oResponse.setQuestionnaireResponse(oQuestionnaireResponse);

                    if (searchPreviousResponse(oResponse)) {
                        oResponse.setModificationUser(loginBean.getStakeholder().getEmail());
                        oResponse.setModificationDate(new Date());
                    }

                    lstRsp.add(oResponse);
                }
            }
        }

        Map<Integer, String> mSave = qdi.insertResponse(setMapResponses(lstRsp, lstAuxQuestionnaireResponse), setMapResponses(lstResponse, lstQuestionnaireResponse));
        if (mSave.containsKey(1)) {
            addMessage("Save", "Response have been saving.", 1);

            if (band == 1) { //is complete button final send
                sendEmail();
                buildQuestionnaire();
                //RequestContext.getCurrentInstance().update("@this");                
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
                ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
            }
            //new QuestionnaireDynamicBean();
        } else {
            addMessage("Error", "Please try again later.", 0);
        }
    }
    
    public void sendEmail(){
        emailJSFManagedBean email= new emailJSFManagedBean();
        email.setTo("");
        email.setFrom("quep.framework@gmail.com");
        email.setUsername("quep.framework@gmail.com");
        email.setPassword("QuEP2016!");
        email.setSmtp("smtp.gmail.com");
        email.setPort(587);        
        email.setSubject("Cuestionario enviado <<Marco QuEP>>");
        email.setDescr("Gracias por contestar al cuestionario. Su colaboración es muy importante para mejorar la gestión de planes de emergencia en organizaciones.");        
        
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
                if (loginBean.getStakeholder().getId() == qr.getId().getIdStakeholder()
                        && loginBean.getRole().getId() == qr.getId().getIdRole()
                        && oOrganization.getId() == qr.getId().getIdOrganization()
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
                if (loginBean.getStakeholder().getId() == qr.getId().getIdStakeholder()
                        && loginBean.getRole().getId() == qr.getId().getIdRole()
                        && oOrganization.getId() == qr.getId().getIdOrganization()
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
            oQuestionnaireResponse.setModificationUser(loginBean.getStakeholder().getEmail());
            oQuestionnaireResponse.setModificationDate(new Date());
        } else if (searchAuxQuestionnaireResponse(qqq) == false) {
            oQuestionnaireResponse = new QuestionnaireResponse();
            QuestionnaireResponseId qrId = new QuestionnaireResponseId(
                    qqq.getQuestionnaire().getId().getId(),
                    qqq.getQuepQuestion().getPractice().getId(),
                    loginBean.getStakeholder().getId(), loginBean.getRole().getId(),
                    oOrganization.getId());

            oQuestionnaireResponse.setId(qrId);
            oQuestionnaireResponse.setOrganization(oOrganization);
            oQuestionnaireResponse.setQuestionnaire(lstQuestionnaireQQ.get(0).getQuestionnaire());
            oQuestionnaireResponse.setCreationUser(loginBean.getStakeholder().getEmail());
            oQuestionnaireResponse.setCreationDate(new Date());
            oQuestionnaireResponse.setActive(1);
            oQuestionnaireResponse.setAudit("I");
            oQuestionnaireResponse.setIdPrinciple(lstQuestionnaireQQ.get(0).getIdPrinciple());
            oQuestionnaireResponse.setComputedValue(0);
            oQuestionnaireResponse.setStatus(band);
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
        buildQuestionnaire();
        this.panel = panel;
    }

    public List<QuestionnaireResponse> getLstQuestionnaireResponse() {
        return lstQuestionnaireResponse;
    }

    public void setLstQuestionnaireResponse(List<QuestionnaireResponse> lstQuestionnaireResponse) {
        this.lstQuestionnaireResponse = lstQuestionnaireResponse;
    }

    public Organization getoOrganization() {
        return oOrganization;
    }

    public void setoOrganization(Organization oOrganization) {
        this.oOrganization = oOrganization;
    }

    public boolean isbSessionQuestionnaire() {
        return bSessionQuestionnaire;
    }

    public void setbSessionQuestionnaire(boolean bSessionQuestionnaire) {
        this.bSessionQuestionnaire = bSessionQuestionnaire;
    }
    

    
}
