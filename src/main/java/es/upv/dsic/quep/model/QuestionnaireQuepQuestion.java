package es.upv.dsic.quep.model;
// Generated 10-ago-2016 23:05:31 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * QuestionnaireQuepQuestion generated by hbm2java
 */
@Entity
@Table(name="questionnaire_quep_question"
    ,schema="public"
)
public class QuestionnaireQuepQuestion  implements java.io.Serializable {


     private QuestionnaireQuepQuestionId id;
     private QuepQuestion quepQuestion;
     private Questionnaire questionnaire;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private int idPrinciple;
     private Set<Response> responses = new HashSet<Response>(0);

    public QuestionnaireQuepQuestion() {
    }

	
    public QuestionnaireQuepQuestion(QuestionnaireQuepQuestionId id, QuepQuestion quepQuestion, Questionnaire questionnaire, String creationUser, Date creationDate, int active, String audit, int idPrinciple) {
        this.id = id;
        this.quepQuestion = quepQuestion;
        this.questionnaire = questionnaire;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
        this.idPrinciple = idPrinciple;
    }
    public QuestionnaireQuepQuestion(QuestionnaireQuepQuestionId id, QuepQuestion quepQuestion, Questionnaire questionnaire, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, int idPrinciple, Set<Response> responses) {
       this.id = id;
       this.quepQuestion = quepQuestion;
       this.questionnaire = questionnaire;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.idPrinciple = idPrinciple;
       this.responses = responses;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idQuepQuestion", column=@Column(name="id_quep_question", nullable=false) ), 
        @AttributeOverride(name="idQuestionnaire", column=@Column(name="id_questionnaire", nullable=false) ), 
        @AttributeOverride(name="idPractice", column=@Column(name="id_practice", nullable=false) ), 
        @AttributeOverride(name="idRole", column=@Column(name="id_role", nullable=false) ) } )
    public QuestionnaireQuepQuestionId getId() {
        return this.id;
    }
    
    public void setId(QuestionnaireQuepQuestionId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_quep_question", nullable=false, insertable=false, updatable=false)
    public QuepQuestion getQuepQuestion() {
        return this.quepQuestion;
    }
    
    public void setQuepQuestion(QuepQuestion quepQuestion) {
        this.quepQuestion = quepQuestion;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns( { 
        @JoinColumn(name="id_questionnaire", referencedColumnName="id", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_practice", referencedColumnName="id_practice", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_role", referencedColumnName="id_role", nullable=false, insertable=false, updatable=false) } )
    public Questionnaire getQuestionnaire() {
        return this.questionnaire;
    }
    
    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    
    @Column(name="creation_user", nullable=false)
    public String getCreationUser() {
        return this.creationUser;
    }
    
    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="creation_date", nullable=false, length=13)
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    
    @Column(name="modification_user")
    public String getModificationUser() {
        return this.modificationUser;
    }
    
    public void setModificationUser(String modificationUser) {
        this.modificationUser = modificationUser;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="modification_date", length=13)
    public Date getModificationDate() {
        return this.modificationDate;
    }
    
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    
    @Column(name="active", nullable=false)
    public int getActive() {
        return this.active;
    }
    
    public void setActive(int active) {
        this.active = active;
    }

    
    @Column(name="audit", nullable=false)
    public String getAudit() {
        return this.audit;
    }
    
    public void setAudit(String audit) {
        this.audit = audit;
    }

    
    @Column(name="id_principle", nullable=false)
    public int getIdPrinciple() {
        return this.idPrinciple;
    }
    
    public void setIdPrinciple(int idPrinciple) {
        this.idPrinciple = idPrinciple;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="questionnaireQuepQuestion")
    public Set<Response> getResponses() {
        return this.responses;
    }
    
    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }




}

