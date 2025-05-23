package database_manager;

import database.Team;
import jakarta.persistence.*;

import java.util.List;

public class TeamDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public TeamDAO() {
        emf = Persistence.createEntityManagerFactory("tournament_persistence");
        em = emf.createEntityManager();
    }

    public void addTeam(Team t) {
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
    }

    public Team findByName(String name) {
        try {
            return em.createNamedQuery("Team.findByTeamName", Team.class)
                    .setParameter("teamName", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void updateTeam(Team t) {
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
    }

    public void deleteTeam(Team t) {
        em.getTransaction().begin();
        t = em.merge(t);
        em.remove(t);
        em.getTransaction().commit();
    }

    public List<Team> findAll() {
        return em.createNamedQuery("Team.findAll", Team.class).getResultList();
    }
}
