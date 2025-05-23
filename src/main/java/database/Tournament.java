/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author crist
 */
@Entity
@Table(name = "TOURNAMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tournament.findAll", query = "SELECT t FROM Tournament t"),
    @NamedQuery(name = "Tournament.findByTournamentId", query = "SELECT t FROM Tournament t WHERE t.tournamentId = :tournamentId"),
    @NamedQuery(name = "Tournament.findByTournamentName", query = "SELECT t FROM Tournament t WHERE t.tournamentName = :tournamentName"),
    @NamedQuery(name = "Tournament.findByTeamCount", query = "SELECT t FROM Tournament t WHERE t.teamCount = :teamCount"),
    @NamedQuery(name = "Tournament.findByMatchTimeMinutes", query = "SELECT t FROM Tournament t WHERE t.matchTimeMinutes = :matchTimeMinutes"),
    @NamedQuery(name = "Tournament.findByStartDate", query = "SELECT t FROM Tournament t WHERE t.startDate = :startDate")})
public class Tournament implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TOURNAMENT_ID")
    private BigDecimal tournamentId;
    @Basic(optional = false)
    @Column(name = "TOURNAMENT_NAME")
    private String tournamentName;
    @Basic(optional = false)
    @Column(name = "TEAM_COUNT")
    private BigInteger teamCount;
    @Basic(optional = false)
    @Column(name = "MATCH_TIME_MINUTES")
    private BigInteger matchTimeMinutes;
    @Column(name = "START_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tournamentId")
    private Collection<TournamentTeam> tournamentTeamCollection;
    @JoinColumn(name = "SPORT_ID", referencedColumnName = "SPORT_ID")
    @ManyToOne(optional = false)
    private Sport sportId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tournamentId")
    private Collection<Match> matchCollection;

    public Tournament() {
    }

    public Tournament(BigDecimal tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Tournament(BigDecimal tournamentId, String tournamentName, BigInteger teamCount, BigInteger matchTimeMinutes) {
        this.tournamentId = tournamentId;
        this.tournamentName = tournamentName;
        this.teamCount = teamCount;
        this.matchTimeMinutes = matchTimeMinutes;
    }

    public BigDecimal getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(BigDecimal tournamentId) {
        this.tournamentId = tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public BigInteger getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(BigInteger teamCount) {
        this.teamCount = teamCount;
    }

    public BigInteger getMatchTimeMinutes() {
        return matchTimeMinutes;
    }

    public void setMatchTimeMinutes(BigInteger matchTimeMinutes) {
        this.matchTimeMinutes = matchTimeMinutes;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @XmlTransient
    public Collection<TournamentTeam> getTournamentTeamCollection() {
        return tournamentTeamCollection;
    }

    public void setTournamentTeamCollection(Collection<TournamentTeam> tournamentTeamCollection) {
        this.tournamentTeamCollection = tournamentTeamCollection;
    }

    public Sport getSportId() {
        return sportId;
    }

    public void setSportId(Sport sportId) {
        this.sportId = sportId;
    }

    @XmlTransient
    public Collection<Match> getMatchCollection() {
        return matchCollection;
    }

    public void setMatchCollection(Collection<Match> matchCollection) {
        this.matchCollection = matchCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tournamentId != null ? tournamentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tournament)) {
            return false;
        }
        Tournament other = (Tournament) object;
        if ((this.tournamentId == null && other.tournamentId != null) || (this.tournamentId != null && !this.tournamentId.equals(other.tournamentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.Tournament[ tournamentId=" + tournamentId + " ]";
    }
    
}
