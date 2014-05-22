package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.Subscriber;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.utils.DataBaseConnection;

public class CompetitorPlayerDAO {
    public static CompetitorPlayer persist(CompetitorPlayer comp)
            throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection
                    .prepareStatement("INSERT INTO Competitor(name, firstname, birthdate, isteam)  values (?, ?, ?, ?)");

            psPersist.setString(1, comp.getName());
            psPersist.setString(2, comp.getFirstName());
            psPersist.setDate(3, new java.sql.Date(comp.getBorndate()
                    .getTimeInMillis()));

            psPersist.setBoolean(4, false);

            psPersist.executeUpdate();

            psPersist.close();

            // Retrieving the value of the id with a request on the
            // sequence (competitor_idcompetitor_seq).
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

            comp.setId(id);

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

        return comp;
    }
    
    public static List<CompetitorPlayer> findAll() throws SQLException, BadParametersException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competitor order by idcompetitor");
        ResultSet resultSet = psSelect.executeQuery();
        List<CompetitorPlayer> competitorsPlayers = new ArrayList<CompetitorPlayer>();
        while (resultSet.next()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(resultSet.getDate("birthdate"));
            competitorsPlayers
                    .add(new CompetitorPlayer(resultSet.getInt("idcompetitor"),
                            resultSet.getString("name"),
                            resultSet.getString("firstname"),
                            cal));
        }
        resultSet.close();
        psSelect.close();
        c.close();

        return competitorsPlayers;
    }
    
    public static void update(CompetitorPlayer competitor) {
     // 1 - Get a database connection from the class 'DatabaseConnection'
        Connection c = DataBaseConnection.getConnection();

        // 2 - Creating a Prepared Statement with the SQL instruction.
        // The parameters are represented by question marks.
        PreparedStatement psUpdate = c
                .prepareStatement("update subscriber set name=?, firstname=?, birthdate=?, isteam=? where idsubscriber=?");

        // 3 - Supplying values for the prepared statement parameters (question
        // marks).
        psUpdate.setString(1, competitor.getName());
        psUpdate.setString(2, competitor.getFirstName());
        psUpdate.setDate(3, new java.sql.Date(competitor.getBorndate().getTimeInMillis()));
        psUpdate.setLong(4, competitor.getTokens());
        psUpdate.setInt(5, competitor.getId());
    }
}
