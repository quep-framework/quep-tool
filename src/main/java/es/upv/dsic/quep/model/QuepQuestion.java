package es.upv.dsic.quep.model;
// Generated 10-ago-2016 23:05:31 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * QuepQuestion generated by hbm2java
 */
@Entity
@Table(name="quep_question"
    ,schema="public"
)
public class QuepQuestion  implements java.io.Serializable {


     private int id;
     private Practice practice;
     private QuestionType questionType;
     private int idPrinciple;
     private String description;
     private String tip;
     private int hasComment;
     private int hasPageNumber;
     private int isMandatory;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private Integer umbral;
     private Integer weight;
     private Set<QuepQuestionResilience> quepQuestionResiliences = new HashSet<QuepQuestionResilience>(0);
     private Set<QuepQuestionTechnique> quepQuestionTechniques = new HashSet<QuepQuestionTechnique>(0);
     private Set<QuepQuestionResponseOption> quepQuestionResponseOptions = new HashSet<QuepQuestionResponseOption>(0);
     private Set<QuestionnaireQuepQuestion> questionnaireQuepQuestions = new HashSet<QuestionnaireQuepQuestion>(0);

    public QuepQuestion() {
    }

	
    public QuepQuestion(int id, Practice practice, QuestionType questionType, int idPrinciple, String description, int hasComment, int hasPageNumber, int isMandatory, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.practice = practice;
        this.questionType = questionType;
        this.idPrinciple = idPrinciple;
        this.description = description;
        this.hasComment = hasComment;
        this.hasPageNumber = hasPageNumber;
        this.isMandatory = isMandatory;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public QuepQuestion(int id, Practice practice, QuestionType questionType, int idPrinciple, String description, String tip, int hasComment, int hasPageNumber, int isMandatory, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, Integer umbral, Integer weight, Set<QuepQuestionResilience> quepQuestionResiliences, Set<QuepQuestionTechnique> quepQuestionTechniques, Set<QuepQuestionResponseOption> quepQuestionResponseOptions, Set<QuestionnaireQuepQuestion> questionnaireQuepQuestions) {
       this.id = id;
       this.practice = practice;
       this.questionType = questionType;
       this.idPrinciple = idPrinciple;
       this.description = description;
       this.tip = tip;
       this.hasComment = hasComment;
       this.hasPageNumber = hasPageNumber;
       this.isMandatory = isMandatory;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.umbral = umbral;
       this.weight = weight;
       this.quepQuestionResiliences = quepQuestionResiliences;
       this.quepQuestionTechniques = quepQuestionTechniques;
       this.quepQuestionResponseOptions = quepQuestionResponseOptions;
       this.questionnaireQuepQuestions = questionnaireQuepQuestions;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_practice", nullable=false)
    public Practice getPractice() {
        return this.practice;
    }
    
    public void setPractice(Practice practice) {
        this.practice = practice;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_question_type", nullable=false)
    public QuestionType getQuestionType() {
        return this.questionType;
    }
    
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    
    @Column(name="id_principle", nullable=false)
    public int getIdPrinciple() {
        return this.idPrinciple;
    }
    
    public void setIdPrinciple(int idPrinciple) {
        this.idPrinciple = idPrinciple;
    }

    
    @Column(name="description", nullable=false)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    
    @Column(name="tip")
    public String getTip() {
        return this.tip;
    }
    
    public void setTip(String tip) {
        this.tip = tip;
    }

    
    @Column(name="has_comment", nullable=false)
    public int getHasComment() {
        return this.hasComment;
    }
    
    public void setHasComment(int hasComment) {
        this.hasComment = hasComment;
    }

    
    @Column(name="has_page_number", nullable=false)
    public int getHasPageNumber() {
        return this.hasPageNumber;
    }
    
    public void setHasPageNumber(int hasPageNumber) {
        this.hasPageNumber = hasPageNumber;
    }

    
    @Column(name="is_mandatory", nullable=false)
    public int getIsMandatory() {
        return this.isMandatory;
    }
    
    public void setIsMandatory(int isMandatory) {
        this.isMandatory = isMandatory;
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

    
    @Column(name="umbral", precision=5, scale=0)
    public Integer getUmbral() {
        return this.umbral;
    }
    
    public void setUmbral(Integer umbral) {
        this.umbral = umbral;
    }

    
    @Column(name="weight", precision=5, scale=0)
    public Integer getWeight() {
        return this.weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="quepQuestion")
    public Set<QuepQuestionResilience> getQuepQuestionResiliences() {
        return this.quepQuestionResiliences;
    }
    
    public void setQuepQuestionResiliences(Set<QuepQuestionResilience> quepQuestionResiliences) {
        this.quepQuestionResiliences = quepQuestionResiliences;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="quepQuestion")
    public Set<QuepQuestionTechnique> getQuepQuestionTechniques() {
        return this.quepQuestionTechniques;
    }
    
    public void setQuepQuestionTechniques(Set<QuepQuestionTechnique> quepQuestionTechniques) {
        this.quepQuestionTechniques = quepQuestionTechniques;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="quepQuestion")
    public Set<QuepQuestionResponseOption> getQuepQuestionResponseOptions() {
        return this.quepQuestionResponseOptions;
    }
    
    public void setQuepQuestionResponseOptions(Set<QuepQuestionResponseOption> quepQuestionResponseOptions) {
        this.quepQuestionResponseOptions = quepQuestionResponseOptions;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="quepQuestion")
    public Set<QuestionnaireQuepQuestion> getQuestionnaireQuepQuestions() {
        return this.questionnaireQuepQuestions;
    }
    
    public void setQuestionnaireQuepQuestions(Set<QuestionnaireQuepQuestion> questionnaireQuepQuestions) {
        this.questionnaireQuepQuestions = questionnaireQuepQuestions;
    }




}

