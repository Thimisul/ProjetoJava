/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author thi_s
 */
@Entity
@Table(name = "table_1")
@NamedQueries({
    @NamedQuery(name = "Table1.findAll", query = "SELECT t FROM Table1 t"),
    @NamedQuery(name = "Table1.findByTableId", query = "SELECT t FROM Table1 t WHERE t.tableId = :tableId"),
    @NamedQuery(name = "Table1.findByOwner", query = "SELECT t FROM Table1 t WHERE t.owner = :owner"),
    @NamedQuery(name = "Table1.findByName", query = "SELECT t FROM Table1 t WHERE t.name = :name")})
public class Table1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "table_id")
    private Integer tableId;
    @Column(name = "owner")
    private String owner;
    @Column(name = "name")
    private String name;
    @JoinTable(name = "table_analysis", joinColumns = {
        @JoinColumn(name = "table_id", referencedColumnName = "table_id")}, inverseJoinColumns = {
        @JoinColumn(name = "analysis_id", referencedColumnName = "analysis_id")})
    @ManyToMany
    private Collection<Analysis> analysisCollection;

    public Table1() {
    }

    public Table1(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Analysis> getAnalysisCollection() {
        return analysisCollection;
    }

    public void setAnalysisCollection(Collection<Analysis> analysisCollection) {
        this.analysisCollection = analysisCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tableId != null ? tableId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Table1)) {
            return false;
        }
        Table1 other = (Table1) object;
        if ((this.tableId == null && other.tableId != null) || (this.tableId != null && !this.tableId.equals(other.tableId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Table1[ tableId=" + tableId + " ]";
    }
    
}
