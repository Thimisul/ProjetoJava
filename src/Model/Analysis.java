/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author thi_s
 */
@Entity
@Table(name = "analysis")
@NamedQueries({
    @NamedQuery(name = "Analysis.findAll", query = "SELECT a FROM Analysis a"),
    @NamedQuery(name = "Analysis.findByAnalysisId", query = "SELECT a FROM Analysis a WHERE a.analysisId = :analysisId"),
    @NamedQuery(name = "Analysis.findByDate", query = "SELECT a FROM Analysis a WHERE a.date = :date")})
public class Analysis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "analysis_id")
    private Integer analysisId;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @ManyToMany(mappedBy = "analysisCollection")
    private Collection<Table1> table1Collection;
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @ManyToOne
    private Client clientId;

    public Analysis() {
    }

    public Analysis(Integer analysisId) {
        this.analysisId = analysisId;
    }

    public Integer getAnalysisId() {
        return analysisId;
    }

    public void setAnalysisId(Integer analysisId) {
        this.analysisId = analysisId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection<Table1> getTable1Collection() {
        return table1Collection;
    }

    public void setTable1Collection(Collection<Table1> table1Collection) {
        this.table1Collection = table1Collection;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (analysisId != null ? analysisId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Analysis)) {
            return false;
        }
        Analysis other = (Analysis) object;
        if ((this.analysisId == null && other.analysisId != null) || (this.analysisId != null && !this.analysisId.equals(other.analysisId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Analysis[ analysisId=" + analysisId + " ]";
    }
    
}
