package es.upv.dsic.quep.model;
// Generated 06-ago-2016 21:21:41 by Hibernate Tools 4.3.1


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
 * Practice generated by hbm2java
 */
@Entity
@Table(name="practice"
    ,schema="public"
)
public class Practice  implements java.io.Serializable {


     private int id;
     private Principle principle;
     private String abbreviation;
     private String name;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private Integer idTechnique;
     private Integer umbral;
     private Integer weight;
     private String audit;
     private String description;
     private Set<Technique> techniques = new HashSet<Technique>(0);
     private Set<Questionnaire> questionnaires = new HashSet<Questionnaire>(0);
     private Set<MaturityLevelPractice> maturityLevelPractices = new HashSet<MaturityLevelPractice>(0);
     private Set<RolePractice> rolePractices = new HashSet<RolePractice>(0);
     private Set<QuepQuestion> quepQuestions = new HashSet<QuepQuestion>(0);

    public Practice() {
    }

	
    public Practice(int id, Principle principle, String name, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.principle = principle;
        this.name = name;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public Practice(int id, Principle principle, String abbreviation, String name, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, Integer idTechnique, Integer umbral, Integer weight, String audit, String description, Set<Technique> techniques, Set<Questionnaire> questionnaires, Set<MaturityLevelPractice> maturityLevelPractices, Set<RolePractice> rolePractices, Set<QuepQuestion> quepQuestions) {
       this.id = id;
       this.principle = principle;
       this.abbreviation = abbreviation;
       this.name = name;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.idTechnique = idTechnique;
       this.umbral = umbral;
       this.weight = weight;
       this.audit = audit;
       this.description = description;
       this.techniques = techniques;
       this.questionnaires = questionnaires;
       this.maturityLevelPractices = maturityLevelPractices;
       this.rolePractices = rolePractices;
       this.quepQuestions = quepQuestions;
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
    @JoinColumn(name="id_principle", nullable=false)
    public Principle getPrinciple() {
        return this.principle;
    }
    
    public void setPrinciple(Principle principle) {
        this.principle = principle;
    }

    
    @Column(name="abbreviation")
    public String getAbbreviation() {
        return this.abbreviation;
    }
    
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    
    @Column(name="name", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
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

    
    @Column(name="id_technique")
    public Integer getIdTechnique() {
        return this.idTechnique;
    }
    
    public void setIdTechnique(Integer idTechnique) {
        this.idTechnique = idTechnique;
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

    
    @Column(name="audit", nullable=false)
    public String getAudit() {
        return this.audit;
    }
    
    public void setAudit(String audit) {
        this.audit = audit;
    }

    
    @Column(name="description")
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="practice")
    public Set<Technique> getTechniques() {
        return this.techniques;
    }
    
    public void setTechniques(Set<Technique> techniques) {
        this.techniques = techniques;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="practice")
    public Set<Questionnaire> getQuestionnaires() {
        return this.questionnaires;
    }
    
    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="practice")
    public Set<MaturityLevelPractice> getMaturityLevelPractices() {
        return this.maturityLevelPractices;
    }
    
    public void setMaturityLevelPractices(Set<MaturityLevelPractice> maturityLevelPractices) {
        this.maturityLevelPractices = maturityLevelPractices;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="practice")
    public Set<RolePractice> getRolePractices() {
        return this.rolePractices;
    }
    
    public void setRolePractices(Set<RolePractice> rolePractices) {
        this.rolePractices = rolePractices;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="practice")
    public Set<QuepQuestion> getQuepQuestions() {
        return this.quepQuestions;
    }
    
    public void setQuepQuestions(Set<QuepQuestion> quepQuestions) {
        this.quepQuestions = quepQuestions;
    }




}


