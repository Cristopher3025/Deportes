package database_manager;

import database.Match;
import database.Tournament;
import jakarta.persistence.*;
import java.util.List;

public class MatchDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("tournament_persistence");
    private final EntityManager em = emf.createEntityManager();

    public void addMatch(Match m) {
        em.getTransaction().begin();
        em.persist(m);
        em.getTransaction().commit();
    }
    
    public List<Match> findByTournament(Tournament torneo) {
    return em.createQuery("SELECT m FROM Match m WHERE m.tournamentId = :torneo", Match.class)
             .setParameter("torneo", torneo)
             .getResultList();
    }
    
    public List<Match> findPendientes() {
    return em.createQuery("SELECT m FROM Match m WHERE m.status = 'Pendiente'", Match.class)
             .getResultList();
}

    public void updateMatch(Match m) {
         em.getTransaction().begin();
        em.merge(m);
        em.getTransaction().commit();
    }
    
    public List<Match> findAll() {
    return em.createQuery("SELECT m FROM Match m", Match.class).getResultList();
    }



}
