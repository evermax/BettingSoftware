package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.uv1.bettingServices.ACompetitor;
import fr.uv1.bettingServices.Bet;
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

    public static void update(Bet bet) {

    }

    public static void delete(Bet bet) {

    }

}
