/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.QuestionnaireResponse;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface QuestionnaireResponseDao {
    
    public List<QuestionnaireResponse> getQuestionnaireResponses();
    public QuestionnaireResponse getQuestionnaireResponse(int id);
    public void insertQuestionnaireResponse(QuestionnaireResponse oQR);
    public void updateQuestionnaireResponse(QuestionnaireResponse oQR);
    public void deleteQuestionnaireResponse(QuestionnaireResponse oQR);
       
}
