/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.dao;

import es.upv.dsic.quep.model.QuepQuestion;
import es.upv.dsic.quep.model.QuepQuestionResponseOption;
import es.upv.dsic.quep.model.QuepQuestionTechnique;
import java.util.List;

/**
 *
 * @author agna8685
 */
public interface QuepQuestionDao {

    public List<QuepQuestion> getLstQuepQuestion();

    public List<QuepQuestionResponseOption> getLstQQuestionResponseOption();

    public List<QuepQuestionTechnique> getLstQQuestionTechniques();
}
