package database;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "MATCH")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "Match.findAll", query = "SELECT m FROM Match m"),
@NamedQuery(name = "Match.findByMatchId", query = "SELECT m FROM Match m WHERE m.matchId = :matchId"),
@NamedQuery(name = "Match.findByScheduledTime", query = "SELECT m FROM Match m WHERE m.scheduledTime = :scheduledTime"),
@NamedQuery(name = "Match.findByStatus", query = "SELECT m FROM Match m WHERE m.status = :status")
})
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MATCH_ID")
    private BigDecimal matchId;

    @Column(name = "SCHEDULED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduledTime;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ROUND_NUMBER")
    private Integer roundNumber;

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matchId")
    private Collection<MatchResult> matchResultCollection;

    @JoinColumn(name = "TEAM1_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne(optional = false)
    private Team team1Id;

    @JoinColumn(name = "TEAM2_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne(optional = false)
    private Team team2Id;

    @JoinColumn(name = "WINNER_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne
    private Team winnerId;

    @JoinColumn(name = "TOURNAMENT_ID", referencedColumnName = "TOURNAMENT_ID")
    @ManyToOne(optional = false)
    private Tournament tournamentId;

    public Match() {
    }

    public Match(BigDecimal matchId) {
        this.matchId = matchId;
    }

    public BigDecimal getMatchId() {
        return matchId;
    }

    public void setMatchId(BigDecimal matchId) {
        this.matchId = matchId;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<MatchResult> getMatchResultCollection() {
        return matchResultCollection;
    }

    public void setMatchResultCollection(Collection<MatchResult> matchResultCollection) {
        this.matchResultCollection = matchResultCollection;
    }

    public Team getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(Team team1Id) {
        this.team1Id = team1Id;
    }

    public Team getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(Team team2Id) {
        this.team2Id = team2Id;
    }

    public Team getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Team winnerId) {
        this.winnerId = winnerId;
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
        hash += (matchId != null ? matchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Match)) {
            return false;
        }
        Match other = (Match) object;
        return !((this.matchId == null && other.matchId != null) || 
                 (this.matchId != null && !this.matchId.equals(other.matchId)));
    }

    @Override
    public String toString() {
        return "database.Match[ matchId=" + matchId + " ]";
    }
}
