package database;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "SPORT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sport.findAll", query = "SELECT s FROM Sport s"),
    @NamedQuery(name = "Sport.findBySportId", query = "SELECT s FROM Sport s WHERE s.sportId = :sportId"),
    @NamedQuery(name = "Sport.findBySportName", query = "SELECT s FROM Sport s WHERE s.sportName = :sportName"),
    @NamedQuery(name = "Sport.findByBallImagePath", query = "SELECT s FROM Sport s WHERE s.ballImagePath = :ballImagePath")})
public class Sport implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sportId")
    private Collection<Tournament> tournamentCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sportId")
    private Collection<Team> teamCollection;

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "SPORT_ID")
    private BigDecimal sportId;
    @Basic(optional = false)
    @Column(name = "SPORT_NAME")
    private String sportName;
    @Column(name = "BALL_IMAGE_PATH")
    private String ballImagePath;

    public Sport() {
    }
    public Sport(String prm_sport_name){
        this.sportName = prm_sport_name;
    }

    public Sport(BigDecimal sportId) {
        this.sportId = sportId;
    }

    public Sport(BigDecimal sportId, String sportName) {
        this.sportId = sportId;
        this.sportName = sportName;
    }

    public BigDecimal getSportId() {
        return sportId;
    }

    public void setSportId(BigDecimal sportId) {
        this.sportId = sportId;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getBallImagePath() {
        return ballImagePath;
    }

    public void setBallImagePath(String ballImagePath) {
        this.ballImagePath = ballImagePath;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sportId != null ? sportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sport)) {
            return false;
        }
        Sport other = (Sport) object;
        if ((this.sportId == null && other.sportId != null) || (this.sportId != null && !this.sportId.equals(other.sportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "database.Sport[ sportId=" + sportId + " ]";
    }

    @XmlTransient
    public Collection<Tournament> getTournamentCollection() {
        return tournamentCollection;
    }

    public void setTournamentCollection(Collection<Tournament> tournamentCollection) {
        this.tournamentCollection = tournamentCollection;
    }

    @XmlTransient
    public Collection<Team> getTeamCollection() {
        return teamCollection;
    }

    public void setTeamCollection(Collection<Team> teamCollection) {
        this.teamCollection = teamCollection;
    }
    
}
