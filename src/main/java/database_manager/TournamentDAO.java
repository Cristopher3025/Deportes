package database_manager;

import database.Tournament;
import jakarta.persistence.*;

import java.util.List;

public class TournamentDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public TournamentDAO() {
        emf = Persistence.createEntityManagerFactory("tournament_persistence");
        em = emf.createEntityManager();
    }

    public void addTournament(Tournament t) {
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
    }

    public List<Tournament> findAll() {
        return em.createNamedQuery("Tournament.findAll", Tournament.class).getResultList();
    }
}
