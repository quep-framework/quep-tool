/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.Principle;
import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireQuepQuestion;
import es.upv.dsic.quep.model.QuestionnaireResponse;
import es.upv.dsic.quep.model.Response;
import es.upv.dsic.quep.model.ResponseOption;
import java.util.List;
import java.util.Map;

/**
 *
 * @author agna8685
 */
public interface QuestionnaireQQDao {
    
    public List<QuestionnaireQuepQuestion> getQuestionnairesQQbyRole(int idRole);

    public List<QuestionnaireQuepQuestion> getQuestionnairesQQRoleSthk(int idRole, int idStk);
    
    public List<QuestionnaireResponse> getQuestionnaireResponse(int idRole,int idStk);

    public List<ResponseOption> getResponseOptions(int idqq);

    public ResponseOption getResponseOption(int idqq, int idro);

    public Map<Integer, String> insertResponse(List<Response> lstResponse);

    public Map<Integer, String> insertQuestionnaireResponse(List<QuestionnaireResponse> lstQResponse);    

    public List<Principle> getPrinciples(int idRole, int idStk);
    
    
}
