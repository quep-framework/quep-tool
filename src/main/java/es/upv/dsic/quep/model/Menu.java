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
 * Menu generated by hbm2java
 */
@Entity
@Table(name="menu"
    ,schema="public"
)
public class Menu  implements java.io.Serializable {


     private int id;
     private Menu menu;
     private Role role;
     private String name;
     private Character type;
     private String creationUser;
     private Date creationDate;
     private String modificationUser;
     private Date moficationDate;
     private String audit;
     private int active;
     private String url;
     private Set<Menu> menus = new HashSet<Menu>(0);

    public Menu() {
    }

	
    public Menu(int id, Role role, String name, String creationUser, Date creationDate, String audit, int active) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.creationUser = creationUser;
        this.creationDate = creationDate;
        this.audit = audit;
        this.active = active;
    }
    public Menu(int id, Menu menu, Role role, String name, Character type, String creationUser, Date creationDate, String modificationUser, Date moficationDate, String audit, int active, String url, Set<Menu> menus) {
       this.id = id;
       this.menu = menu;
       this.role = role;
       this.name = name;
       this.type = type;
       this.creationUser = creationUser;
       this.creationDate = creationDate;
       this.modificationUser = modificationUser;
       this.moficationDate = moficationDate;
       this.audit = audit;
       this.active = active;
       this.url = url;
       this.menus = menus;
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
    @JoinColumn(name="id_submenu")
    public Menu getMenu() {
        return this.menu;
    }
    
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_role", nullable=false)
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }

    
    @Column(name="name", nullable=false)
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    @Column(name="type", length=1)
    public Character getType() {
        return this.type;
    }
    
    public void setType(Character type) {
        this.type = type;
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
    @Column(name="mofication_date", length=13)
    public Date getMoficationDate() {
        return this.moficationDate;
    }
    
    public void setMoficationDate(Date moficationDate) {
        this.moficationDate = moficationDate;
    }

    
    @Column(name="audit", nullable=false)
    public String getAudit() {
        return this.audit;
    }
    
    public void setAudit(String audit) {
        this.audit = audit;
    }

    
    @Column(name="active", nullable=false)
    public int getActive() {
        return this.active;
    }
    
    public void setActive(int active) {
        this.active = active;
    }

    
    @Column(name="url")
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="menu")
    public Set<Menu> getMenus() {
        return this.menus;
    }
    
    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }




}


