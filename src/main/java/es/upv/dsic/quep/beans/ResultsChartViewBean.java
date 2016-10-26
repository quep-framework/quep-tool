/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.beans;

import es.upv.dsic.quep.converters.MaturityLevelComparable;
import es.upv.dsic.quep.dao.MaturityLevelDao;
import es.upv.dsic.quep.dao.MaturityLevelDaoImplement;
import es.upv.dsic.quep.dao.ResultsDaoImplement;
import es.upv.dsic.quep.model.MaturityLevel;
import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.RoleStakeholder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.component.breadcrumb.BreadCrumb;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.component.panelgrid.PanelGrid;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.menu.DefaultMenuModel;
//import org.primefaces.model.menu.DefaultMenuItem;
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
    
    private MenuModel bMenu;
    
    private PanelGrid panel = new PanelGrid();

    @PostConstruct
    public void init() {
        oRoleStakeholder = (RoleStakeholder) AccessBean.getSessionObj("roleStakeholder");
        //model = null;
        createBarModels();
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }

    public void createBarModels() {
        createHorizontalBarMaturityLevel();
        //createPrincipleBarModel();
    }
   
    public void createHorizontalBarMaturityLevel() {
        setMenuModel(1);
        
        setHorizontalBarModel(new HorizontalBarChartModel());

        ChartSeries chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");
                
        ChartSeries chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");
        
        MaturityLevelDaoImplement mldi = new MaturityLevelDaoImplement();           
        lstMaturityLevel = new ArrayList<MaturityLevel>();
        lstMaturityLevel = mldi.getMaturityLevels();
        
        mapMaturityLevelResults = new HashMap<MaturityLevel, Result>();
        mapMaturityLevelResults = getResults(lstMaturityLevel);

        Collections.sort(lstMaturityLevel, MaturityLevelComparable.Comparators.ID);         
        
        for (MaturityLevel oMaturityLevel : lstMaturityLevel) {
            chartComplete.set(oMaturityLevel.getLevelAbbreviation()+". "+oMaturityLevel.getName(), mapMaturityLevelResults.get(oMaturityLevel).getComplete());
            
            chartPerComplete.set(oMaturityLevel.getLevelAbbreviation()+". "+oMaturityLevel.getName(), mapMaturityLevelResults.get(oMaturityLevel).getPerComplete());
        }
        
        getHorizontalBarModel().addSeries(chartComplete);
        getHorizontalBarModel().addSeries(chartPerComplete);

        getHorizontalBarModel().setTitle(oRoleStakeholder.getOrganization().getName());
        getHorizontalBarModel().setAnimate(true);
        getHorizontalBarModel().setLegendPosition("e");
        getHorizontalBarModel().setStacked(true);

        Axis xAxis = getHorizontalBarModel().getAxis(AxisType.X);
        xAxis.setLabel("Values");
        xAxis.setMin(0);
        xAxis.setMax(100);

        Axis yAxis = getHorizontalBarModel().getAxis(AxisType.Y);
        yAxis.setLabel("Maturity Levels");       
    }     
     
     public void createHorizontalBarPrinciple(String sPrinciple) {
        //setHorizontalBarModel(new HorizontalBarChartModel());
        setMenuModel(2);
        horizontalBarModel= new HorizontalBarChartModel();        

        ChartSeries chartComplete = new ChartSeries();
        chartComplete.setLabel("% complete");
                
        ChartSeries chartPerComplete = new ChartSeries();
        chartPerComplete.setLabel("% per complete");
        
        Principle oPrinciple = new  Principle();
        
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
     
      public Map<MaturityLevel, Result> getResults(List<MaturityLevel> lstMl ) {
        try {
            Map<MaturityLevel, Result> mapMlResults = new HashMap<MaturityLevel, Result>();             
            for (MaturityLevel oMl : lstMl) {
                Result oResult = new Result();
                Double dComplete=getResultsComplete(oMl);
                oResult.setComplete(dComplete);
                oResult.setPerComplete(getResultsPerComplete(dComplete));
                mapMlResults.put(oMl, oResult);
            }

            return mapMlResults;
        } catch (Exception e) {
            return null;
        }
    }          

      public void calcular(){
      ResultsDaoImplement rdi= new  ResultsDaoImplement();
      
       //1.Evaluation Responses
      //SUM REQ Weight
      List<QuepQuestionResponseOption> lstQQResponseOption=new ArrayList<QuepQuestionResponseOption>();
      lstQQResponseOption=rdi.getQuepQuestionResponseOption(oRoleStakeholder.getId().getIdOrganization());
      
       //2.Calculate Responses
         List<Response> lstResult=new ArrayList<Response>();
         lstResult=rdi.getListResponse(oRoleStakeholder.getId().getIdOrganization());
     
     
      Map<QuepQuestion,ResponseEstimate> mapSumResponseOp = new HashMap<QuepQuestion, ResponseEstimate>();  
          for (QuepQuestionResponseOption oQQRO : lstQQResponseOption) {
              ResponseEstimate oREstimate = new ResponseEstimate();
              Double sumReqQQ= new  Double(0.0);        //cambiar mapeo de int a double      
              Double sumQQ= new  Double(0.0);
              Double avg = new Double(0.0);
               for (QuepQuestionResponseOption oQQRO_aux : lstQQResponseOption) {
                   if (oQQRO_aux.getId().getIdQuepQuestion()==oQQRO.getId().getIdQuepQuestion()){
                       sumQQ=sumQQ+oQQRO_aux.getResponseOption().getWeight();
                       if (oQQRO_aux.getResponseOption().getIsRequiered()==1){
                           sumReqQQ=sumReqQQ+oQQRO_aux.getResponseOption().getWeight();
                       }
                   }
               }
               
              Double sumRsp = new Double(0.0);
              for (Response oResponse : lstResult) {
                  if (oResponse.getId().getIdQuepQuestion()==oQQRO.getId().getIdQuepQuestion()){
                      sumRsp=sumRsp+oResponse.getResponseOption().getWeight();
                  }
              }
               avg=sumRsp/sumReqQQ;
                  
               oREstimate.setSumRequiered(sumReqQQ);
               oREstimate.setSum(sumQQ);
               oREstimate.setAvg(avg);
               mapSumResponseOp.put(oQQRO.getQuepQuestion(),oREstimate);
          }
          
       
      }
      
    public Double getResultsPerComplete(Double dResult) {
        try {
            Double dResultPerComplete;
            dResultPerComplete=(-1) * (dResult - 100);
            return dResultPerComplete;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Double getResultsComplete(MaturityLevel oMl) {
        try {
            Double dResultPerComplete;
            dResultPerComplete=5.0;
            return dResultPerComplete;
        } catch (Exception e) {
            return null;
        }
    }
    
    public void listener(ItemSelectEvent e){        
        if(e.getItemIndex() == 0){
            createHorizontalBarPrinciple(String.valueOf(e.getSeriesIndex()));
        }else if(e.getItemIndex() == 1){
            
        }
    }


    public Double getL10(List lst) {
        try {
            double sum = 0;
            for (int i = 0; i < lst.size() - 1; i++) {
                sum = sum + Double.parseDouble(lst.get(i).toString());
            }
            return sum / lst.size();
        } catch (Exception e) {
            return null;
        }
    }

    public Double getL10PerComplete(double sumL10) {
        try {
            return (-1) * (sumL10 - 100);
        } catch (Exception e) {
            return null;
        }
    }
    
    BreadCrumb bc = new BreadCrumb();
     UIForm form = new UIForm();
    
    public void setMenuModel(int band) {
        if (band == 2) {
            //Map<String, String[]> requestParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();        
            //String[] lstRO = (String[])  requestParams.get("frmMenu:bc");
           
          // <p:ajax event="itemSelect" listener="#{resultsChartViewBean.listener}" update="idchart" process="@this"></p:ajax>
            UIMenuItem mp = new UIMenuItem();
            mp.setValue("Principles");
             AjaxBehavior aj= new AjaxBehavior();
        //aj.setUpdate("idchart");
       // aj.setProcess("@this");
            bc.getChildren().add(mp);
           // bc.addClientBehavior("itemSelect", aj);
            form.getChildren().add(bc);
            form.setSubmitted(true);
            panel.getChildren().add(form);
        }
        else{
        panel = new PanelGrid();
        panel.setId("pnlMain");

        form = new UIForm();
        form.setId("frmMenu");

        bc = new BreadCrumb();
        bc.setId("bc");

        UIMenuItem mi = new UIMenuItem();
        mi.setValue("Home");
        bc.getChildren().add(mi);

        UIMenuItem mml = new UIMenuItem();
        mml.setValue("Maturity Levels");
        bc.getChildren().add(mml);

        
        
       // AjaxBehavior aj= new AjaxBehavior();
       // aj.setUpdate("idchart");
        //aj.setProcess("@this");
          // <p:ajax event="itemSelect" listener="#{resultsChartViewBean.listener}" update="idchart" process="@this"></p:ajax>
        bc.isDynamic();
        //bc.addClientBehavior("itemSelect", aj);
        form.getChildren().add(bc);
        form.setSubmitted(true);
        panel.getChildren().add(form);
        }
    }

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

    public MenuModel getbMenu() {
        return bMenu;
    }

    public void setbMenu(MenuModel bMenu) {
        this.bMenu = bMenu;
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
    
    

    

}
