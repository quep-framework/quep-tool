/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Menu;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.Response;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface ResultsDao {    
   public List<QuepQuestionResponseOption> getQuepQuestionResponseOption(int idOrg);
   public List<Response> getListResponse(int idOrg);
   public int getLegendQrStatus(int idOrg,int iStatus) ;
}
