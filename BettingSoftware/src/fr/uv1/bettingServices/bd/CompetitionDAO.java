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
                PreparedStatement psPersistCompetitorID = connection
                        .prepareStatement("INSERT INTO competitionparticipants(idcompetition, idcompetitor) values (?, ?)");
                psPersistCompetitorID.setInt(1, id);
                psPersistCompetitorID.setInt(2, ((ACompetitor)competitor).getId());
                psPersistCompetitorID.executeUpdate();
                psPersistCompetitorID.close();
                
                PreparedStatement psPersistCompetitor = connection
                        .prepareStatement("INSERT INTO competitor(name, firstname, birthdate, isteam)  values (?, ?, ?, ?)");
                if (((ACompetitor)competitor) instanceof CompetitorPlayer) {
                    psPersistCompetitor.setString(1, ((CompetitorPlayer)competitor).getName());
                    psPersistCompetitor.setString(2, ((CompetitorPlayer)competitor).getFirstName());
                    psPersistCompetitor.setDate(3, new java.sql.Date(((CompetitorPlayer)competitor).getBorndate().getTimeInMillis()));
                    psPersistCompetitor.setBoolean(4, false);
                } else {
                    psPersistCompetitor.setString(1, ((CompetitorTeam)competitor).getName());
                    psPersistCompetitor.setString(2, null);
                    psPersistCompetitor.setDate(3, null);
                    psPersistCompetitor.setBoolean(4, true);
                }
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

    public static List<Competition> findAll() throws SQLException,
            BadParametersException, CompetitionException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competition order by idcompetition");
        ResultSet resultSet = psSelect.executeQuery();
        List<Competition> competitions = new ArrayList<Competition>();
        while (resultSet.next()) {
            Calendar cal = Calendar.getInstance();
            Collection<Competitor> competitors = new ArrayList<Competitor>();
            cal.setTime(resultSet.getDate("closingdate"));
            competitions.add(new Competition(resultSet.getInt("idcompetition"),
                    resultSet.getString("name"), cal, competitors));
        }
        resultSet.close();
        psSelect.close();

        for (Competition competition : competitions) {
            PreparedStatement psCompetitorId = c
                    .prepareStatement("select idcompetitor from competitionparticipants where idcompetition =?");
            psCompetitorId.setInt(1, competition.getId());
            ResultSet resultCompetitorId = psCompetitorId.executeQuery();
            while (resultCompetitorId.next()) {
                PreparedStatement psCompetitor = c
                        .prepareStatement("select * from competitor where idcompetitor = ?");
                psCompetitor.setInt(1,
                        resultCompetitorId.getInt("idcompetitor"));
                ResultSet resultCompetitor = psCompetitor.executeQuery();
                if (resultCompetitor.getBoolean("isteam")) {
                    competition.getCompetitors().add(new CompetitorTeam(resultCompetitor
                            .getInt("idcompetitor"), resultCompetitor
                            .getString("name")));
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(resultCompetitor.getDate("closingdate"));
                    competition.getCompetitors().add(new CompetitorPlayer(resultCompetitor
                            .getInt("idcompetitor"), resultCompetitor
                            .getString("name"), resultCompetitor
                            .getString("firstname"), cal));
                }
                resultCompetitor.close();
                psCompetitor.close();
            }
            resultCompetitorId.close();
            psCompetitorId.close();
        }

        c.close();

        return competitions;
    }
}
