package es.upv.dsic.quep.model;
// Generated 10-ago-2016 23:05:31 by Hibernate Tools 4.3.1


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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * QuestionnaireResponse generated by hbm2java
 */
@Entity
@Table(name="questionnaire_response"
    ,schema="public"
)
public class QuestionnaireResponse  implements java.io.Serializable {


     private QuestionnaireResponseId id;
     private Organization organization;
     private Questionnaire questionnaire;
     private Response response;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private int idPrinciple;
     private Integer computedValue;

    public QuestionnaireResponse() {
    }

	
    public QuestionnaireResponse(Organization organization, Questionnaire questionnaire, Response response, String creationUser, Date creationDate, int active, String audit, int idPrinciple) {
        this.organization = organization;
        this.questionnaire = questionnaire;
        this.response = response;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
        this.idPrinciple = idPrinciple;
    }
    public QuestionnaireResponse(Organization organization, Questionnaire questionnaire, Response response, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, int idPrinciple, Integer computedValue) {
       this.organization = organization;
       this.questionnaire = questionnaire;
       this.response = response;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.idPrinciple = idPrinciple;
       this.computedValue = computedValue;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idQuestionnaire", column=@Column(name="id_questionnaire", nullable=false) ), 
        @AttributeOverride(name="idPractice", column=@Column(name="id_practice", nullable=false) ), 
        @AttributeOverride(name="idRole", column=@Column(name="id_role", nullable=false) ), 
        @AttributeOverride(name="idStakeholder", column=@Column(name="id_stakeholder", nullable=false) ), 
        @AttributeOverride(name="idQuepQuestion", column=@Column(name="id_quep_question", nullable=false) ), 
        @AttributeOverride(name="idOrganization", column=@Column(name="id_organization", nullable=false) ) } )
    public QuestionnaireResponseId getId() {
        return this.id;
    }
    
    public void setId(QuestionnaireResponseId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_organization", nullable=false, insertable=false, updatable=false)
    public Organization getOrganization() {
        return this.organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
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

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Response getResponse() {
        return this.response;
    }
    
    public void setResponse(Response response) {
        this.response = response;
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

    
    @Column(name="computed_value", precision=5, scale=0)
    public Integer getComputedValue() {
        return this.computedValue;
    }
    
    public void setComputedValue(Integer computedValue) {
        this.computedValue = computedValue;
    }




}

