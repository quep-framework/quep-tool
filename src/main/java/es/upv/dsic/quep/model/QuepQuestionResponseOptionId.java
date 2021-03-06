package es.upv.dsic.quep.model;
// Generated 20-oct-2016 20:22:12 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QuepQuestionResponseOptionId generated by hbm2java
 */
@Embeddable
public class QuepQuestionResponseOptionId  implements java.io.Serializable {


     private int idQuepQuestion;
     private int idResponseOption;

    public QuepQuestionResponseOptionId() {
    }

    public QuepQuestionResponseOptionId(int idQuepQuestion, int idResponseOption) {
       this.idQuepQuestion = idQuepQuestion;
       this.idResponseOption = idResponseOption;
    }
   


    @Column(name="id_quep_question", nullable=false)
    public int getIdQuepQuestion() {
        return this.idQuepQuestion;
    }
    
    public void setIdQuepQuestion(int idQuepQuestion) {
        this.idQuepQuestion = idQuepQuestion;
    }


    @Column(name="id_response_option", nullable=false)
    public int getIdResponseOption() {
        return this.idResponseOption;
    }
    
    public void setIdResponseOption(int idResponseOption) {
        this.idResponseOption = idResponseOption;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof QuepQuestionResponseOptionId) ) return false;
		 QuepQuestionResponseOptionId castOther = ( QuepQuestionResponseOptionId ) other; 
         
		 return (this.getIdQuepQuestion()==castOther.getIdQuepQuestion())
 && (this.getIdResponseOption()==castOther.getIdResponseOption());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdQuepQuestion();
         result = 37 * result + this.getIdResponseOption();
         return result;
   }   


}


