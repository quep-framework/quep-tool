package es.upv.dsic.quep.model;
// Generated 04-ago-2016 14:00:00 by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Principle generated by hbm2java
 */
@Entity
@Table(name="principle"
    ,schema="public"
)
public class Principle  implements java.io.Serializable {


     private int id;
     private String abbreviation;
     private String name;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private Integer umbral;
     private String audit;
     private Integer weight;
     private Set<Practice> practices = new HashSet<Practice>(0);

    public Principle() {
    }

	
    public Principle(int id, String abbreviation, String name, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.abbreviation = abbreviation;
        this.name = name;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public Principle(int id, String abbreviation, String name, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, Integer umbral, String audit, Integer weight, Set<Practice> practices) {
       this.id = id;
       this.abbreviation = abbreviation;
       this.name = name;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.umbral = umbral;
       this.audit = audit;
       this.weight = weight;
       this.practices = practices;
    }
   
     @Id 

    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="abbreviation", nullable=false)
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

    
    @Column(name="umbral", precision=5, scale=0)
    public Integer getUmbral() {
        return this.umbral;
    }
    
    public void setUmbral(Integer umbral) {
        this.umbral = umbral;
    }

    
    @Column(name="audit", nullable=false)
    public String getAudit() {
        return this.audit;
    }
    
    public void setAudit(String audit) {
        this.audit = audit;
    }

    
    @Column(name="weight", precision=5, scale=0)
    public Integer getWeight() {
        return this.weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="principle")
    public Set<Practice> getPractices() {
        return this.practices;
    }
    
    public void setPractices(Set<Practice> practices) {
        this.practices = practices;
    }




}


