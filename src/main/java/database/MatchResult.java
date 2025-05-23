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
@Table(name = "MATCH_RESULT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MatchResult.findAll", query = "SELECT m FROM MatchResult m"),
    @NamedQuery(name = "MatchResult.findByResultId", query = "SELECT m FROM MatchResult m WHERE m.resultId = :resultId"),
    @NamedQuery(name = "MatchResult.findByGoals", query = "SELECT m FROM MatchResult m WHERE m.goals = :goals"),
    @NamedQuery(name = "MatchResult.findByExtraPoints", query = "SELECT m FROM MatchResult m WHERE m.extraPoints = :extraPoints")})
public class MatchResult implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "RESULT_ID")
    private BigDecimal resultId;
    @Column(name = "GOALS")
    private BigInteger goals;
    @Column(name = "EXTRA_POINTS")
    private BigInteger extraPoints;
    @JoinColumn(name = "MATCH_ID", referencedColumnName = "MATCH_ID")
    @ManyToOne(optional = false)
    private Match matchId;
    @JoinColumn(name = "TEAM_ID", referencedColumnName = "TEAM_ID")
    @ManyToOne(optional = false)
    private Team teamId;

    public MatchResult() {
    }

    public MatchResult(BigDecimal resultId) {
        this.resultId = resultId;
    }

    public BigDecimal getResultId() {
        return resultId;
    }

    public void setResultId(BigDecimal resultId) {
        this.resultId = resultId;
    }

    public BigInteger getGoals() {
        return goals;
    }

    public void setGoals(BigInteger goals) {
        this.goals = goals;
    }

    public BigInteger getExtraPoints() {
        return extraPoints;
    }

    public void setExtraPoints(BigInteger extraPoints) {
        this.extraPoints = extraPoints;
    }

    public Match getMatchId() {
        return matchId;
    }

    public void setMatchId(Match matchId) {
        this.matchId = matchId;
    }

    public Team getTeamId() {
        return teamId;
    }

    public void setTeamId(Team teamId) {
        this.teamId = teamId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resultId != null ? resultId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatchResult)) {
            return false;
        }
        MatchResult other = (MatchResult) object;
        if ((this.resultId == null && other.resultId != null) || (this.resultId != null && !this.resultId.equals(other.resultId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.MatchResult[ resultId=" + resultId + " ]";
    }
    
}
