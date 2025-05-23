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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author crist
 */
@Entity
@Table(name = "TEAM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Team.findAll", query = "SELECT t FROM Team t"),
    @NamedQuery(name = "Team.findByTeamId", query = "SELECT t FROM Team t WHERE t.teamId = :teamId"),
    @NamedQuery(name = "Team.findByTeamName", query = "SELECT t FROM Team t WHERE t.teamName = :teamName"),
    @NamedQuery(name = "Team.findByPhotoPath", query = "SELECT t FROM Team t WHERE t.photoPath = :photoPath")})
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TEAM_ID")
    private BigDecimal teamId;
    @Basic(optional = false)
    @Column(name = "TEAM_NAME")
    private String teamName;
    @Column(name = "PHOTO_PATH")
    private String photoPath;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamId")
    private Collection<MatchResult> matchResultCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "teamId")
    private Collection<TournamentTeam> tournamentTeamCollection;
    @JoinColumn(name = "SPORT_ID", referencedColumnName = "SPORT_ID")
    @ManyToOne(optional = false)
    private Sport sportId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team1Id")
    private Collection<Match> matchCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team2Id")
    private Collection<Match> matchCollection1;
    @OneToMany(mappedBy = "winnerId")
    private Collection<Match> matchCollection2;

    public Team() {
    }

    public Team(BigDecimal teamId) {
        this.teamId = teamId;
    }

    public Team(BigDecimal teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public BigDecimal getTeamId() {
        return teamId;
    }

    public void setTeamId(BigDecimal teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    @XmlTransient
    public Collection<MatchResult> getMatchResultCollection() {
        return matchResultCollection;
    }

    public void setMatchResultCollection(Collection<MatchResult> matchResultCollection) {
        this.matchResultCollection = matchResultCollection;
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

    @XmlTransient
    public Collection<Match> getMatchCollection1() {
        return matchCollection1;
    }

    public void setMatchCollection1(Collection<Match> matchCollection1) {
        this.matchCollection1 = matchCollection1;
    }

    @XmlTransient
    public Collection<Match> getMatchCollection2() {
        return matchCollection2;
    }

    public void setMatchCollection2(Collection<Match> matchCollection2) {
        this.matchCollection2 = matchCollection2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (teamId != null ? teamId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Team)) {
            return false;
        }
        Team other = (Team) object;
        if ((this.teamId == null && other.teamId != null) || (this.teamId != null && !this.teamId.equals(other.teamId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.Team[ teamId=" + teamId + " ]";
    }
    
}
