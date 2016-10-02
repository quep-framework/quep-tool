package es.upv.dsic.quep.model;
// Generated 20-sep-2016 10:46:39 by Hibernate Tools 4.3.1

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
 * MaturityLevel generated by hbm2java
 */
@Entity
@Table(name = "maturity_level", schema = "public"
)
public class MaturityLevel implements java.io.Serializable {

    private int id;
    private String levelAbbreviation;
    private String name;
    private String description;
    private String type;
    private String creationUser;
    private Date fechaCreado;
    private String actualizado;
    private Date modificationDate;
    private int active;
    private String audit;
    private Integer umbral;
    private Integer weight;
    private Set<MaturityLevelPractice> maturityLevelPractices = new HashSet<MaturityLevelPractice>(0);

    public MaturityLevel() {
    }

    public MaturityLevel(int id, String levelAbbreviation, String name, String creationUser, Date fechaCreado, int active, String audit) {
        this.id = id;
        this.levelAbbreviation = levelAbbreviation;
        this.name = name;
        this.creationUser = creationUser;
        this.fechaCreado = fechaCreado;
        this.active = active;
        this.audit = audit;
    }

    public MaturityLevel(int id, String levelAbbreviation, String name, String description, String type, String creationUser, Date fechaCreado, String actualizado, Date modificationDate, int active, String audit, Integer umbral, Integer weight, Set<MaturityLevelPractice> maturityLevelPractices) {
        this.id = id;
        this.levelAbbreviation = levelAbbreviation;
        this.name = name;
        this.description = description;
        this.type = type;
        this.creationUser = creationUser;
        this.fechaCreado = fechaCreado;
        this.actualizado = actualizado;
        this.modificationDate = modificationDate;
        this.active = active;
        this.audit = audit;
        this.umbral = umbral;
        this.weight = weight;
        this.maturityLevelPractices = maturityLevelPractices;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //add to mapped
   // @GeneratedValue(generator="maturity_level_id_seq", strategy=GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "level_abbreviation", nullable = false)
    public String getLevelAbbreviation() {
        return this.levelAbbreviation;
    }

    public void setLevelAbbreviation(String levelAbbreviation) {
        this.levelAbbreviation = levelAbbreviation;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "type")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "creation_user", nullable = false)
    public String getCreationUser() {
        return this.creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_creado", nullable = false, length = 13)
    public Date getFechaCreado() {
        return this.fechaCreado;
    }

    public void setFechaCreado(Date fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    @Column(name = "actualizado")
    public String getActualizado() {
        return this.actualizado;
    }

    public void setActualizado(String actualizado) {
        this.actualizado = actualizado;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "modification_date", length = 13)
    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Column(name = "active", nullable = false)
    public int getActive() {
        return this.active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    @Column(name = "audit", nullable = false)
    public String getAudit() {
        return this.audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    @Column(name = "umbral", precision = 5, scale = 0)
    public Integer getUmbral() {
        return this.umbral;
    }

    public void setUmbral(Integer umbral) {
        this.umbral = umbral;
    }

    @Column(name = "weight", precision = 5, scale = 0)
    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "maturityLevel")
    public Set<MaturityLevelPractice> getMaturityLevelPractices() {
        return this.maturityLevelPractices;
    }

    public void setMaturityLevelPractices(Set<MaturityLevelPractice> maturityLevelPractices) {
        this.maturityLevelPractices = maturityLevelPractices;
    }

}
