package database_manager;

import database.MatchResult;
import jakarta.persistence.*;

public class MatchResultDAO {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("tournament_persistence");
    private final EntityManager em = emf.createEntityManager();

    public void addResult(MatchResult r) {
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
    }
}
