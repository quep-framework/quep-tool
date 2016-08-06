package es.upv.dsic.quep.model;
// Generated 04-ago-2016 14:00:00 by Hibernate Tools 4.3.1


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
 * MaturityLevelPractice generated by hbm2java
 */
@Entity
@Table(name="maturity_level_practice"
    ,schema="public"
)
public class MaturityLevelPractice  implements java.io.Serializable {


     private MaturityLevelPracticeId id;
     private MaturityLevel maturityLevel;
     private Practice practice;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private int idPrinciple;

    public MaturityLevelPractice() {
    }

	
    public MaturityLevelPractice(MaturityLevelPracticeId id, MaturityLevel maturityLevel, Practice practice, String creationUser, Date creationDate, int active, String audit, int idPrinciple) {
        this.id = id;
        this.maturityLevel = maturityLevel;
        this.practice = practice;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
        this.idPrinciple = idPrinciple;
    }
    public MaturityLevelPractice(MaturityLevelPracticeId id, MaturityLevel maturityLevel, Practice practice, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, int idPrinciple) {
       this.id = id;
       this.maturityLevel = maturityLevel;
       this.practice = practice;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.idPrinciple = idPrinciple;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idPractice", column=@Column(name="id_practice", nullable=false) ), 
        @AttributeOverride(name="idMaturityLevel", column=@Column(name="id_maturity_level", nullable=false) ) } )
    public MaturityLevelPracticeId getId() {
        return this.id;
    }
    
    public void setId(MaturityLevelPracticeId id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_maturity_level", nullable=false, insertable=false, updatable=false)
    public MaturityLevel getMaturityLevel() {
        return this.maturityLevel;
    }
    
    public void setMaturityLevel(MaturityLevel maturityLevel) {
        this.maturityLevel = maturityLevel;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_practice", nullable=false, insertable=false, updatable=false)
    public Practice getPractice() {
        return this.practice;
    }
    
    public void setPractice(Practice practice) {
        this.practice = practice;
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




}


