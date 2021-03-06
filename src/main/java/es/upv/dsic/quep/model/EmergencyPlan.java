package es.upv.dsic.quep.model;
// Generated 15-sep-2017 22:47:04 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EmergencyPlan generated by hbm2java
 */
@Entity
@Table(name="emergency_plan"
    ,schema="public"
)
public class EmergencyPlan  implements java.io.Serializable {


     private int id;
     private String name;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private String description;
     private Set<Organization> organizations = new HashSet<Organization>(0);

    public EmergencyPlan() {
    }

	
    public EmergencyPlan(int id, String name, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.name = name;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public EmergencyPlan(int id, String name, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, String description, Set<Organization> organizations) {
       this.id = id;
       this.name = name;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.description = description;
       this.organizations = organizations;
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="emergencyPlan")
    public Set<Organization> getOrganizations() {
        return this.organizations;
    }
    
    public void setOrganizations(Set<Organization> organizations) {
        this.organizations = organizations;
    }




}


