package database_manager;

import database.Sport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import java.util.List;

public class SportDAO {

    private EntityManagerFactory emf;
    private EntityManager em;

    public SportDAO() {
        try {
            emf = Persistence.createEntityManagerFactory("tournament_persistence");
            em = emf.createEntityManager();
        } catch (Exception e) {
            System.out.println("Error de conexión JPA");
            e.printStackTrace();
        }
    }

    public void addSport(Sport sport) {
        em.getTransaction().begin();
        em.persist(sport);
        em.getTransaction().commit();
    }

    public Sport findSportByName(String name) {
        try {
            return em.createNamedQuery("Sport.findBySportName", Sport.class)
                    .setParameter("sportName", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void updateSport(Sport sport) {
        em.getTransaction().begin();
        em.merge(sport);
        em.getTransaction().commit();
    }

    public void deleteSport(Sport sport) {
        em.getTransaction().begin();
        sport = em.merge(sport); // asegurarse de que esté gestionado
        em.remove(sport);
        em.getTransaction().commit();
    }
    
    

public List<Sport> findAll() {
    return em.createNamedQuery("Sport.findAll", Sport.class).getResultList();
}

}
