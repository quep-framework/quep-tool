/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.converters.MaturityLevelComparable;
import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
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
public class MaturityLevelResultsChartViewBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    @Inject
    private OrganizationBean organizationBean;

    @Inject
    BeanManager manager;

    private RoleStakeholder oRoleStakeholder = null;
    private Organization oOrganization = null;
    private Principle oPrinciple;
    private MaturityLevel oMaturityLevel;

    private HorizontalBarChartModel horizontalBarModel;
    private Map<MaturityLevel, Result> mapMaturityLevelResults;
    private Map<Principle, Result> mapPrinciplesResults = new HashMap<Principle, Result>();
    private Map<Practice, Result> mapPracticeResults = new HashMap<Practice, Result>();

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
    private Results results = new Results();;

    private int charLevel;
    private int imenu;
    private String titleChar="";
    
    private String legendNumberStk="";
    private boolean bSessionMLResults=false;
    
    public MaturityLevelResultsChartViewBean() {
        
    }

    @PostConstruct
    public void init() {
        oOrganization = organizationBean.getOrganization();                
        imenu = 1;
        
        createHorizontalBarMaturityLevel();
        bSessionMLResults=true;
    }
    
   


    public void initValues(String sId) {
       // results = new Results();        
        setMenuBreadCrumb(imenu);
        setHorizontalBarModel(new HorizontalBarChartModel());
        
        chartTolerable = new ChartSeries();
        chartTolerable.setLabel("% tolerable");

        chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");        

        chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");

        if (imenu == 1) {//by maturity 
            MaturityLevelDaoImplement mldi = new MaturityLevelDaoImplement();
            lstMaturityLevel = new ArrayList<MaturityLevel>();
            lstMaturityLevel = mldi.getMaturityLevels();

            mapSumQuepQuestions = new HashMap<QuepQuestion, ResponseEstimate>();
            mapSumQuepQuestions = results.calculateQuestions(oOrganization.getId());

            mapMaturityLevelResults = new HashMap<MaturityLevel, Result>();
            mapMaturityLevelResults = getResultsMaturityLevels();                        

        } else if (imenu == 2) {//principle by id maturity level
            PrincipleDaoImplement pdi = new PrincipleDaoImplement();
            lstPrinciple = pdi.getPrinciple();

            mapPrinciplesResults = new HashMap<Principle, Result>();
            mapPrinciplesResults = getResultsPrincipleByLevel(sId);
        }
        
        legendNumberStk=getlegendQuestionnaires();
    }

    public void drawHorizontalBarModel(String sXAxis, String sYAxis, String title) {
        titleChar=title;        
        horizontalBarModel.clear();
        horizontalBarModel.addSeries(chartComplete);
         //color bar char        
        //horizontalBarModel.setSeriesColors("0F5FE9,E4EAF0");
        horizontalBarModel.setSeriesColors("00749F,E4EAF0");
        
            //horizontalBarModel.setSeriesColors("5196BB,FDAB84,5D9C78,EBCC4D,E290FB,FCD5FA,F8A43D,2C97EA,1C8481,717A79");
        
        horizontalBarModel.addSeries(chartPerComplete);
        
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
        //horizontalBarModel.getAxis(AxisType.X).setTickFormat("%2$d");
        horizontalBarModel.getAxis(AxisType.Y).setLabel(sYAxis);
    }

    public void createHorizontalBarMaturityLevel() {
        initValues("");

        charLevel = 0;

        Collections.sort(lstMaturityLevel, MaturityLevelComparable.Comparators.ID);

        for (MaturityLevel oMaturityLevel : lstMaturityLevel) {
            for (Map.Entry<MaturityLevel, Result> entry : mapMaturityLevelResults.entrySet()) {
                MaturityLevel oML = entry.getKey();
                Result result = entry.getValue();
                if (oMaturityLevel.getId() == oML.getId()) {
                    chartComplete.set(oML.getLevelAbbreviation() + ". " + oML.getName(), result.getComplete());
                    chartPerComplete.set(oML.getLevelAbbreviation() + ". " + oML.getName(), result.getPerComplete());
                    chartTolerable.set(oML.getLevelAbbreviation() + ". " + oML.getName(), this.umbral);
                }
            }
        }
        drawHorizontalBarModel("Porcentaje obtenido",oOrganization.getName(),"Resultados por Niveles de Madurez");
    }

    public void createHorizontalBarPrinciplesByLevel(String sMaturityLevelId) {
        initValues(sMaturityLevelId);

        charLevel++;

        //Collections.sort(lstPrinciple, MaturityLevelComparable.Comparators.ID);         
        for (Principle oPri : lstPrinciple) {
            for (Map.Entry<Principle, Result> entry : mapPrinciplesResults.entrySet()) {
                Principle op = entry.getKey();
                Result result = entry.getValue();
                if (op.getId() == oPri.getId()) {
                    chartComplete.set(op.getAbbreviation() + ". " + op.getName(), result.getComplete());
                    chartPerComplete.set(op.getAbbreviation() + ". " + op.getName(), result.getPerComplete());
                }
            }
        }

        MaturityLevelDaoImplement mdi = new MaturityLevelDaoImplement();
        oMaturityLevel = mdi.getMaturityLevel(Integer.parseInt(sMaturityLevelId));
        drawHorizontalBarModel("Porcentaje obtenido", "Level " +oMaturityLevel.getId()+": "+ oMaturityLevel.getName(), "Resultados por Principios");
    }

    public Map<MaturityLevel, Result> getResultsMaturityLevels() {
        try {
            Map<MaturityLevel, ResponseEstimate> mapMaturityLevel = new HashMap<MaturityLevel, ResponseEstimate>();

            if (mapSumQuepQuestions.size() > 0 && mapSumQuepQuestions != null) {
                mapMaturityLevel = results.calculateMaturityLevelBySumQuestions(mapSumQuepQuestions, lstMaturityLevel);
            }

            Map<MaturityLevel, Result> mapMlResults = new HashMap<MaturityLevel, Result>();
            for (Map.Entry<MaturityLevel, ResponseEstimate> mapML : mapMaturityLevel.entrySet()) {
                MaturityLevel ml = mapML.getKey();
                ResponseEstimate rsp = mapML.getValue();
                Result oResult = new Result();
                BigDecimal dComplete = rsp.getAvg();
                
                BigDecimal scale;
                scale= new BigDecimal (100.00);
                //scale=scale.setScale(4, RoundingMode.HALF_EVEN);
                dComplete=dComplete.multiply(scale);
                
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
            Map<Principle, Result> mapPriResults = new HashMap<Principle, Result>();
            if (mapSumQuepQuestions.size() > 0 && mapSumQuepQuestions != null) {
                Map<Principle, ResponseEstimate> mapSumPrinciple = new HashMap<Principle, ResponseEstimate>();

                if (!sMaturityLevelId.equals("10") && imenu == 2) {
                    mapSumPrinciple = results.calculatePrinciplesByIdMaturityLevel(mapSumQuepQuestions, lstPrinciple, sMaturityLevelId);
                } else {
                    mapSumPrinciple = results.calculatePrincipleBySumQuestions(mapSumQuepQuestions, lstPrinciple);
                }

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
        if (charLevel < 1) {
            imenu = 2;
            createHorizontalBarPrinciplesByLevel(String.valueOf(e.getItemIndex() + 1));
        }
    }

    public void setMenuBreadCrumb(int band) {
        model = new DefaultMenuModel();
        DefaultMenuItem item = new DefaultMenuItem("");
        item.setCommand("#{maturityLevelResultsChartViewBean.createHorizontalBarMaturityLevel}");
        item.setUpdate("idchart");
        model.addElement(item);

        item = new DefaultMenuItem("Maturity Levels");
        item.setCommand("#{maturityLevelResultsChartViewBean.createHorizontalBarMaturityLevel}");
        item.setUpdate("idchart");
        item.setId("mMl");
        model.addElement(item);

        item = new DefaultMenuItem("Principles");
        item.setId("mPri");
        //itemP.setCommand("#");
        //item.setUpdate("idchart");
        model.addElement(item);
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

    public BeanManager getManager() {
        return manager;
    }

    public void setManager(BeanManager manager) {
        this.manager = manager;
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        //onload();
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

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

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

    public boolean isbSessionMLResults() {
        return bSessionMLResults;
    }

    public void setbSessionMLResults(boolean bSessionMLResults) {
        this.bSessionMLResults = bSessionMLResults;
    }
    
    
}
