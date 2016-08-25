package es.upv.dsic.quep.model;
// Generated 10-ago-2016 23:05:31 by Hibernate Tools 4.3.1


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
 * Role generated by hbm2java
 */
@Entity
@Table(name="role"
    ,schema="public"
)
public class Role  implements java.io.Serializable {


     private int id;
     private String name;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date modificationDate;
     private int active;
     private String audit;
     private Integer umbral;
     private Integer weight;
     private String abbreviation;
     private Set<RolePractice> rolePractices = new HashSet<RolePractice>(0);
     private Set<Menu> menus = new HashSet<Menu>(0);
     private Set<RoleStakeholder> roleStakeholders = new HashSet<RoleStakeholder>(0);

    public Role() {
    }

	
    public Role(int id, String name, String creationUser, Date creationDate, int active, String audit) {
        this.id = id;
        this.name = name;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.active = active;
        this.audit = audit;
    }
    public Role(int id, String name, String creationUser, Date creationDate, String modificationUser, Date modificationDate, int active, String audit, Integer umbral, Integer weight, String abbreviation, Set<RolePractice> rolePractices, Set<Menu> menus, Set<RoleStakeholder> roleStakeholders) {
       this.id = id;
       this.name = name;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.modificationDate = modificationDate;
       this.active = active;
       this.audit = audit;
       this.umbral = umbral;
       this.weight = weight;
       this.abbreviation = abbreviation;
       this.rolePractices = rolePractices;
       this.menus = menus;
       this.roleStakeholders = roleStakeholders;
    }
   
     @Id 

    
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

    
    @Column(name="abbreviation", length=4)
    public String getAbbreviation() {
        return this.abbreviation;
    }
    
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="role")
    public Set<RolePractice> getRolePractices() {
        return this.rolePractices;
    }
    
    public void setRolePractices(Set<RolePractice> rolePractices) {
        this.rolePractices = rolePractices;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="role")
    public Set<Menu> getMenus() {
        return this.menus;
    }
    
    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="role")
    public Set<RoleStakeholder> getRoleStakeholders() {
        return this.roleStakeholders;
    }
    
    public void setRoleStakeholders(Set<RoleStakeholder> roleStakeholders) {
        this.roleStakeholders = roleStakeholders;
    }




}


