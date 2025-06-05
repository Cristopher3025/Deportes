package database_manager;

import database.MatchResult;
import database.Match;
import database.Team;
import database.Tournament;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class MatchResultDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("tournament_persistence");
    private final EntityManager em = emf.createEntityManager();

    public void addResult(MatchResult r) {
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
    }

    public List<MatchResult> findByMatch(Match match) {
        return em.createQuery("SELECT r FROM MatchResult r WHERE r.matchId = :match", MatchResult.class)
                 .setParameter("match", match)
                 .getResultList();
    }

    
    public int getGolesTotales(Tournament torneo, Team equipo) {
        List<MatchResult> lista = em.createQuery(
                "SELECT r FROM MatchResult r WHERE r.matchId.tournamentId = :torneo AND r.teamId = :equipo",
                MatchResult.class)
            .setParameter("torneo", torneo)
            .setParameter("equipo", equipo)
            .getResultList();

        return lista.stream()
                    .mapToInt(r -> r.getGoals().intValue())
                    .sum();
    }
}
