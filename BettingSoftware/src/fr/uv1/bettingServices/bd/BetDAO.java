package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.uv1.bettingServices.ACompetitor;
import fr.uv1.bettingServices.Bet;
import fr.uv1.bettingServices.Competition;
import fr.uv1.bettingServices.Subscriber;
import fr.uv1.utils.DataBaseConnection;

public class BetDAO {

    public static Bet persist(Bet bet) throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection
                    .prepareStatement("insert into bets (idsubscriber, idcompetition, tokens, idcompetitor1, idcompetitor2, idcompetitor3) values (?, ?, ?, ?, ?, ?)");
            psPersist.setInt(1, bet.getSubscriber().getId());
            psPersist.setInt(2, bet.getCompetition().getId());
            psPersist.setLong(3, bet.getTokenNumber());
            psPersist.setInt(4, ((ACompetitor) bet.getWinner()).getId());
            if (bet.isBetOnPodium()) {
                psPersist.setInt(5, ((ACompetitor) bet.getSecond()).getId());
                psPersist.setInt(6, ((ACompetitor) bet.getThird()).getId());
            } else {
                psPersist.setNull(5, java.sql.Types.INTEGER);
                psPersist.setNull(6, java.sql.Types.INTEGER);
            }

            psPersist.executeUpdate();
            psPersist.close();

            PreparedStatement psIdValue = connection
                    .prepareStatement("select currval('bet_idbet_seq') as value_id");
            ResultSet resultSet = psIdValue.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt("value_id");
            }
            resultSet.close();
            psIdValue.close();
            connection.commit();
            bet.setId(id);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            connection.setAutoCommit(true);
            throw e;
        }

        connection.setAutoCommit(true);
        connection.close();
    }

    public static List<Bet> findAll() {

    }

    public static List<Bet> findByCompetition(Competition competition)
            throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        PreparedStatement psSelect = connection
                .prepareStatement("select * from bets where idcompetition = ?");
        psSelect.setInt(1, competition.getId());
        ResultSet resultSet = psSelect.executeQuery();
        List<Bet> bets = new ArrayList<Bet>();
        while (resultSet.next()) {
            int idBet = resultSet.getInt("idbet");
            Subscriber subscriber = SubscriberDAO.findById(resultSet
                    .getInt("idsubscriber"));
            Competition competition = CompetitionDAO.findById(resultSet
                    .getInt("idcompetition"));
            long tokens = resultSet.getLong("tokens");
            // TODO Récupérer les compétiteurs et créer l'objet Bet
            // Faut-il effectuer une requête ici pour chaque compétiteur pour
            // vérifier d'abord si c'est une équipe ou un joueur ? On ne peut
            // pas appeler findById de CompetitorPlayerDAO ou de
            // CompetitorTeamDAO tant qu'on ne sait pas de quel type il s'agit..

        }

    }

    public static void update(Bet bet) {

    }

    public static void delete(Bet bet) {

    }

}
