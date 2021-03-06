package com.ampaiva.mcsheep.detection.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.CascadeOnDelete;

/**
 * The persistent class for the methods database table.
 * 
 */
@Entity
@Table(name = "methods")
@NamedQueries({ @NamedQuery(name = "Method.findAll", query = "SELECT m FROM Method m"),
        @NamedQuery(name = "Method.findById", query = "SELECT r FROM Method r WHERE r.id=?1") })
public class Method implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(unique = true, nullable = false)
    private int id;

    @Column(length = 255)
    private String name;

    @Column(nullable = false)
    private int beglin;

    @Column(nullable = false)
    private int begcol;

    @Column(nullable = false)
    private int endlin;

    @Column(nullable = false)
    private int endcol;

    @Lob
    private String source;

    @OneToMany(mappedBy = "methodBean", orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @CascadeOnDelete
    private List<Call> calls;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "unit", nullable = false)
    private Unit unitBean;

    public Method() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBeglin() {
        return beglin;
    }

    public void setBeglin(int beglin) {
        this.beglin = beglin;
    }

    public int getBegcol() {
        return begcol;
    }

    public void setBegcol(int begcol) {
        this.begcol = begcol;
    }

    public int getEndlin() {
        return endlin;
    }

    public void setEndlin(int endlin) {
        this.endlin = endlin;
    }

    public int getEndcol() {
        return endcol;
    }

    public void setEndcol(int endcol) {
        this.endcol = endcol;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Call> getCalls() {
        return this.calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    public Unit getUnitBean() {
        return this.unitBean;
    }

    public void setUnitBean(Unit unitBean) {
        this.unitBean = unitBean;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + begcol;
        result = prime * result + beglin;
        result = prime * result + endcol;
        result = prime * result + endlin;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((unitBean == null) ? 0 : unitBean.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Method other = (Method) obj;
        if (begcol != other.begcol) {
            return false;
        }
        if (beglin != other.beglin) {
            return false;
        }
        if (endcol != other.endcol) {
            return false;
        }
        if (endlin != other.endlin) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (unitBean == null) {
            if (other.unitBean != null) {
                return false;
            }
        } else if (!unitBean.equals(other.unitBean)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Method [id=" + id + ", name=" + name + ", unitBean=" + unitBean + "]";
    }

}