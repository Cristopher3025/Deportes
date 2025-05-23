package database_manager;

import database.Sport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author crist
 */
public class DAO {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityMnagaer;
    
    public DAO(){
        try{
            this.entityManagerFactory = Persistence.createEntityManagerFactory("tournament_persistence");
            this.entityMnagaer = entityManagerFactory.createEntityManager();
        }catch (Exception e){
            System.out.println("No se establecio la conexion");
            e.printStackTrace();
        }
    }
    public void addSport(Sport sport){
        entityMnagaer.getTransaction().begin();
        entityMnagaer.persist(sport);
        entityMnagaer.getTransaction().commit();
    }
}
