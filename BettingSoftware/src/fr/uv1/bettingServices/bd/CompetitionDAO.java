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
            // sequence (subscribers_id_seq).
            PreparedStatement psIdValue = connection
                    .prepareStatement("select currval('competitor_idcompetitor_seq') as value_id");
            ResultSet resultSet = psIdValue.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt("value_id");
            }

            resultSet.close();

            psIdValue.close();

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
        // TODO Trouver la structure idoine
        ArrayList<Integer, String, Calendar> competitionss = new ArrayList<Integer, String, Calendar>();
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
                    competitors.add(new CompetitorTeam(resultCompetitor
                            .getInt("idcompetitor"), resultCompetitor
                            .getString("name")));
                } else {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(resultCompetitor.getDate("closingdate"));
                    competitors.add(new CompetitorPlayer(resultCompetitor
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

        // Créer une structure contenant tout ce qu'il faut pour instancier les
        // competitions avec les compétiteurs et le faire maintenant.

        c.close();

        return competitions;
    }
}
