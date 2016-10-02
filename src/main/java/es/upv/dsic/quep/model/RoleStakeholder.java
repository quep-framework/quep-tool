package es.upv.dsic.quep.model;
// Generated 20-sep-2016 10:46:39 by Hibernate Tools 4.3.1


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
 * RoleStakeholder generated by hbm2java
 */
@Entity
@Table(name="role_stakeholder"
    ,schema="public"
)
public class RoleStakeholder  implements java.io.Serializable {


     private RoleStakeholderId id;
     private Organization organization;
     private Role role;
     private Stakeholder stakeholder;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;

    public RoleStakeholder() {
    }

	
    public RoleStakeholder(RoleStakeholderId id, Organization organization, Role role, Stakeholder stakeholder, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.organization = organization;
        this.role = role;
        this.stakeholder = stakeholder;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public RoleStakeholder(RoleStakeholderId id, Organization organization, Role role, Stakeholder stakeholder, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit) {
       this.id = id;
       this.organization = organization;
       this.role = role;
       this.stakeholder = stakeholder;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idRole", column=@Column(name="id_role", nullable=false) ), 
        @AttributeOverride(name="idStakeholder", column=@Column(name="id_stakeholder", nullable=false) ), 
        @AttributeOverride(name="idOrganization", column=@Column(name="id_organization", nullable=false) ) } )
    public RoleStakeholderId getId() {
        return this.id;
    }
    
    public void setId(RoleStakeholderId id) {
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
    @JoinColumn(name="id_role", nullable=false, insertable=false, updatable=false)
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_stakeholder", nullable=false, insertable=false, updatable=false)
    public Stakeholder getStakeholder() {
        return this.stakeholder;
    }
    
    public void setStakeholder(Stakeholder stakeholder) {
        this.stakeholder = stakeholder;
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


