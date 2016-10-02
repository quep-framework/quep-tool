package es.upv.dsic.quep.model;
// Generated 20-sep-2016 10:46:39 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QuepQuestionResilienceId generated by hbm2java
 */
@Embeddable
public class QuepQuestionResilienceId  implements java.io.Serializable {


     private int idResilience;
     private int idQuepQuestion;

    public QuepQuestionResilienceId() {
    }

    public QuepQuestionResilienceId(int idResilience, int idQuepQuestion) {
       this.idResilience = idResilience;
       this.idQuepQuestion = idQuepQuestion;
    }
   


    @Column(name="id_resilience", nullable=false)
    public int getIdResilience() {
        return this.idResilience;
    }
    
    public void setIdResilience(int idResilience) {
        this.idResilience = idResilience;
    }


    @Column(name="id_quep_question", nullable=false)
    public int getIdQuepQuestion() {
        return this.idQuepQuestion;
    }
    
    public void setIdQuepQuestion(int idQuepQuestion) {
        this.idQuepQuestion = idQuepQuestion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof QuepQuestionResilienceId) ) return false;
		 QuepQuestionResilienceId castOther = ( QuepQuestionResilienceId ) other; 
         
		 return (this.getIdResilience()==castOther.getIdResilience())
 && (this.getIdQuepQuestion()==castOther.getIdQuepQuestion());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdResilience();
         result = 37 * result + this.getIdQuepQuestion();
         return result;
   }   


}


