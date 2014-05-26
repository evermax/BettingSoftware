package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.uv1.bettingServices.ACompetitor;
import fr.uv1.bettingServices.Bet;
import fr.uv1.bettingServices.Competition;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.bettingServices.Subscriber;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.utils.DataBaseConnection;

public class BetDAO {

    public static Bet persist(Bet bet) throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection
                    .prepareStatement("insert into bet (idsubscriber, idcompetition, tokens, idcompetitor1, idcompetitor2, idcompetitor3) values (?, ?, ?, ?, ?, ?)");
            psPersist.setInt(1, bet.getSubscriber().getId());
            psPersist.setInt(2, bet.getCompetition().getId());
            psPersist.setLong(3, bet.getTokenNumber());
            psPersist.setInt(4, ((ACompetitor) bet.getWinner()).getId());
            if (bet.isBetOnPodium()) {
                psPersist.setInt(5, ((ACompetitor) bet.getSecond()).getId());
                psPersist.setInt(6, ((ACompetitor) bet.getThird()).getId());
            } else {
                psPersist.setNull(5, java.sql.Types.NULL);
                psPersist.setNull(6, java.sql.Types.NULL);
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

        return bet;
    }

    public static List<Bet> findAll() throws BadParametersException,
            CompetitionException, SQLException {
        Connection connection = DataBaseConnection.getConnection();
        PreparedStatement psSelect = connection
                .prepareStatement("select * from bet");
        ResultSet resultSet = psSelect.executeQuery();
        List<Bet> bets = new ArrayList<Bet>();
        while (resultSet.next()) {
            int idBet = resultSet.getInt("idbet");
            Subscriber subscriber = SubscriberDAO.findById(resultSet
                    .getInt("idsubscriber"));
            long tokens = resultSet.getLong("tokens");
            Competition competition = CompetitionDAO.findById(resultSet
                    .getInt("idcompetition"));
            Competitor competitor1 = null, competitor2 = null, competitor3 = null;
            int idCompetitor1 = resultSet.getInt("idcompetitor1");
            competitor1 = findCompetitorById(idCompetitor1);
            int idCompetitor2 = resultSet.getInt("idcompetitor2");
            if (!resultSet.wasNull())
                competitor2 = findCompetitorById(idCompetitor2);
            int idCompetitor3 = resultSet.getInt("idcompetitor3");
            if (!resultSet.wasNull())
                competitor3 = findCompetitorById(idCompetitor3);

            bets.add(new Bet(idBet, tokens, subscriber, competition,
                    competitor1, competitor2, competitor3));

        }

        resultSet.close();
        psSelect.close();
        connection.close();
        return bets;

    }

    /**
     * Retourne tous les paris associés à une compétition
     */
    public static List<Bet> findByCompetition(Competition competition)
            throws SQLException, BadParametersException {
        Connection connection = DataBaseConnection.getConnection();
        PreparedStatement psSelect = connection
                .prepareStatement("select * from bet where idcompetition = ?");
        psSelect.setInt(1, competition.getId());
        ResultSet resultSet = psSelect.executeQuery();
        List<Bet> bets = new ArrayList<Bet>();
        while (resultSet.next()) {
            int idBet = resultSet.getInt("idbet");
            Subscriber subscriber = SubscriberDAO.findById(resultSet
                    .getInt("idsubscriber"));
            long tokens = resultSet.getLong("tokens");
            Competitor competitor1 = null, competitor2 = null, competitor3 = null;
            int idCompetitor1 = resultSet.getInt("idcompetitor1");
            competitor1 = findCompetitorById(idCompetitor1);
            int idCompetitor2 = resultSet.getInt("idcompetitor2");
            if (!resultSet.wasNull())
                competitor2 = findCompetitorById(idCompetitor2);
            int idCompetitor3 = resultSet.getInt("idcompetitor3");
            if (!resultSet.wasNull())
                competitor3 = findCompetitorById(idCompetitor3);

            bets.add(new Bet(idBet, tokens, subscriber, competition,
                    competitor1, competitor2, competitor3));
            // TODO Récupérer les compétiteurs et créer l'objet Bet
            // Faut-il effectuer une requête ici pour chaque compétiteur pour
            // vérifier d'abord si c'est une équipe ou un joueur ? On ne peut
            // pas appeler findById de CompetitorPlayerDAO ou de
            // CompetitorTeamDAO tant qu'on ne sait pas de quel type il s'agit..

        }

        resultSet.close();
        psSelect.close();
        connection.close();
        return bets;

    }

    public static void update(Bet bet) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psUpdate = c
                .prepareStatement("update bet set idsubscriber=?, idcompetition=?, tokens=?, idcompetitor1=?, idcompetitor2=?, idcompetitor3=? where idbet = ?");
        psUpdate.setInt(1, bet.getSubscriber().getId());
        psUpdate.setInt(2, bet.getCompetition().getId());
        psUpdate.setLong(3, bet.getTokenNumber());
        psUpdate.setInt(4, ((ACompetitor) bet.getWinner()).getId());
        if (bet.isBetOnPodium()) {
            psUpdate.setInt(5, ((ACompetitor) bet.getSecond()).getId());
            psUpdate.setInt(6, ((ACompetitor) bet.getThird()).getId());
        } else {
            psUpdate.setNull(5, java.sql.Types.NULL);
            psUpdate.setNull(6, java.sql.Types.NULL);
        }

        psUpdate.executeUpdate();

        psUpdate.close();

        c.close();

    }

    public static void delete(Bet bet) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psDelete = c
                .prepareStatement("delete from bet where idbet=?");
        psDelete.setInt(1, bet.getId());
        psDelete.executeUpdate();
        psDelete.close();
        c.close();
    }

    /*
     * Fonction qui retourne un compétiteur à partir de son id, indépendement du
     * fait que ce soit un joueur ou une équipe
     */
    private static Competitor findCompetitorById(int id) throws SQLException,
            BadParametersException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competitor where idcompetitor = ?");
        psSelect.setInt(1, id);
        ResultSet resultSet = psSelect.executeQuery();
        Competitor competitor = null;
        while (resultSet.next()) {
            boolean isTeam = resultSet.getBoolean("isteam");
            if (isTeam)
                competitor = new CompetitorTeam(id, resultSet.getString("name"));
            else {
                Calendar cal = Calendar.getInstance();
                cal.setTime(resultSet.getDate("birthdate"));
                competitor = new CompetitorPlayer(
                        resultSet.getInt("idcompetitor"),
                        resultSet.getString("name"),
                        resultSet.getString("firstname"), cal);
            }
        }
        resultSet.close();
        psSelect.close();
        c.close();
        return competitor;
    }

}
