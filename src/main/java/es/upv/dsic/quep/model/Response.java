package es.upv.dsic.quep.model;
// Generated 21-oct-2016 22:49:47 by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Response generated by hbm2java
 */
@Entity
@Table(name="response"
    ,schema="public"
)
public class Response  implements java.io.Serializable {


     private ResponseId id;
     private QuestionnaireQuepQuestion questionnaireQuepQuestion;
     private QuestionnaireResponse questionnaireResponse;
     private ResponseOption responseOption;
     private Stakeholder stakeholder;
     private int idPrinciple;
     private String responseOption_1;
     private String comment;
     private String pagenumber;
     private Integer computedValue;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;

    public Response() {
    }

	
    public Response(ResponseId id, QuestionnaireQuepQuestion questionnaireQuepQuestion, QuestionnaireResponse questionnaireResponse, ResponseOption responseOption, Stakeholder stakeholder, int idPrinciple, String responseOption_1, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.questionnaireQuepQuestion = questionnaireQuepQuestion;
        this.questionnaireResponse = questionnaireResponse;
        this.responseOption = responseOption;
        this.stakeholder = stakeholder;
        this.idPrinciple = idPrinciple;
        this.responseOption_1 = responseOption_1;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public Response(ResponseId id, QuestionnaireQuepQuestion questionnaireQuepQuestion, QuestionnaireResponse questionnaireResponse, ResponseOption responseOption, Stakeholder stakeholder, int idPrinciple, String responseOption_1, String comment, String pagenumber, Integer computedValue, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit) {
       this.id = id;
       this.questionnaireQuepQuestion = questionnaireQuepQuestion;
       this.questionnaireResponse = questionnaireResponse;
       this.responseOption = responseOption;
       this.stakeholder = stakeholder;
       this.idPrinciple = idPrinciple;
       this.responseOption_1 = responseOption_1;
       this.comment = comment;
       this.pagenumber = pagenumber;
       this.computedValue = computedValue;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idStakeholder", column=@Column(name="id_stakeholder", nullable=false) ), 
        @AttributeOverride(name="idQuepQuestion", column=@Column(name="id_quep_question", nullable=false) ), 
        @AttributeOverride(name="idPractice", column=@Column(name="id_practice", nullable=false) ), 
        @AttributeOverride(name="idQuestionnaire", column=@Column(name="id_questionnaire", nullable=false) ), 
        @AttributeOverride(name="idRole", column=@Column(name="id_role", nullable=false) ), 
        @AttributeOverride(name="idOrganization", column=@Column(name="id_organization", nullable=false) ), 
        @AttributeOverride(name="idResponseOption", column=@Column(name="id_response_option", nullable=false) ) } )
    public ResponseId getId() {
        return this.id;
    }
    
    public void setId(ResponseId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns( { 
        @JoinColumn(name="id_quep_question", referencedColumnName="id_quep_question", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_questionnaire", referencedColumnName="id_questionnaire", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_practice", referencedColumnName="id_practice", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_role", referencedColumnName="id_role", nullable=false, insertable=false, updatable=false) } )
    public QuestionnaireQuepQuestion getQuestionnaireQuepQuestion() {
        return this.questionnaireQuepQuestion;
    }
    
    public void setQuestionnaireQuepQuestion(QuestionnaireQuepQuestion questionnaireQuepQuestion) {
        this.questionnaireQuepQuestion = questionnaireQuepQuestion;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns( { 
        @JoinColumn(name="id_questionnaire", referencedColumnName="id_questionnaire", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_practice", referencedColumnName="id_practice", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_stakeholder", referencedColumnName="id_stakeholder", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_role", referencedColumnName="id_role", nullable=false, insertable=false, updatable=false), 
        @JoinColumn(name="id_organization", referencedColumnName="id_organization", nullable=false, insertable=false, updatable=false) } )
    public QuestionnaireResponse getQuestionnaireResponse() {
        return this.questionnaireResponse;
    }
    
    public void setQuestionnaireResponse(QuestionnaireResponse questionnaireResponse) {
        this.questionnaireResponse = questionnaireResponse;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_response_option", nullable=false, insertable=false, updatable=false)
    public ResponseOption getResponseOption() {
        return this.responseOption;
    }
    
    public void setResponseOption(ResponseOption responseOption) {
        this.responseOption = responseOption;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_stakeholder", nullable=false, insertable=false, updatable=false)
    public Stakeholder getStakeholder() {
        return this.stakeholder;
    }
    
    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
    }

    
    @Column(name="id_principle", nullable=false)
    public int getIdPrinciple() {
        return this.idPrinciple;
    }
    
    public void setIdPrinciple(int idPrinciple) {
        this.idPrinciple = idPrinciple;
    }

    
    @Column(name="response_option", nullable=false)
    public String getResponseOption_1() {
        return this.responseOption_1;
    }
    
    public void setResponseOption_1(String responseOption_1) {
        this.responseOption_1 = responseOption_1;
    }

    
    @Column(name="comment")
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }

    
    @Column(name="pagenumber")
    public String getPagenumber() {
        return this.pagenumber;
    }
    
    public void setPagenumber(String pagenumber) {
        this.pagenumber = pagenumber;
    }

    
    @Column(name="computed_value", precision=5, scale=0)
    public Integer getComputedValue() {
        return this.computedValue;
    }
    
    public void setComputedValue(Integer computedValue) {
        this.computedValue = computedValue;
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




}


