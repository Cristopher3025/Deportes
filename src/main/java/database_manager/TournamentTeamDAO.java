package database_manager;

import database.Tournament;
import database.TournamentTeam;
import jakarta.persistence.*;
import java.util.List;

public class TournamentTeamDAO {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("tournament_persistence");
    private final EntityManager em = emf.createEntityManager();

    public void inscribir(TournamentTeam tt) {
        em.getTransaction().begin();
        em.persist(tt);
        em.getTransaction().commit();
    }
    
    public List<TournamentTeam> findByTournament(Tournament torneo) {
    return em.createQuery("SELECT tt FROM TournamentTeam tt WHERE tt.tournamentId = :torneo", TournamentTeam.class)
             .setParameter("torneo", torneo)
             .getResultList();
}

}
