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
 * Stakeholder generated by hbm2java
 */
@Entity
@Table(name="stakeholder"
    ,schema="public"
)
public class Stakeholder  implements java.io.Serializable {


     private int id;
     private Organization organization;
     private String name;
     private String lastName;
     private String email;
     private String password;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private Set<RoleStakeholder> roleStakeholders = new HashSet<RoleStakeholder>(0);
     private Set<Response> responses = new HashSet<Response>(0);

    public Stakeholder() {
    }

	
    public Stakeholder(int id, String name, String lastName, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public Stakeholder(int id, Organization organization, String name, String lastName, String email, String password, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, Set<RoleStakeholder> roleStakeholders, Set<Response> responses) {
       this.id = id;
       this.organization = organization;
       this.name = name;
       this.lastName = lastName;
       this.email = email;
       this.password = password;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.roleStakeholders = roleStakeholders;
       this.responses = responses;
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
    @JoinColumn(name="id_organization")
    public Organization getOrganization() {
        return this.organization;
    }
    
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    
    @Column(name="name", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="last_name", nullable=false)
    public String getLastName() {
        return this.lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    
    @Column(name="email")
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    
    @Column(name="password")
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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

@OneToMany(fetch=FetchType.LAZY, mappedBy="stakeholder")
    public Set<RoleStakeholder> getRoleStakeholders() {
        return this.roleStakeholders;
    }
    
    public void setRoleStakeholders(Set<RoleStakeholder> roleStakeholders) {
        this.roleStakeholders = roleStakeholders;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="stakeholder")
    public Set<Response> getResponses() {
        return this.responses;
    }
    
    public void setResponses(Set<Response> responses) {
        this.responses = responses;
    }

}


