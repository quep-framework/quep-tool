/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.converters.MaturityLevelComparable;
import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
import es.upv.dsic.quep.dao.PracticeDaoImplement;
import es.upv.dsic.quep.dao.PrincipleDaoImplement;
import es.upv.dsic.quep.dao.ResultsDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.MaturityLevelPractice;
import es.upv.dsic.quep.model.Practice;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.awt.MenuItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.inject.Inject;
import javax.inject.Named;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHint;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.MethodExpressionActionListener;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.context.RequestContext;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped
public class ResultsChartViewBean implements Serializable {

    @Inject
    private AccessBean accessBean;
    private RoleStakeholder oRoleStakeholder = null;

    private HorizontalBarChartModel horizontalBarModel;
    private Map<MaturityLevel, Result> mapMaturityLevelResults;
    private List<MaturityLevel> lstMaturityLevel;

    private Map<Practice, ResponseEstimate> mapSumPractices;
    private Map<Principle, ResponseEstimate> mapSumPrinciple;
    private final Double umbral=50.00;


    private PanelGrid panel = new PanelGrid();

    public ResultsChartViewBean() {
        oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
        createHorizontalBarMaturityLevel();
    }

//    @PostConstruct
//      public void init() {
//        oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
//        //model = null;
//        //createBarModels();
//        createHorizontalBarMaturityLevel();
//    }
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void createHorizontalBarMaturityLevel() {
        setMenuModel(1);
        setHorizontalBarModel(new HorizontalBarChartModel());

        ChartSeries chartTolerable = new ChartSeries();
        chartTolerable.setLabel("% tolerable");
        
        ChartSeries chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");

        ChartSeries chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");

        MaturityLevelDaoImplement mldi = new MaturityLevelDaoImplement();
        lstMaturityLevel = new ArrayList<MaturityLevel>();
        lstMaturityLevel = mldi.getMaturityLevels();

        mapMaturityLevelResults = new HashMap<MaturityLevel, Result>();
        //mapMaturityLevelResults = getResults(lstMaturityLevel);
        mapMaturityLevelResults = getResultsMaturityLevels();

        Collections.sort(lstMaturityLevel, MaturityLevelComparable.Comparators.ID);

        for (MaturityLevel oMaturityLevel : lstMaturityLevel) {
            for (Map.Entry<MaturityLevel, Result> entry : mapMaturityLevelResults.entrySet()) {
                MaturityLevel oML = entry.getKey();
                Result result = entry.getValue();
                if (oMaturityLevel.getId() == oML.getId()) {
                    chartComplete.set(oML.getLevelAbbreviation() + ". " + oML.getName(), result.getComplete());
                    chartPerComplete.set(oML.getLevelAbbreviation() + ". " + oML.getName(), result.getPerComplete());
                    chartTolerable.set(oML.getLevelAbbreviation() + ". " + oML.getName(),  this.umbral);
                }
            }

        }

        getHorizontalBarModel().addSeries(chartComplete);        
        //getHorizontalBarModel().addSeries(chartTolerable);
        getHorizontalBarModel().addSeries(chartPerComplete);
        getHorizontalBarModel().setShowDatatip(true);
        getHorizontalBarModel().setShowPointLabels(true);
        
        //getHorizontalBarModel().setDatatipFormat(getHorizontalBarModel().getDatatipFormat());
        //getHorizontalBarModel().setMouseoverHighlight(true);

        getHorizontalBarModel().setTitle(oRoleStakeholder.getOrganization().getName());
        getHorizontalBarModel().setAnimate(true);
        getHorizontalBarModel().setLegendPosition("e");
        getHorizontalBarModel().setStacked(true);
        getHorizontalBarModel().setZoom(true);
        //getHorizontalBarModel().setBarWidth(10);

        Axis xAxis = getHorizontalBarModel().getAxis(AxisType.X);
        xAxis.setLabel("Values");
        xAxis.setMin(0);
        xAxis.setMax(100);
        xAxis.setTickAngle(25); 
        xAxis.setTickFormat("%.4s%%");    

        Axis yAxis = getHorizontalBarModel().getAxis(AxisType.Y);
        yAxis.setLabel("Maturity Levels");
       
        //yAxis.setTickFormat("%s"+"NT");
        
        
    }

    public void createHorizontalBarPrinciplesByLevel(String sMaturityLevelId) {
        setMenuModel(2);

        setHorizontalBarModel(new HorizontalBarChartModel());

        ChartSeries chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");

        ChartSeries chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");

        PrincipleDaoImplement pdi = new PrincipleDaoImplement();
        List<Principle> lstPrinciple = new ArrayList<Principle>();
        lstPrinciple = pdi.getPrinciple();

        Map<Principle, Result> mapPrinciplesResults = new HashMap<Principle, Result>();
        mapPrinciplesResults = getResultsPrincipleByLevel(sMaturityLevelId);

        //Collections.sort(lstPrinciple, MaturityLevelComparable.Comparators.ID);         
        for (Principle oPri : lstPrinciple) {
            for (Map.Entry<Principle, Result> entry : mapPrinciplesResults.entrySet()) {
                Principle oPrinciple = entry.getKey();
                Result result = entry.getValue();
                if (oPrinciple.getId() == oPri.getId()) {
                    chartComplete.set(oPrinciple.getAbbreviation() + ". " + oPrinciple.getName(), result.getComplete());
                    chartPerComplete.set(oPrinciple.getAbbreviation() + ". " + oPrinciple.getName(), result.getPerComplete());
                }
            }
        }

        getHorizontalBarModel().addSeries(chartComplete);
        getHorizontalBarModel().addSeries(chartPerComplete);
        getHorizontalBarModel().setShowDatatip(true);
        getHorizontalBarModel().setShowPointLabels(true);

        //getHorizontalBarModel().setTitle(oRoleStakeholder.getOrganization().getName());
        getHorizontalBarModel().setTitle("Level "+sMaturityLevelId);
        getHorizontalBarModel().setAnimate(true);
        getHorizontalBarModel().setLegendPosition("e");
        getHorizontalBarModel().setStacked(true);
        getHorizontalBarModel().setZoom(true);

        Axis xAxis = getHorizontalBarModel().getAxis(AxisType.X);
        xAxis.setLabel("Values");
        xAxis.setMin(0);
        xAxis.setMax(100);
        xAxis.setTickFormat("%.4s%%");   

        Axis yAxis = getHorizontalBarModel().getAxis(AxisType.Y);
        yAxis.setLabel("Principles");
    }

    public void createHorizontalBarPrinciple(String sPrinciple) {
        //setHorizontalBarModel(new HorizontalBarChartModel());
        setMenuModel(2);
        horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");

        ChartSeries chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");

        Principle oPrinciple = new Principle();

        chartComplete.set("Principle", 5.0);
        chartPerComplete.set("Principle", getResultsPerComplete(5.0));

        horizontalBarModel.addSeries(chartComplete);
        horizontalBarModel.addSeries(chartPerComplete);

        horizontalBarModel.setTitle(sPrinciple);
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);

        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Values");
        xAxis.setMin(0);
        xAxis.setMax(100);

        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Principle");

    }

    public Map<MaturityLevel, Result> getResultsMaturityLevels() {
        try {
            mapSumPractices = new HashMap<Practice, ResponseEstimate>();
            mapSumPractices = calculatePractices(calculateQuestions());

            Map<MaturityLevel, ResponseEstimate> mapMaturityLevel = new HashMap<MaturityLevel, ResponseEstimate>();
            mapMaturityLevel = calculateMaturityLevel(mapSumPractices);

            Map<MaturityLevel, Result> mapMlResults = new HashMap<MaturityLevel, Result>();
            for (Map.Entry<MaturityLevel, ResponseEstimate> mapML : mapMaturityLevel.entrySet()) {
                MaturityLevel ml = mapML.getKey();
                ResponseEstimate rsp = mapML.getValue();
                Result oResult = new Result();
                Double dComplete = rsp.getAvg();
                oResult.setComplete(dComplete);
                oResult.setPerComplete(getResultsPerComplete(dComplete));
                mapMlResults.put(ml, oResult);
            }

            return mapMlResults;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<Principle, Result> getResultsPrincipleByLevel(String sMaturityLevelId) {
        try {
            // mapSumPractices=new HashMap<Practice, ResponseEstimate>();
            // mapSumPractices=calculatePractices(calculateQuestions());     
            Map<Principle, Result> mapPriResults = new HashMap<Principle, Result>();

            if (mapSumPractices.size() > 0 && mapSumPractices != null) {
                mapSumPrinciple = new HashMap<Principle, ResponseEstimate>();
                mapSumPrinciple = calculatePrincipleByMaturityLevel(mapSumPractices, sMaturityLevelId);

                for (Map.Entry<Principle, ResponseEstimate> mapPri : mapSumPrinciple.entrySet()) {
                    Principle pri = mapPri.getKey();
                    ResponseEstimate rsp = mapPri.getValue();
                    Result oResult = new Result();
                    Double dComplete = rsp.getAvg();
                    oResult.setComplete(dComplete);
                    oResult.setPerComplete(getResultsPerComplete(dComplete));
                    mapPriResults.put(pri, oResult);

                }
            }

            return mapPriResults;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<QuepQuestion, ResponseEstimate> calculateQuestions() {
        ResultsDaoImplement rdi = new ResultsDaoImplement();

        //1.Evaluation Responses
        //SUM REQ Weight
        List<QuepQuestionResponseOption> lstQQuestionResponseOption = new ArrayList<QuepQuestionResponseOption>();
        lstQQuestionResponseOption = rdi.getQuepQuestionResponseOption(oRoleStakeholder.getId().getIdOrganization());

        //2.Calculate Responses
        List<Response> lstResult = new ArrayList<Response>();
        lstResult = rdi.getListResponse(oRoleStakeholder.getId().getIdOrganization());

        Map<QuepQuestion, ResponseEstimate> mapSumResponseOp = new HashMap<QuepQuestion, ResponseEstimate>();
        for (QuepQuestionResponseOption oQQRO : lstQQuestionResponseOption) {
            ResponseEstimate oREstimate = new ResponseEstimate();
            Double sumReqQQuestion = new Double(0.0);        //cambiar mapeo de int a double      
            Double sumQQuestion = new Double(0.0);
            Double avg = new Double(0.0);

            //Get weight SUMs (included requiered SUM)
            for (QuepQuestionResponseOption oQQRO_aux : lstQQuestionResponseOption) {
                if (oQQRO_aux.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumQQuestion = sumQQuestion + oQQRO_aux.getResponseOption().getWeight();
                    if (oQQRO_aux.getResponseOption().getIsRequiered() == 1) {
                        sumReqQQuestion = sumReqQQuestion + oQQRO_aux.getResponseOption().getWeight();
                    }
                }
            }

            //Get Responses SUMs of an Organization 
            Double sumRsp = new Double(0.0);
            for (Response oResponse : lstResult) {
                if (oResponse.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumRsp = sumRsp + oResponse.getResponseOption().getWeight();
                }
            }
            //average Responses SUMs / Requiered SUM of Questions Configurated in QuEP
            if (sumReqQQuestion == 0.0) {
                sumReqQQuestion = 1.0;
            }
            avg = sumRsp / sumReqQQuestion;

            //setting map Reponses SUMs of Quep Questions
            oREstimate.setSumRequiered(sumReqQQuestion);
            oREstimate.setSum(sumQQuestion);
            oREstimate.setAvg(avg);
            mapSumResponseOp.put(oQQRO.getQuepQuestion(), oREstimate);
        }

        return mapSumResponseOp;
    }

    public Map<Practice, ResponseEstimate> calculatePractices(Map<QuepQuestion, ResponseEstimate> mapSumQuestions) {
        Map<Practice, ResponseEstimate> mapPracticeEstimate = new HashMap<Practice, ResponseEstimate>();
        List<Practice> lstPractice = new ArrayList<Practice>();

        PracticeDaoImplement prdao = new PracticeDaoImplement();
        lstPractice = prdao.getPractice();

        for (Practice oPr : lstPractice) {
            int size = 0;
            ResponseEstimate oREstimate = new ResponseEstimate();
            Double avg = new Double(0.0);
            for (Map.Entry<QuepQuestion, ResponseEstimate> oSumQ : mapSumQuestions.entrySet()) {
                QuepQuestion qq = oSumQ.getKey();
                ResponseEstimate rsp = oSumQ.getValue();
                if (qq.getPractice().getId() == oPr.getId()) {
                    avg = avg + rsp.getAvg();
                    size++;
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg / size);
            mapPracticeEstimate.put(oPr, oREstimate);
        }
        return mapPracticeEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrinciple(Map<Practice, ResultsChartViewBean.ResponseEstimate> mapSumPractices) {
        Map<Principle, ResultsChartViewBean.ResponseEstimate> mapPrincipleEstimate = new HashMap<Principle, ResultsChartViewBean.ResponseEstimate>();

        PrincipleDaoImplement pdi = new PrincipleDaoImplement();
        List<Principle> lstPrinciple = new ArrayList<Principle>();
        lstPrinciple = pdi.getPrinciple();

        for (Principle p : lstPrinciple) {
            int size = 0;
            ResponseEstimate oREstimate = new ResponseEstimate();
            Double avg = new Double(0.0);
            for (Map.Entry<Practice, ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                Practice pr = mPra.getKey();
                ResponseEstimate rsp = mPra.getValue();
                if (pr.getPrinciple().getId() == p.getId()) {
                    avg = avg + rsp.getAvg();
                    size++;
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg / size);
            mapPrincipleEstimate.put(p, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<Principle, ResponseEstimate> calculatePrincipleByMaturityLevel(Map<Practice, ResultsChartViewBean.ResponseEstimate> mapSumPractices, String sMaturityLevelId) {
        Map<Principle, ResultsChartViewBean.ResponseEstimate> mapPrincipleEstimate = new HashMap<Principle, ResultsChartViewBean.ResponseEstimate>();

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();
        PrincipleDaoImplement pdi = new PrincipleDaoImplement();

        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();

        List<Principle> lstPrinciple = new ArrayList<Principle>();
        lstPrinciple = pdi.getPrinciple();

        for (Principle pri : lstPrinciple) {
            ResultsChartViewBean.ResponseEstimate oREstimate = new ResultsChartViewBean.ResponseEstimate();
            Double avg = new Double(0.0);
            int size = 0;
            for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                if (mlp.getId().getIdMaturityLevel() == Integer.parseInt(sMaturityLevelId)) {
                    for (Map.Entry<Practice, ResultsChartViewBean.ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                        Practice pra = mPra.getKey();
                        ResponseEstimate rsp = mPra.getValue();
                        if (pra.getId() == mlp.getId().getIdPractice()
                                && pri.getId() == mlp.getIdPrinciple()) {
                            avg = avg + rsp.getAvg();
                            size++;
                        }
                    }
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg / size);
            mapPrincipleEstimate.put(pri, oREstimate);
        }
        return mapPrincipleEstimate;
    }

    public Map<MaturityLevel, ResponseEstimate> calculateMaturityLevel(Map<Practice, ResponseEstimate> mapSumPractices) {
        Map<MaturityLevel, ResponseEstimate> mapMaturityLevelEstimate = new HashMap<MaturityLevel, ResponseEstimate>();

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();
        PrincipleDaoImplement pdi = new PrincipleDaoImplement();

        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();

        List<MaturityLevel> lstMaturityLevel = new ArrayList<MaturityLevel>();
        lstMaturityLevel = mldao.getMaturityLevels();
        
        ResponseEstimate oREstimate = new ResponseEstimate();
        
        Double sumLastLevel =0.0;
        int sizeLevels=0;
        MaturityLevel oLastMaturityLevel = new MaturityLevel();
        for (MaturityLevel ml : lstMaturityLevel) {
            if (ml.getId() != 10) { //add un campo mas que indique que es el último nivel en la BD
                oREstimate = new ResponseEstimate();
                Double avg = new Double(0.0);
                int size = 0;
                for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                    //if(mlp.getId().getIdMaturityLevel()==ml.getId()){
                    for (Map.Entry<Practice, ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                        Practice pra = mPra.getKey();
                        ResponseEstimate rsp = mPra.getValue();
                        if (pra.getId() == mlp.getId().getIdPractice()
                                && mlp.getId().getIdMaturityLevel() == ml.getId()) {
                            avg = avg + rsp.getAvg();
                            size++;
                        }
                    }
                    // }
                }
                if (size == 0) {
                    size = 1;
                }
                oREstimate.setAvg(avg / size);
                mapMaturityLevelEstimate.put(ml, oREstimate);
                
                sumLastLevel=sumLastLevel+avg;
                sizeLevels++;
            }
            else{
                oLastMaturityLevel=ml;
            }
        }
        
        //setting last level
        oREstimate = new ResponseEstimate();
        oREstimate.setAvg(sumLastLevel/sizeLevels);        
        mapMaturityLevelEstimate.put(oLastMaturityLevel, oREstimate);

        return mapMaturityLevelEstimate;
    }

    public Double getResultsPerComplete(Double dResult) {
        try {
            Double dResultPerComplete;
            dResultPerComplete = (-1) * (dResult - 100);
            return dResultPerComplete;
        } catch (Exception e) {
            return null;
        }
    }

    public void listener(ItemSelectEvent e) {
            createHorizontalBarPrinciplesByLevel(String.valueOf(e.getSeriesIndex()));
    }

    BreadCrumb bc = new BreadCrumb();
    UIForm form = new UIForm();

    public void setMenuModel(int band) {
       // MenuModel menuModel=new DefaultMenuModel();
        
        if (band == 2) {
            UIMenuItem mp = new UIMenuItem();
            mp.setValue("Principles");
            mp.setUrl("#");

            RequestContext context = RequestContext.getCurrentInstance();
            context.update("frmMenu");

            bc.getChildren().add(mp);
            //menuModel.addElement(mp);
            //bc.setModel(menuModel);
            
            form.getChildren().add(bc);
            form.setSubmitted(true);
            panel.getChildren().add(form);
        } else {
            panel = new PanelGrid();
            panel.setId("pnlMain");

            form = new UIForm();
            form.setId("frmMenu");

            bc = new BreadCrumb();
            bc.setId("bc");

            UIMenuItem mh = new UIMenuItem();
            mh.setValue("Home");
            mh.setUrl("#");
            //menuModel.addElement(mh);           
            bc.getChildren().add(mh); 

            
            UIMenuItem mml = new UIMenuItem();
            mml.setId("mml");
            mml.setValue("Maturity Levels");    
            mml.setUrl("#");
//            mml.setOnclick("onClickMenuItem()");
            //mml.addActionListener(new MenuActionListener());
            mml.addActionListener(new ActionListener() {
                @Override
                public void processAction(ActionEvent event) throws AbortProcessingException {
                    onClickOnLevel(event);
                }
            });
            bc.getChildren().add(mml); 
            /*ExpressionFactory factory = FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
            ELContext elContext = FacesContext.getCurrentInstance().getELContext();
            MethodExpression expression = factory.createMethodExpression(elContext, "#{resultsChartViewBean.onClickOnLevel(" + mml + ")}", null, new Class[]{UIMenuItem.class});
            mml.addActionListener(new MethodExpressionActionListener(expression));
            menuModel.addElement(mml);*/
            //           
            
           // bc.setModel(menuModel);
            bc.isDynamic();
            
            form.getChildren().add(bc);
            form.setSubmitted(true);
            panel.getChildren().add(form);
        }
    }

   /*class MenuActionListener implements ActionListener{
    @Override
    public void processAction(ActionEvent event) throws AbortProcessingException {
     onClickOnLevel(event);    
    }
*/
    
   public void onClickOnLevel(ActionEvent ae) {
        try {
            //createHorizontalBarMaturityLevel();
            ResultsChartViewBean resultsChartViewBean = new ResultsChartViewBean();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
//   
//   public void onClickOnL() {
//        try {
//                        ResultsChartViewBean resultsChartViewBean = new ResultsChartViewBean();
//            //createHorizontalBarMaturityLevel();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public UIForm getForm() {
        return form;
    }

    public void setForm(UIForm form) {
        this.form = form;
    }

    private class Result {

        private Double complete;
        private Double perComplete;

        public Result() {
        }

        public Double getComplete() {
            return complete;
        }

        public void setComplete(Double complete) {
            this.complete = complete;
        }

        public Double getPerComplete() {
            return perComplete;
        }

        public void setPerComplete(Double perComplete) {
            this.perComplete = perComplete;
        }
    }

    private class ResponseEstimate {

        private Double sumRequiered;
        private Double sum;
        private Double avg;

        public ResponseEstimate() {
        }

        public Double getSumRequiered() {
            return sumRequiered;
        }

        public void setSumRequiered(Double sumRequiered) {
            this.sumRequiered = sumRequiered;
        }

        public Double getSum() {
            return sum;
        }

        public void setSum(Double sum) {
            this.sum = sum;
        }

        public Double getAvg() {
            return avg;
        }

        public void setAvg(Double avg) {
            this.avg = avg;
        }

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

    /**
     * @param horizontalBarModel the horizontalBarModel to set
     */
    public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
        this.horizontalBarModel = horizontalBarModel;
    }

    public Map<MaturityLevel, Result> getMapMaturityLevelResults() {
        return mapMaturityLevelResults;
    }

    public void setMapMaturityLevelResults(Map<MaturityLevel, Result> mapMaturityLevelResults) {
        this.mapMaturityLevelResults = mapMaturityLevelResults;
    }

    public List<MaturityLevel> getLstMaturityLevel() {
        return lstMaturityLevel;
    }

    public void setLstMaturityLevel(List<MaturityLevel> lstMaturityLevel) {
        this.lstMaturityLevel = lstMaturityLevel;
    }

   
    public PanelGrid getPanel() {
        return panel;
    }

    public void setPanel(PanelGrid panel) {
        this.panel = panel;
    }

    public BreadCrumb getBc() {
        return bc;
    }

    public void setBc(BreadCrumb bc) {
        this.bc = bc;
    }

    public Map<Practice, ResponseEstimate> getMapSumPractices() {
        return mapSumPractices;
    }

    public void setMapSumPractices(Map<Practice, ResponseEstimate> mapSumPractices) {
        this.mapSumPractices = mapSumPractices;
    }

    public Map<Principle, ResponseEstimate> getMapSumPrinciple() {
        return mapSumPrinciple;
    }

    public void setMapSumPrinciple(Map<Principle, ResponseEstimate> mapSumPrinciple) {
        this.mapSumPrinciple = mapSumPrinciple;
    }

}
