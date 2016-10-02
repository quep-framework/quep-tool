package es.upv.dsic.quep.model;
// Generated 20-sep-2016 10:46:39 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ResponseId generated by hbm2java
 */
@Embeddable
public class ResponseId  implements java.io.Serializable {


     private int idStakeholder;
     private int idQuepQuestion;
     private int idPractice;
     private int idQuestionnaire;
     private int idRole;
     private int idOrganization;

    public ResponseId() {
    }

    public ResponseId(int idStakeholder, int idQuepQuestion, int idPractice, int idQuestionnaire, int idRole, int idOrganization) {
       this.idStakeholder = idStakeholder;
       this.idQuepQuestion = idQuepQuestion;
       this.idPractice = idPractice;
       this.idQuestionnaire = idQuestionnaire;
       this.idRole = idRole;
       this.idOrganization = idOrganization;
    }
   


    @Column(name="id_stakeholder", nullable=false)
    public int getIdStakeholder() {
        return this.idStakeholder;
    }
    
    public void setIdStakeholder(int idStakeholder) {
        this.idStakeholder = idStakeholder;
    }


    @Column(name="id_quep_question", nullable=false)
    public int getIdQuepQuestion() {
        return this.idQuepQuestion;
    }
    
    public void setIdQuepQuestion(int idQuepQuestion) {
        this.idQuepQuestion = idQuepQuestion;
    }


    @Column(name="id_practice", nullable=false)
    public int getIdPractice() {
        return this.idPractice;
    }
    
    public void setIdPractice(int idPractice) {
        this.idPractice = idPractice;
    }


    @Column(name="id_questionnaire", nullable=false)
    public int getIdQuestionnaire() {
        return this.idQuestionnaire;
    }
    
    public void setIdQuestionnaire(int idQuestionnaire) {
        this.idQuestionnaire = idQuestionnaire;
    }


    @Column(name="id_role", nullable=false)
    public int getIdRole() {
        return this.idRole;
    }
    
    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }


    @Column(name="id_organization", nullable=false)
    public int getIdOrganization() {
        return this.idOrganization;
    }
    
    public void setIdOrganization(int idOrganization) {
        this.idOrganization = idOrganization;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof ResponseId) ) return false;
		 ResponseId castOther = ( ResponseId ) other; 
         
		 return (this.getIdStakeholder()==castOther.getIdStakeholder())
 && (this.getIdQuepQuestion()==castOther.getIdQuepQuestion())
 && (this.getIdPractice()==castOther.getIdPractice())
 && (this.getIdQuestionnaire()==castOther.getIdQuestionnaire())
 && (this.getIdRole()==castOther.getIdRole())
 && (this.getIdOrganization()==castOther.getIdOrganization());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdStakeholder();
         result = 37 * result + this.getIdQuepQuestion();
         result = 37 * result + this.getIdPractice();
         result = 37 * result + this.getIdQuestionnaire();
         result = 37 * result + this.getIdRole();
         result = 37 * result + this.getIdOrganization();
         return result;
   }   


}


