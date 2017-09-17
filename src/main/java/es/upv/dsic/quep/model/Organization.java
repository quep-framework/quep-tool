package es.upv.dsic.quep.model;
// Generated 20-oct-2016 20:22:12 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
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
 * Organization generated by hbm2java
 */
@Entity
@Table(name="organization"
    ,schema="public"
)
public class Organization  implements java.io.Serializable {


     private int id;
     private EmergencyPlan emergencyPlan;
     private String name;
     private String locate;
     private String adress;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private Set<QuestionnaireResponse> questionnaireResponses = new HashSet<QuestionnaireResponse>(0);
     private Set<RoleStakeholder> roleStakeholders = new HashSet<RoleStakeholder>(0);
     private Set<Stakeholder> stakeholders = new HashSet<Stakeholder>(0);

    public Organization() {
    }

	
    public Organization(int id,  EmergencyPlan emergencyPlan,  String name, String adress, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.emergencyPlan = emergencyPlan;
        this.name = name;
        this.adress = adress;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public Organization(int id,  EmergencyPlan emergencyPlan, String name, String locate, String adress, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, Set<QuestionnaireResponse> questionnaireResponses, Set<RoleStakeholder> roleStakeholders, Set<Stakeholder> stakeholders) {
       this.id = id;
       this.emergencyPlan = emergencyPlan;
       this.name = name;
       this.locate = locate;
       this.adress = adress;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.questionnaireResponses = questionnaireResponses;
       this.roleStakeholders = roleStakeholders;
       this.stakeholders = stakeholders;
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
    @JoinColumn(name="id_emergency_plan", nullable=false)
    public EmergencyPlan getEmergencyPlan() {
        return this.emergencyPlan;
    }
    
    public void setEmergencyPlan(EmergencyPlan emergencyPlan) {
        this.emergencyPlan = emergencyPlan;
    }
    

    
    @Column(name="name", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="locate")
    public String getLocate() {
        return this.locate;
    }
    
    public void setLocate(String locate) {
        this.locate = locate;
    }

    
    @Column(name="adress", nullable=false)
    public String getAdress() {
        return this.adress;
    }
    
    public void setAdress(String adress) {
        this.adress = adress;
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="organization")
    public Set<QuestionnaireResponse> getQuestionnaireResponses() {
        return this.questionnaireResponses;
    }
    
    public void setQuestionnaireResponses(Set<QuestionnaireResponse> questionnaireResponses) {
        this.questionnaireResponses = questionnaireResponses;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="organization")
    public Set<RoleStakeholder> getRoleStakeholders() {
        return this.roleStakeholders;
    }
    
    public void setRoleStakeholders(Set<RoleStakeholder> roleStakeholders) {
        this.roleStakeholders = roleStakeholders;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="organization")
    public Set<Stakeholder> getStakeholders() {
        return this.stakeholders;
    }
    
    public void setStakeholders(Set<Stakeholder> stakeholders) {
        this.stakeholders = stakeholders;
    }




}


