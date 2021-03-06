/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.dao.PracticeDaoImplement;
import es.upv.dsic.quep.dao.PrincipleDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.Organization;
import es.upv.dsic.quep.model.Practice;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.RoleStakeholder;
import es.upv.es.dsic.quep.utils.ResponseEstimate;
import es.upv.es.dsic.quep.utils.Result;
import es.upv.es.dsic.quep.utils.Results;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;
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
public class PrincipleResultsChartViewBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    @Inject
    private OrganizationBean organizationBean;

    private RoleStakeholder oRoleStakeholder = null;
    private Organization oOrganization = null;
    private Principle oPrinciple;
    private MaturityLevel oMaturityLevel;

    private HorizontalBarChartModel horizontalBarModel;
    private Map<MaturityLevel, Result> mapMaturityLevelResults;
    private Map<Principle, Result> mapPrinciplesResults = new HashMap<Principle, Result>();
    private Map<Practice, Result> mapPracticeResults = new HashMap<Practice, Result>();
    private Map<QuepQuestion, Result> mapQuepQuestionResults = new HashMap<QuepQuestion, Result>();

    private List<MaturityLevel> lstMaturityLevel;
    private List<Principle> lstPrinciple = new ArrayList<Principle>();
    private List<Practice> lstPractice = new ArrayList<Practice>();

    private Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions = new HashMap<QuepQuestion, ResponseEstimate>();
    

    private final BigDecimal umbral = BigDecimal.valueOf(50.00);

    private PanelGrid panel = new PanelGrid();

    private ChartSeries chartTolerable;
    private ChartSeries chartComplete;
    private ChartSeries chartPerComplete;

    private MenuModel model;

    private Results results = new Results();
    private int charLevel;
    private int imenu;
    //private boolean bExport;
    private String titleChar="";
    
    private String legendNumberStk="";
    
    private boolean bSessionPrincipleResult=false;

    public PrincipleResultsChartViewBean() {
        
    }

    @PostConstruct
    public void init() {
        oOrganization = organizationBean.getOrganization();
        imenu = 1;
        createHorizontalBarPrinciples();
        bSessionPrincipleResult=true;
    }
    

    public void initValues(String sId) {
        //results = new Results();        
        setMenuBreadCrumb(imenu);
        setHorizontalBarModel(new HorizontalBarChartModel());

        chartTolerable = new ChartSeries();
        chartTolerable.setLabel("% tolerable");

        chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");

        chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");

        PrincipleDaoImplement pdi = new PrincipleDaoImplement();
        lstPrinciple = pdi.getPrinciple();

        if (imenu == 1) { //by principles           
            mapSumQuepQuestions = new HashMap<QuepQuestion, ResponseEstimate>();
            mapSumQuepQuestions = results.calculateQuestions(oOrganization.getId());

            mapPrinciplesResults = new HashMap<Principle, Result>();
            mapPrinciplesResults = getResultsPrinciple();
        } else if (imenu == 2) { //by practice
            mapPracticeResults = new HashMap<Practice, Result>();
            mapPracticeResults = getResultsPracticesByPrinciple(sId);
        }
        
        legendNumberStk=getlegendQuestionnaires();
        /*else if (imenu == 3) { //by practice
            mapQuepQuestionResults = new HashMap<QuepQuestion, Result>();
            mapQuepQuestionResults = getResultsQuestionsByPractice(sId);
        }*/
    }

    public void drawHorizontalBarModel(String sXAxis, String sYAxis, String title) {
        titleChar=title;        
        horizontalBarModel.clear();
        horizontalBarModel.addSeries(chartComplete);
        horizontalBarModel.addSeries(chartPerComplete);
        //horizontalBarModel.setSeriesColors("0F5FE9,E4EAF0");
        horizontalBarModel.setSeriesColors("00749F,E4EAF0");
        horizontalBarModel.setShowDatatip(true);
        horizontalBarModel.setShowPointLabels(true);
        horizontalBarModel.setTitle(titleChar);
        horizontalBarModel.setAnimate(true);
        //horizontalBarModel.setLegendPosition("se");
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setZoom(true);
        horizontalBarModel.getAxis(AxisType.X).setLabel(sXAxis);
        horizontalBarModel.getAxis(AxisType.X).setMin(0);
        horizontalBarModel.getAxis(AxisType.X).setMax(100);
        horizontalBarModel.getAxis(AxisType.X).setTickAngle(25);
        horizontalBarModel.getAxis(AxisType.X).setTickInterval("5");
        horizontalBarModel.getAxis(AxisType.X).setTickFormat("%.4s%%");
        horizontalBarModel.getAxis(AxisType.Y).setLabel(sYAxis);
    }

    public void createHorizontalBarPrinciples() {
        String sChart = "";
        initValues("");

        charLevel = 0;
        //Collections.sort(lstPrinciple, MaturityLevelComparable.Comparators.ID);         
        for (Principle oPri : lstPrinciple) {
            for (Map.Entry<Principle, Result> entry : mapPrinciplesResults.entrySet()) {
                Principle op = entry.getKey();
                Result result = entry.getValue();
                if (op.getId() == oPri.getId()) {
                    sChart = op.getAbbreviation() + ". " + op.getName();
                    //if (!op.getAbbreviation().contains("null")) sChart= op.getAbbreviation()+ ". " + sChart;
                    chartComplete.set(sChart, result.getComplete());
                    chartPerComplete.set(sChart, result.getPerComplete());
                }
            }
        }
        drawHorizontalBarModel("Porcentaje obtenido", "Principios", "Resultados por Principios");
    }

    public void createHorizontalBarPracticeByPrinciple(String sPrincipleId) {
        String sChart = "";
        initValues(sPrincipleId);

        PracticeDaoImplement pdi = new PracticeDaoImplement();
        lstPractice = pdi.getPractice();

        charLevel++;

        //Collections.sort(lstPrinciple, MaturityLevelComparable.Comparators.ID);         
        for (Practice oPra : lstPractice) {
            for (Map.Entry<Practice, Result> entry : mapPracticeResults.entrySet()) {
                Practice op = entry.getKey();
                Result result = entry.getValue();
                if (op.getId() == oPra.getId()) {
                    sChart = op.getName();
                    //if (!op.getAbbreviation().contains("null")) sChart= op.getAbbreviation()+ ". " + sChart;
                    chartComplete.set(sChart, result.getComplete());
                    chartPerComplete.set(sChart, result.getPerComplete());
                    //chartComplete.set(op.getAbbreviation() + ". " + op.getName(), result.getComplete());
                    //chartPerComplete.set(op.getAbbreviation() + ". " + op.getName(), result.getPerComplete());
                }
            }
        }

        PrincipleDaoImplement prdi = new PrincipleDaoImplement();
        oPrinciple = prdi.getPrinciple(Integer.parseInt(sPrincipleId));

        drawHorizontalBarModel("Porcentaje obtenido", "Principio: " + oPrinciple.getName(),"Resultados por Practicas");
    }

    public Map<Principle, Result> getResultsPrinciple() {
        try {
            Map<Principle, Result> mapPriResults = new HashMap<Principle, Result>();
            if (mapSumQuepQuestions.size() > 0 && mapSumQuepQuestions != null) {
                Map<Principle, ResponseEstimate> mapSumPrinciple = new HashMap<Principle, ResponseEstimate>();

                mapSumPrinciple = results.calculatePrincipleBySumQuestions(mapSumQuepQuestions, lstPrinciple);

                for (Map.Entry<Principle, ResponseEstimate> mapPri : mapSumPrinciple.entrySet()) {
                    Principle pri = mapPri.getKey();
                    ResponseEstimate rsp = mapPri.getValue();
                    Result oResult = new Result();
                    BigDecimal dComplete = rsp.getAvg();
                    
                    BigDecimal scale;
                    scale = new BigDecimal(100.00);                    
                    dComplete = dComplete.multiply(scale);
                    
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

    public Map<Practice, Result> getResultsPracticesByPrinciple(String sPrincipleId) {
        try {
            Map<Practice, Result> mapPraResults = new HashMap<Practice, Result>();
            if (mapSumQuepQuestions.size() > 0 && mapSumQuepQuestions != null) {
                Map<Practice, ResponseEstimate> mapSumPractice = new HashMap<Practice, ResponseEstimate>();

                mapSumPractice = results.calculatePracticesBySumQuestions(mapSumQuepQuestions, sPrincipleId);

                for (Map.Entry<Practice, ResponseEstimate> mapPra : mapSumPractice.entrySet()) {
                    Practice pra = mapPra.getKey();
                    ResponseEstimate rsp = mapPra.getValue();
                    Result oResult = new Result();
                    BigDecimal dComplete = rsp.getAvg();
                    
                    BigDecimal scale;
                    scale = new BigDecimal(100.00);                    
                    dComplete = dComplete.multiply(scale);
                    
                    oResult.setComplete(dComplete);
                    oResult.setPerComplete(getResultsPerComplete(dComplete));
                    mapPraResults.put(pra, oResult);
                }
            }
            return mapPraResults;
        } catch (Exception e) {
            return null;
        }
    }

    public Map<QuepQuestion, Result> getResultsQuestionsByPractice(String sPracticeId) {
        try {
            mapQuepQuestionResults = new HashMap<QuepQuestion, Result>();
            if (mapSumQuepQuestions.size() > 0 && mapSumQuepQuestions != null) {
                for (Map.Entry<QuepQuestion, ResponseEstimate> mapPra : mapSumQuepQuestions.entrySet()) {
                    QuepQuestion qq = mapPra.getKey();
                    if (qq.getPractice().getId() == Integer.parseInt(sPracticeId)) {
                        ResponseEstimate rsp = mapPra.getValue();
                        Result oResult = new Result();
                        BigDecimal dComplete = rsp.getAvg();

                        BigDecimal scale;
                        scale = new BigDecimal(100.00);
                        dComplete = dComplete.multiply(scale);

                        oResult.setComplete(dComplete);
                        oResult.setPerComplete(getResultsPerComplete(dComplete));
                        mapQuepQuestionResults.put(qq, oResult);
                    }
                }
            }
            return mapQuepQuestionResults;
        } catch (Exception e) {
            return null;
        }
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
    
    public String getlegendQuestionnaires(){
        Results oResults = new Results();
        int cStatusComplete=0;
        cStatusComplete=oResults.calculteLegendQR(oOrganization.getId(),1);
        int cStatusPerComplete=0;
        cStatusPerComplete=oResults.calculteLegendQR(oOrganization.getId(),2);
        return String.valueOf(cStatusComplete) +
                " completados de "+
                String.valueOf(cStatusComplete+cStatusPerComplete) +
                " configurados";                
    }

    public void listener(ItemSelectEvent e) {
        String str = organizationBean.getNavigationBean().getCurrentPage();
        if (charLevel == 0) {
            imenu = 2;
            createHorizontalBarPracticeByPrinciple(String.valueOf(e.getItemIndex() + 1));
        } 
        /*else if (charLevel == 1) {
            imenu = 3;
            createHorizontalBarQuestionsByPractice(String.valueOf(e.getItemIndex() + 1));
        }*/
    }

    public void setMenuBreadCrumb(int band) {
        model = new DefaultMenuModel();
        DefaultMenuItem item = new DefaultMenuItem("");
        item.setCommand("#{principleResultsChartViewBean.createHorizontalBarPrinciples}");
        item.setUpdate("idchart1");
        model.addElement(item);

        item = new DefaultMenuItem("Principles");
        item.setCommand("#{principleResultsChartViewBean.createHorizontalBarPrinciples}");
        item.setUpdate("idchart1");
        item.setId("mPri");
        model.addElement(item);

        item = new DefaultMenuItem("Practice");
        //item.setCommand("#{principleResultsChartViewBean.createHorizontalBarPrinciples}");
        item.setId("mPra");
        model.addElement(item);

        /*item = new DefaultMenuItem("Questions");
        item.setId("mQuepQ");
        model.addElement(item);*/
    }

    public MenuModel getModel() {
        return model;
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

    public Organization getoOrganization() {
        return oOrganization;
    }

    public String getOOrganizationName() {
        return oOrganization.getName();
    }

    public void setoOrganization(Organization oOrganization) {
        this.oOrganization = oOrganization;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public OrganizationBean getOrganizationBean() {
        return organizationBean;
    }

    public void setOrganizationBean(OrganizationBean organizationBean) {
        this.organizationBean = organizationBean;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {        
        return horizontalBarModel;
    }

    public BigDecimal getUmbral() {
        return umbral;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }

    public ChartSeries getChartTolerable() {
        return chartTolerable;
    }

    public void setChartTolerable(ChartSeries chartTolerable) {
        this.chartTolerable = chartTolerable;
    }

    public ChartSeries getChartComplete() {
        return chartComplete;
    }

    public void setChartComplete(ChartSeries chartComplete) {
        this.chartComplete = chartComplete;
    }

    public ChartSeries getChartPerComplete() {
        return chartPerComplete;
    }

    public void setChartPerComplete(ChartSeries chartPerComplete) {
        this.chartPerComplete = chartPerComplete;
    }

    public List<Principle> getLstPrinciple() {
        return lstPrinciple;
    }

    public void setLstPrinciple(List<Principle> lstPrinciple) {
        this.lstPrinciple = lstPrinciple;
    }

    public Map<Principle, Result> getMapPrinciplesResults() {
        return mapPrinciplesResults;
    }

    public void setMapPrinciplesResults(Map<Principle, Result> mapPrinciplesResults) {
        this.mapPrinciplesResults = mapPrinciplesResults;
    }

    public Map<QuepQuestion, ResponseEstimate> getMapSumQuepQuestions() {
        return mapSumQuepQuestions;
    }

    public void setMapSumQuepQuestions(Map<QuepQuestion, ResponseEstimate> mapSumQuepQuestions) {
        this.mapSumQuepQuestions = mapSumQuepQuestions;
    }

    public int getCharLevel() {
        return charLevel;
    }

    public void setCharLevel(int charLevel) {
        this.charLevel = charLevel;
    }

    public int getImenu() {
        return imenu;
    }

    public void setImenu(int imenu) {
        this.imenu = imenu;
    }

    public List<Practice> getLstPractice() {
        return lstPractice;
    }

    public void setLstPractice(List<Practice> lstPractice) {
        this.lstPractice = lstPractice;
    }

    public Map<Practice, Result> getMapPracticeResults() {
        return mapPracticeResults;
    }

    public void setMapPracticeResults(Map<Practice, Result> mapPracticeResults) {
        this.mapPracticeResults = mapPracticeResults;
    }

    public Principle getoPrinciple() {
        return oPrinciple;
    }

    public void setoPrinciple(Principle oPrinciple) {
        this.oPrinciple = oPrinciple;
    }

    public MaturityLevel getoMaturityLevel() {
        return oMaturityLevel;
    }

    public void setoMaturityLevel(MaturityLevel oMaturityLevel) {
        this.oMaturityLevel = oMaturityLevel;
    }

    public Map<MaturityLevel, Result> getMapMaturityLevelResults() {
        return mapMaturityLevelResults;
    }

    public void setMapMaturityLevelResults(Map<MaturityLevel, Result> mapMaturityLevelResults) {
        this.mapMaturityLevelResults = mapMaturityLevelResults;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public Map<QuepQuestion, Result> getMapQuepQuestionResults() {
        return mapQuepQuestionResults;
    }

    public void setMapQuepQuestionResults(Map<QuepQuestion, Result> mapQuepQuestionResults) {
        this.mapQuepQuestionResults = mapQuepQuestionResults;
    }

    /*public boolean getbExport() {
        if (charLevel<1) bExport=true; 
        else bExport=false;
        return bExport;
    }

    public void setbExport(boolean bExport) {
        this.bExport = bExport;
    }*/

    public String getTitleChar() {
        return titleChar;
    }

    public void setTitleChar(String titleChar) {
        this.titleChar = titleChar;
    }
    
    public String getLegendNumberStk() {
        return legendNumberStk;
    }

    public void setLegendNumberStk(String legendNumberStk) {
        this.legendNumberStk = legendNumberStk;
    }

    public boolean isbSessionPrincipleResult() {
        return bSessionPrincipleResult;
    }

    public void setbSessionPrincipleResult(boolean bSessionPrincipleResult) {
        this.bSessionPrincipleResult = bSessionPrincipleResult;
    }

    
}
