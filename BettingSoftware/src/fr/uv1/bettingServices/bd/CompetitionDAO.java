package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import fr.uv1.bettingServices.Competition;
import fr.uv1.bettingServices.ACompetitor;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.utils.DataBaseConnection;

public class CompetitionDAO {
    public static Competition persist(Competition competition)
            throws SQLException {
        Connection connection = DataBaseConnection.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection
                    .prepareStatement("INSERT INTO competition(name, closingdate, settled) values (?, ?, ?)");
            psPersist.setString(1, competition.getName());
            psPersist.setDate(2, new java.sql.Date(competition.getClosingDate()
                    .getTimeInMillis()));
            psPersist.setBoolean(3, competition.isSettled());

            psPersist.executeUpdate();
            psPersist.close();

            // Retrieving the value of the id with a request on the
            // sequence (competition_idcompetition_seq).
            PreparedStatement psIdValue = connection
                    .prepareStatement("select currval('competition_idcompetition_seq') as value_id");
            ResultSet resultSet = psIdValue.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt("value_id");
            }

            resultSet.close();

            psIdValue.close();

            for (Competitor competitor : competition.getCompetitors()) {
                // TODO : Ne pas persister à nouveau les entrées qui existent
                // déjà
                if (competitor instanceof CompetitorPlayer) {
                    CompetitorPlayerDAO.persist((CompetitorPlayer) competitor);
                } else {
                    CompetitorTeamDAO.persist((CompetitorTeam) competitor);
                }
                PreparedStatement psPersistCompetitorID = connection
                        .prepareStatement("INSERT INTO competitionparticipants(idcompetition, idcompetitor) values (?, ?)");
                psPersistCompetitorID.setInt(1, id);
                psPersistCompetitorID.setInt(2,
                        ((ACompetitor) competitor).getId());
                psPersistCompetitorID.executeUpdate();
                psPersistCompetitorID.close();

            }

            connection.commit();

            competition.setId(id);

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

        return competition;
    }
    
    public static void removeCompetitorFromCompetition(ACompetitor competitor, Competition competition) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c.prepareStatement("delete from competitionparticipants where idcompetitor = ? and idcompetition = ?");
        psSelect.setInt(1, competitor.getId());
        psSelect.setInt(2, competition.getId());
        
        psSelect.executeUpdate();
        
        psSelect.close();
        
        c.close();
    }
    
    public static void addCompetitorInCompetition(ACompetitor competitor, Competition competition) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c.prepareStatement("insert into competitionparticipants(idcompetitor, idcompetition) values (?, ?)");
        psSelect.setInt(1, competitor.getId());
        psSelect.setInt(2, competition.getId());
        
        psSelect.executeUpdate();
        
        psSelect.close();
        
        c.close();
    }
    

    public static List<Competition> findAll() throws SQLException,
            BadParametersException, CompetitionException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competition order by idcompetition");
        ResultSet resultSet = psSelect.executeQuery();
        List<Competition> competitions = new ArrayList<Competition>();
        while (resultSet.next()) {
            int idCompetition = resultSet.getInt("idcompetition");

            Calendar cal = Calendar.getInstance();
            cal.setTime(resultSet.getDate("closingdate"));

            // On récupère la liste des participants à la compétition
            Collection<Competitor> competitors = new ArrayList<Competitor>();
            PreparedStatement psCompetitorId = c
                    .prepareStatement("select idcompetitor, isteam from competitionparticipants natural join competitor where idcompetition = ?");
            psCompetitorId.setInt(1, idCompetition);
            ResultSet resultCompetitorId = psCompetitorId.executeQuery();
            while (resultCompetitorId.next()) {
                int idCompetitor = resultCompetitorId.getInt("idcompetitor");
                boolean isTeam = resultCompetitorId.getBoolean("isteam");
                if (!isTeam) {
                    competitors.add(CompetitorPlayerDAO.findById(idCompetitor));
                }

            }
            competitions.add(new Competition(idCompetition, resultSet
                    .getString("name"), cal, competitors));
        }
        resultSet.close();
        psSelect.close();

        c.close();

        return competitions;
    }

    public static Competition findById(int id) throws BadParametersException,
            CompetitionException, SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competition where idcompetition = ?");
        psSelect.setInt(1, id);
        ResultSet resultSet = psSelect.executeQuery();
        Competition competition = null;
        while (resultSet.next()) {
            int idCompetition = resultSet.getInt("idcompetition");

            Calendar cal = Calendar.getInstance();
            cal.setTime(resultSet.getDate("closingdate"));

            // On récupère la liste des participants à la compétition
            Collection<Competitor> competitors = new ArrayList<Competitor>();
            PreparedStatement psCompetitorId = c
                    .prepareStatement("select idcompetitor, isteam from competitionparticipants natural join competitor where idcompetition = ?");
            psCompetitorId.setInt(1, idCompetition);
            ResultSet resultCompetitorId = psCompetitorId.executeQuery();
            while (resultCompetitorId.next()) {
                int idCompetitor = resultCompetitorId.getInt("idcompetitor");
                boolean isTeam = resultCompetitorId.getBoolean("isteam");
                if (!isTeam) {
                    competitors.add(CompetitorPlayerDAO.findById(idCompetitor));
                }

            }
            competition = new Competition(idCompetition,
                    resultSet.getString("name"), cal, competitors);
        }
        resultSet.close();
        psSelect.close();

        c.close();

        return competition;
    }
}
