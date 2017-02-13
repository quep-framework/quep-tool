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
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Practice;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;

import javax.enterprise.context.SessionScoped;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

//import org.primefaces.component.menuitem.MenuItem;

/**
 *
 * @author agna8685
 */
@Named
@SessionScoped
//@RequestScoped
public class ResultsChartViewBean implements Serializable {

    @Inject
    private AccessBean accessBean;
    
    @Inject
    private OrganizationBean organizationBean;
    
    
    private RoleStakeholder oRoleStakeholder = null;
    private Organization oOrganization=null;

    private HorizontalBarChartModel horizontalBarModel;
    private Map<MaturityLevel, Result> mapMaturityLevelResults;
    private List<MaturityLevel> lstMaturityLevel;

    private Map<Practice, ResponseEstimate> mapSumPractices;
    private Map<Principle, ResponseEstimate> mapSumPrinciple;
    private final BigDecimal umbral=BigDecimal.valueOf(50.00);


    private PanelGrid panel = new PanelGrid();

    public ResultsChartViewBean() {
        oRoleStakeholder = (RoleStakeholder) accessBean.getSessionObj("roleStakeholder");
        //oOrganization = (Organization) accessBean.getSessionObj("organization");
        oOrganization = organizationBean.getOrganization();
        createHorizontalBarMaturityLevel();
    }
    
    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }
    
    

    public void createHorizontalBarMaturityLevel() {
        //setMenuModel(1);
        setMenuBreadCrumb(1);
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
        
        horizontalBarModel.clear();

        horizontalBarModel.addSeries(chartComplete);        
        horizontalBarModel.addSeries(chartPerComplete);
        horizontalBarModel.setShowDatatip(true);
        horizontalBarModel.setShowPointLabels(true);

        horizontalBarModel.setTitle(oOrganization.getName());
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setZoom(true);
        
        //getHorizontalBarModel().setZoom(true);
      

        //Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        horizontalBarModel.getAxis(AxisType.X).setLabel("Values");
        horizontalBarModel.getAxis(AxisType.X).setMin(0);
        horizontalBarModel.getAxis(AxisType.X).setMax(100);
        horizontalBarModel.getAxis(AxisType.X).setTickAngle(25); 
        horizontalBarModel.getAxis(AxisType.X).setTickFormat("%.4s%%");    

        //Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        horizontalBarModel.getAxis(AxisType.Y).setLabel("Maturity Levels");               
        
    }

    public void createHorizontalBarPrinciplesByLevel(String sMaturityLevelId) {
        //setMenuModel(2);
        setMenuBreadCrumb(2);

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
        //setMenuModel(2);
        //menu2(2);
        horizontalBarModel = new HorizontalBarChartModel();

        ChartSeries chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");

        ChartSeries chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");

        Principle oPrinciple = new Principle();

        chartComplete.set("Principle", 5.0);
        chartPerComplete.set("Principle", getResultsPerComplete(BigDecimal.valueOf(5.0)));

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
            //if (mapSumPractices==0) mapSumPractices=1;

            Map<MaturityLevel, ResponseEstimate> mapMaturityLevel = new HashMap<MaturityLevel, ResponseEstimate>();
            mapMaturityLevel = calculateMaturityLevel(mapSumPractices);

            Map<MaturityLevel, Result> mapMlResults = new HashMap<MaturityLevel, Result>();
            for (Map.Entry<MaturityLevel, ResponseEstimate> mapML : mapMaturityLevel.entrySet()) {
                MaturityLevel ml = mapML.getKey();
                ResponseEstimate rsp = mapML.getValue();
                Result oResult = new Result();
                BigDecimal dComplete = rsp.getAvg();
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
                    BigDecimal dComplete = rsp.getAvg();
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
       lstQQuestionResponseOption = rdi.getQuepQuestionResponseOption(oOrganization.getId());

        //2.Calculate Responses
        List<Response> lstResult = new ArrayList<Response>();
        lstResult = rdi.getListResponse(oOrganization.getId());

        Map<QuepQuestion, ResponseEstimate> mapSumResponseOp = new HashMap<QuepQuestion, ResponseEstimate>();
        for (QuepQuestionResponseOption oQQRO : lstQQuestionResponseOption) {
            ResponseEstimate oREstimate = new ResponseEstimate();
            BigDecimal sumReqQQuestion = BigDecimal.ZERO ;        //cambiar mapeo de int a double      
            BigDecimal sumQQuestion =  BigDecimal.ZERO ; 
            BigDecimal avg =  BigDecimal.ZERO ;  
            

            //Get weight SUMs (included requiered SUM)
            for (QuepQuestionResponseOption oQQRO_aux : lstQQuestionResponseOption) {
                if (oQQRO_aux.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumQQuestion = sumQQuestion.add(oQQRO_aux.getResponseOption().getWeight()) ;
                    if (oQQRO_aux.getResponseOption().getIsRequiered() == 1) {
                        sumReqQQuestion = sumReqQQuestion.add(oQQRO_aux.getResponseOption().getWeight());
                    }
                }
            }

            //Get Responses SUMs of an Organization 
            BigDecimal sumRsp = BigDecimal.ZERO;
            for (Response oResponse : lstResult) {
                if (oResponse.getId().getIdQuepQuestion() == oQQRO.getId().getIdQuepQuestion()) {
                    sumRsp = sumRsp.add(oResponse.getResponseOption().getWeight());
                }
            }
            //average Responses SUMs / Requiered SUM of Questions Configurated in QuEP
            if (sumReqQQuestion==BigDecimal.valueOf(0.0) || sumReqQQuestion.equals(BigDecimal.valueOf(0))) {
                sumReqQQuestion = BigDecimal.valueOf(1.0);
            }
            avg = sumRsp.divide(sumReqQQuestion,2, RoundingMode.HALF_EVEN);
            

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
            BigDecimal avg = BigDecimal.ZERO;
            for (Map.Entry<QuepQuestion, ResponseEstimate> oSumQ : mapSumQuestions.entrySet()) {
                QuepQuestion qq = oSumQ.getKey();
                ResponseEstimate rsp = oSumQ.getValue();
                if (qq.getPractice().getId() == oPr.getId()) {
                    avg = avg.add(rsp.getAvg());
                    size++;
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size),2, RoundingMode.HALF_EVEN));
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
            BigDecimal avg = BigDecimal.ZERO;
            for (Map.Entry<Practice, ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                Practice pr = mPra.getKey();
                ResponseEstimate rsp = mPra.getValue();
                if (pr.getPrinciple().getId() == p.getId()) {
                    avg = avg.add(rsp.getAvg());
                    size++;
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size),2, RoundingMode.HALF_EVEN));
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
            BigDecimal avg = BigDecimal.ZERO;
            int size = 0;
            for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                if (mlp.getId().getIdMaturityLevel() == Integer.parseInt(sMaturityLevelId)) {
                    for (Map.Entry<Practice, ResultsChartViewBean.ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                        Practice pra = mPra.getKey();
                        ResponseEstimate rsp = mPra.getValue();
                        if (pra.getId() == mlp.getId().getIdPractice()
                                && pri.getId() == mlp.getIdPrinciple()) {
                            avg = avg.add(rsp.getAvg());
                            size++;
                        }
                    }
                }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size),2, RoundingMode.HALF_EVEN));
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
        
        BigDecimal sumLastLevel = BigDecimal.ZERO;
        int sizeLevels=0;
        MaturityLevel oLastMaturityLevel = new MaturityLevel();
        for (MaturityLevel ml : lstMaturityLevel) {
            if (ml.getId() != 10) { //add un campo mas que indique que es el último nivel en la BD
                oREstimate = new ResponseEstimate();
                BigDecimal avg = BigDecimal.ZERO;
                int size = 0;
                for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                    //if(mlp.getId().getIdMaturityLevel()==ml.getId()){
                    for (Map.Entry<Practice, ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                        Practice pra = mPra.getKey();
                        ResponseEstimate rsp = mPra.getValue();
                        if (pra.getId() == mlp.getId().getIdPractice()
                                && mlp.getId().getIdMaturityLevel() == ml.getId()) {
                            avg = avg.add(rsp.getAvg());
                            size++;
                        }
                    }
                    // }
                }
                if (size == 0) {
                    size = 1;
                }
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size),2, RoundingMode.HALF_EVEN));
                mapMaturityLevelEstimate.put(ml, oREstimate);
                
                sumLastLevel=sumLastLevel.add(avg);
                sizeLevels++;
            }
            else{
                oLastMaturityLevel=ml;
            }
        }
        
        //setting last level
        oREstimate = new ResponseEstimate();
        oREstimate.setAvg(sumLastLevel.divide(BigDecimal.valueOf(sizeLevels),2, RoundingMode.HALF_EVEN));        
        mapMaturityLevelEstimate.put(oLastMaturityLevel, oREstimate);

        return mapMaturityLevelEstimate;
    }

    public BigDecimal getResultsPerComplete(BigDecimal dResult) {
        try {
            BigDecimal dResultPerComplete;
            dResultPerComplete = dResult.subtract(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(-1));
            return dResultPerComplete;
        } catch (Exception e) {
            return null;
        }
    }

    public void listener(ItemSelectEvent e) {
            createHorizontalBarPrinciplesByLevel(String.valueOf(e.getSeriesIndex()));
    }
 
    private MenuModel model;
    public MenuModel getModel() {
        return model;
    }
    
    public void setMenuBreadCrumb(int band) {
        
        model = new DefaultMenuModel();
        DefaultMenuItem item = new DefaultMenuItem("");
        item.setCommand("#{resultsChartViewBean.createHorizontalBarMaturityLevel}");
        item.setUpdate("idchart");
        model.addElement(item);
        
        item = new DefaultMenuItem("Maturity Levels");
        item.setCommand("#{resultsChartViewBean.createHorizontalBarMaturityLevel}");
        item.setUpdate("idchart");
        item.setId("mMl");
        model.addElement(item);
       

             item = new DefaultMenuItem("Principles");
             item.setId("mPri");
            //itemP.setCommand("#");
            //item.setUpdate("idchart");
            model.addElement(item);
    }
    

    private class Result {

        private BigDecimal complete;
        private BigDecimal perComplete;

        public Result() {
        }

        public BigDecimal getComplete() {
            return complete;
        }

        public void setComplete(BigDecimal complete) {
            this.complete = complete;
        }

        public BigDecimal getPerComplete() {
            return perComplete;
        }

        public void setPerComplete(BigDecimal perComplete) {
            this.perComplete = perComplete;
        }
    }

    private class ResponseEstimate {

        private BigDecimal sumRequiered;
        private BigDecimal sum;
        private BigDecimal avg;

        public ResponseEstimate() {
        }

        public BigDecimal getSumRequiered() {
            return sumRequiered;
        }

        public void setSumRequiered(BigDecimal sumRequiered) {
            this.sumRequiered = sumRequiered;
        }

        public BigDecimal getSum() {
            return sum;
        }

        public void setSum(BigDecimal sum) {
            this.sum = sum;
        }

        public BigDecimal getAvg() {
            return avg;
        }

        public void setAvg(BigDecimal avg) {
            this.avg = avg;
        }

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

    public Organization getoOrganization() {
        return oOrganization;
    }
    
    public String getOOrganizationName() {
        return oOrganization.getName();
    }

    public void setoOrganization(Organization oOrganization) {
        this.oOrganization = oOrganization;
    }

    
}
