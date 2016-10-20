package es.upv.dsic.quep.model;
// Generated 20-oct-2016 20:22:12 by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * QuepQuestionTechnique generated by hbm2java
 */
@Entity
@Table(name="quep_question_technique"
    ,schema="public"
)
public class QuepQuestionTechnique  implements java.io.Serializable {


     private QuepQuestionTechniqueId id;
     private QuepQuestion quepQuestion;
     private Technique technique;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;

    public QuepQuestionTechnique() {
    }

	
    public QuepQuestionTechnique(QuepQuestionTechniqueId id, QuepQuestion quepQuestion, Technique technique, String creationUser, String modificationUser, int active, String audit) {
        this.id = id;
        this.quepQuestion = quepQuestion;
        this.technique = technique;
        this.creationUser = creationUser;
        this.modificationUser = modificationUser;
        this.active = active;
        this.audit = audit;
    }
    public QuepQuestionTechnique(QuepQuestionTechniqueId id, QuepQuestion quepQuestion, Technique technique, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit) {
       this.id = id;
       this.quepQuestion = quepQuestion;
       this.technique = technique;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idTechnique", column=@Column(name="id_technique", nullable=false) ), 
        @AttributeOverride(name="idQuestion", column=@Column(name="id_question", nullable=false) ) } )
    public QuepQuestionTechniqueId getId() {
        return this.id;
    }
    
    public void setId(QuepQuestionTechniqueId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_question", nullable=false, insertable=false, updatable=false)
    public QuepQuestion getQuepQuestion() {
        return this.quepQuestion;
    }
    
    public void setQuepQuestion(QuepQuestion quepQuestion) {
        this.quepQuestion = quepQuestion;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_technique", nullable=false, insertable=false, updatable=false)
    public Technique getTechnique() {
        return this.technique;
    }
    
    public void setTechnique(Technique technique) {
        this.technique = technique;
    }

    
    @Column(name="creation_user", nullable=false)
    public String getCreationUser() {
        return this.creationUser;
    }
    
    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="creation_date", length=13)
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    
    @Column(name="modification_user", nullable=false)
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


