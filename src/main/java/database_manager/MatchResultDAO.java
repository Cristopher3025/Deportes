package database_manager;

import database.Match;
import database.MatchResult;
import jakarta.persistence.*;
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

}
