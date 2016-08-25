package es.upv.dsic.quep.model;
// Generated 10-ago-2016 23:05:31 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QuestionnaireQuepQuestionId generated by hbm2java
 */
@Embeddable
public class QuestionnaireQuepQuestionId  implements java.io.Serializable {


     private int idQuepQuestion;
     private int idQuestionnaire;
     private int idPractice;
     private int idRole;

    public QuestionnaireQuepQuestionId() {
    }

    public QuestionnaireQuepQuestionId(int idQuepQuestion, int idQuestionnaire, int idPractice, int idRole) {
       this.idQuepQuestion = idQuepQuestion;
       this.idQuestionnaire = idQuestionnaire;
       this.idPractice = idPractice;
       this.idRole = idRole;
    }
   


    @Column(name="id_quep_question", nullable=false)
    public int getIdQuepQuestion() {
        return this.idQuepQuestion;
    }
    
    public void setIdQuepQuestion(int idQuepQuestion) {
        this.idQuepQuestion = idQuepQuestion;
    }


    @Column(name="id_questionnaire", nullable=false)
    public int getIdQuestionnaire() {
        return this.idQuestionnaire;
    }
    
    public void setIdQuestionnaire(int idQuestionnaire) {
        this.idQuestionnaire = idQuestionnaire;
    }


    @Column(name="id_practice", nullable=false)
    public int getIdPractice() {
        return this.idPractice;
    }
    
    public void setIdPractice(int idPractice) {
        this.idPractice = idPractice;
    }


    @Column(name="id_role", nullable=false)
    public int getIdRole() {
        return this.idRole;
    }
    
    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof QuestionnaireQuepQuestionId) ) return false;
		 QuestionnaireQuepQuestionId castOther = ( QuestionnaireQuepQuestionId ) other; 
         
		 return (this.getIdQuepQuestion()==castOther.getIdQuepQuestion())
 && (this.getIdQuestionnaire()==castOther.getIdQuestionnaire())
 && (this.getIdPractice()==castOther.getIdPractice())
 && (this.getIdRole()==castOther.getIdRole());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdQuepQuestion();
         result = 37 * result + this.getIdQuestionnaire();
         result = 37 * result + this.getIdPractice();
         result = 37 * result + this.getIdRole();
         return result;
   }   


}


