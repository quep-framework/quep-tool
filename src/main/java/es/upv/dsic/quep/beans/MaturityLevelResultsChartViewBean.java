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
import es.upv.es.dsic.quep.utils.ResponseEstimate;
import es.upv.es.dsic.quep.utils.Result;
import es.upv.es.dsic.quep.utils.Results;
import java.io.Serializable;
import java.math.BigDecimal;
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
    
    public MaturityLevelResultsChartViewBean() {

    }

    @PostConstruct
    public void init() {
        oOrganization = organizationBean.getOrganization();                
        imenu = 1;
        createHorizontalBarMaturityLevel();
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
    }

    public void drawHorizontalBarModel(String sXAxis, String sYAxis, String title) {
        titleChar=title;        
        horizontalBarModel.clear();
        horizontalBarModel.addSeries(chartComplete);
        horizontalBarModel.addSeries(chartPerComplete);
        horizontalBarModel.setShowDatatip(true);
        horizontalBarModel.setShowPointLabels(true);
        horizontalBarModel.setTitle(titleChar);        
        horizontalBarModel.setAnimate(true);
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setZoom(true);
        horizontalBarModel.getAxis(AxisType.X).setLabel(sXAxis);
        horizontalBarModel.getAxis(AxisType.X).setMin(0);
        horizontalBarModel.getAxis(AxisType.X).setMax(100);
        horizontalBarModel.getAxis(AxisType.X).setTickAngle(25);
      
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
        drawHorizontalBarModel("Values", "Maturity Levels", oOrganization.getName());
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
        drawHorizontalBarModel("Values", "Principles", "Level: " + oMaturityLevel.getName());
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
    
    

}

/*public Map<Principle, ResponseEstimate> calculatePrinciples() {
        Map<Principle, MaturityLevelResultsChartViewBean.ResponseEstimate> mapPrincipleEstimate 
                = new HashMap<Principle, MaturityLevelResultsChartViewBean.ResponseEstimate>();
        
         mapSumPractices = new HashMap<Practice, ResponseEstimate>();
         mapSumPractices = calculatePractices(mapSumQuepQuestions);
         //if (mapSumPractices==0) mapSumPractices=1;

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();
        //PrincipleDaoImplement pdi = new PrincipleDaoImplement();

        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();

       // List<Principle> lstPrinciple = new ArrayList<Principle>();
       // lstPrinciple = pdi.getPrinciple();

        for (Principle pri : lstPrinciple) {
            MaturityLevelResultsChartViewBean.ResponseEstimate oREstimate = new MaturityLevelResultsChartViewBean.ResponseEstimate();
            BigDecimal avg = BigDecimal.ZERO;
            int size = 0;
            for (MaturityLevelPractice mlp : lstMaturityLevelPractice) {
                //if (mlp.getId().getIdMaturityLevel() == Integer.parseInt(sMaturityLevelId)) {
                    for (Map.Entry<Practice, MaturityLevelResultsChartViewBean.ResponseEstimate> mPra : mapSumPractices.entrySet()) {
                        Practice pra = mPra.getKey();
                        ResponseEstimate rsp = mPra.getValue();
                        if (pra.getId() == mlp.getId().getIdPractice()
                                && pri.getId() == mlp.getIdPrinciple()) {
                            avg = avg.add(rsp.getAvg());
                            size++;
                        }
                    }
               // }
            }
            if (size == 0) {
                size = 1;
            }
            oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
            mapPrincipleEstimate.put(pri, oREstimate);
        }
        return mapPrincipleEstimate;
    }*/
 /*
    public Map<Principle, Result> getResultsPrincipleByLevel(String sMaturityLevelId) {
        try {
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
    }*/
 /*
    public Map<MaturityLevel, ResponseEstimate> calculateMaturityLevel(Map<Practice, ResponseEstimate> mapSumPractices) {
        Map<MaturityLevel, ResponseEstimate> mapMaturityLevelEstimate = new HashMap<MaturityLevel, ResponseEstimate>();

        MaturityLevelDaoImplement mldao = new MaturityLevelDaoImplement();
        //PrincipleDaoImplement pdi = new PrincipleDaoImplement();

        List<MaturityLevelPractice> lstMaturityLevelPractice = new ArrayList<MaturityLevelPractice>();
        lstMaturityLevelPractice = mldao.getMaturityLevelsPractice();

        //List<MaturityLevel> lstMaturityLevel = new ArrayList<MaturityLevel>();
        //lstMaturityLevel = mldao.getMaturityLevels();

        ResponseEstimate oREstimate = new ResponseEstimate();

        BigDecimal sumLastLevel = BigDecimal.ZERO;
        int sizeLevels = 0;
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
                }
                if (size == 0) {
                    size = 1;
                }
                oREstimate.setAvg(avg.divide(BigDecimal.valueOf(size), 2, RoundingMode.HALF_EVEN));
                mapMaturityLevelEstimate.put(ml, oREstimate);

                sumLastLevel = sumLastLevel.add(avg);
                sizeLevels++;
            } else {
                oLastMaturityLevel = ml;
            }
        }

        //setting last level
        oREstimate = new ResponseEstimate();
        oREstimate.setAvg(sumLastLevel.divide(BigDecimal.valueOf(sizeLevels), 2, RoundingMode.HALF_EVEN));
        mapMaturityLevelEstimate.put(oLastMaturityLevel, oREstimate);

        return mapMaturityLevelEstimate;*/
