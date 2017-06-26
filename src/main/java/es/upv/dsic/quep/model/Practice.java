package es.upv.dsic.quep.model;
// Generated 20-oct-2016 20:22:12 by Hibernate Tools 4.3.1


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
     private BigDecimal umbral;
     private BigDecimal weight;
     private String audit;
     private String description;
     private Set<Technique> techniques = new HashSet<>(0);
     private Set<Questionnaire> questionnaires = new HashSet<>(0);
     private Set<MaturityLevelPractice> maturityLevelPractices = new HashSet<>(0);     
     private Set<QuepQuestion> quepQuestions = new HashSet<>(0);
     


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
    public Practice(int id, Principle principle, String abbreviation, String name, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, Integer idTechnique, BigDecimal umbral, BigDecimal weight, String audit, String description, Set<Technique> techniques, Set<Questionnaire> questionnaires, Set<MaturityLevelPractice> maturityLevelPractices, Set<QuepQuestion> quepQuestions) {
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
       this.quepQuestions = quepQuestions;
    }
   
     @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) //add to mapped
    
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

    
    @Column(name="umbral", precision=5)
    public BigDecimal getUmbral() {
        return this.umbral;
    }
    
    public void setUmbral(BigDecimal umbral) {
        this.umbral = umbral;
    }

    
    @Column(name="weight", precision=5)
    public BigDecimal getWeight() {
        return this.weight;
    }
    
    public void setWeight(BigDecimal weight) {
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
    public Set<QuepQuestion> getQuepQuestions() {
        return this.quepQuestions;
    }
    
    public void setQuepQuestions(Set<QuepQuestion> quepQuestions) {
        this.quepQuestions = quepQuestions;
    }


    public void calculateValue() {
        Iterator<QuepQuestion> iter = quepQuestions.iterator();
        while (iter.hasNext()) {
            QuepQuestion i = iter.next();
           // i.get
        }
    }

}


