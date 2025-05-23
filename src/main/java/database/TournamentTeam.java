/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author crist
 */
@Entity
@Table(name = "TOURNAMENT_TEAM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TournamentTeam.findAll", query = "SELECT t FROM TournamentTeam t"),
    @NamedQuery(name = "TournamentTeam.findById", query = "SELECT t FROM TournamentTeam t WHERE t.id = :id"),
    @NamedQuery(name = "TournamentTeam.findByPosition", query = "SELECT t FROM TournamentTeam t WHERE t.position = :position")})
public class TournamentTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Column(name = "POSITION")
    private BigInteger position;
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne(optional = false)
    private Team teamId;
    @JoinColumn(name = "TOURNAMENT_ID", referencedColumnName = "TOURNAMENT_ID")
    @ManyToOne(optional = false)
    private Tournament tournamentId;

    public TournamentTeam() {
    }

    public TournamentTeam(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigInteger getPosition() {
        return position;
    }

    public void setPosition(BigInteger position) {
        this.position = position;
    }

    public Team getTeamId() {
        return teamId;
    }

    public void setTeamId(Team teamId) {
        this.teamId = teamId;
    }

    public Tournament getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Tournament tournamentId) {
        this.tournamentId = tournamentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TournamentTeam)) {
            return false;
        }
        TournamentTeam other = (TournamentTeam) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.TournamentTeam[ id=" + id + " ]";
    }
    
}
