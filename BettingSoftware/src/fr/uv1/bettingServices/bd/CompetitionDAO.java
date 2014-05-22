package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.uv1.bettingServices.Competition;
import fr.uv1.utils.DataBaseConnection;

public class CompetitionDAO {
    public static Competition persist(Competition competition) throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        
        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection.prepareStatement("INSERT INTO competition(name, closingdate, settled) values (?, ?, ?)");
            psPersist.setString(1, competition.getName());
            psPersist.setDate(2, new java.sql.Date(competition.getClosingDate().getTimeInMillis()));
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
}
