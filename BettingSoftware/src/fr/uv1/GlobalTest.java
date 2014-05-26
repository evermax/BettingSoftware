package fr.uv1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import fr.uv1.bettingServices.BettingSoft;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.DataBaseConnection;

public class GlobalTest {
    public static String mgr_password = "Kangourou";
    private static BettingSoft bettingProgram;
    private static Connection connection;
    private static PreparedStatement psRemove;

    public static void main(String[] args) throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException, CompetitionException {
        
        try {
            connection = DataBaseConnection.getConnection();
            psRemove = connection.prepareStatement(" DELETE FROM competitionparticipants; DELETE FROM bet; DELETE FROM teammembers; DELETE FROM competitionranking; DELETE FROM competition; DELETE FROM competitor; DELETE FROM subscriber;");
            psRemove.executeUpdate();
            psRemove.close();
            connection.close();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        /*
         * Instantiation du Programme
         */
        bettingProgram = new BettingSoft(mgr_password);
        
        
        /*
         * Ajout des différents joueurs
         */
        // Joueur 1
        String subs1 = "Ghost";
        String pwdSubs1 = bettingProgram.subscribe("Snow", "John", subs1, "10-02-1963",
                mgr_password);
        bettingProgram.creditSubscriber("Ghost", 20000, mgr_password);
        
        // Joueur 2
        String subs2 = "Quidam";
        String pwdSubs2 = bettingProgram.subscribe("Smith", "John", subs2, "14-03-1958",
                mgr_password);
        bettingProgram.creditSubscriber("Quidam", 25000, mgr_password);
        
        /*
         * Ajout des différents compétiteurs
         */
        // Competiteurs (personnes)
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_password);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_password);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_password);
        
        // Competiteurs (équipes)
        CompetitorTeam ct1 = (CompetitorTeam) bettingProgram.createCompetitor("TheBosses", mgr_password);
        CompetitorTeam ct2 = (CompetitorTeam) bettingProgram.createCompetitor("GreatThoughts", mgr_password);
        
        /* 
         * Ajout des différentes compétitions
         */
        // Competition 1
        String competition1Name = "GolfTournament";
        GregorianCalendar competition1closing = new GregorianCalendar(2014, 7, 14);
        Collection<Competitor> competitors1 = new ArrayList<Competitor>();
        competitors1.add(c1);
        competitors1.add(c2);
        competitors1.add(c3);
        bettingProgram.addCompetition(competition1Name, competition1closing, competitors1, mgr_password);
        
        // Competition 2
        String competition2Name = "100mHaie";
        GregorianCalendar competition2closing = new GregorianCalendar(2014, 6, 20);
        Collection<Competitor> competitors2 = new ArrayList<Competitor>();
        competitors2.add(c2);
        competitors2.add(c3);
        bettingProgram.addCompetition(competition2Name, competition2closing, competitors2, mgr_password);
        
        
        /*
         * Différents paris sur différentes compétitions
         */
        bettingProgram.betOnPodium(3000, competition1Name, c3, c2, c1, subs1, pwdSubs1);
        bettingProgram.betOnPodium(3600, competition1Name, c2, c3, c1, subs2, pwdSubs2);
        bettingProgram.betOnWinner(4000, competition2Name, c2, subs1, pwdSubs1);
        bettingProgram.betOnWinner(14000, competition2Name, c3, subs2, pwdSubs2);
        bettingProgram.betOnWinner(14000, competition2Name, c2, subs2, pwdSubs2);
        
        /*
         * Solde des paris
         */
        bettingProgram.settlePodium(competition1Name, c1, c2, c3, mgr_password);
        bettingProgram.settleWinner(competition2Name, c2, mgr_password);
    }
}
