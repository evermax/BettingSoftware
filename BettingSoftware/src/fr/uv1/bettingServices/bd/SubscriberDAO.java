package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.uv1.bettingServices.Subscriber;
import fr.uv1.utils.DataBaseConnection;

public class SubscriberDAO {

    public static Subscriber persist(Subscriber s) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        try {
            c.setAutoCommit(false);
            PreparedStatement psPersist = c
                    .prepareStatement("INSERT INTO subscriber(firstname, lastname, username, password, tokens, birthdate)  values (?, ?, ?, ?, ?, ?)");

            psPersist.setString(1, s.getFirstname());
            psPersist.setString(2, s.getLastname());
            psPersist.setString(3, s.getUsername());
            psPersist.setString(4, s.getPassword());
            psPersist.setLong(5, s.getTokens());
            psPersist.setDate(6, new java.sql.Date(s.getBirthdate()
                    .getTimeInMillis()));

            psPersist.executeUpdate();

            psPersist.close();

            // Retrieving the value of the id with a request on the
            // sequence (subscribers_id_seq).
            PreparedStatement psIdValue = c
                    .prepareStatement("select currval('subscriber_idsubscriber_seq') as value_id");
            ResultSet resultSet = psIdValue.executeQuery();
            Integer id = null;
            while (resultSet.next()) {
                id = resultSet.getInt("value_id");
            }
            resultSet.close();
            psIdValue.close();
            c.commit();
            s.setId(id);
        } catch (SQLException e) {
            try {
                c.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            c.setAutoCommit(true);
            throw e;
        }

        c.setAutoCommit(true);
        c.close();

        return s;
    }

    public static List<Subscriber> findAll() throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from subscriber order by idsubscriber");
        ResultSet resultSet = psSelect.executeQuery();
        List<Subscriber> subscribers = new ArrayList<Subscriber>();
        while (resultSet.next()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(resultSet.getDate("birthdate"));
            subscribers.add(new Subscriber(resultSet.getInt("idsubscriber"),
                    resultSet.getString("firstname"), resultSet
                            .getString("lastname"), resultSet
                            .getString("username"), resultSet
                            .getString("password"),
                    resultSet.getLong("tokens"), cal));
        }
        resultSet.close();
        psSelect.close();
        c.close();

        return subscribers;
    }
    
    public static void changeTokens(Subscriber s, long tokens) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psUpdateToken = c.prepareStatement("update subscriber set tokens = tokens + ? where idSubscriber = ?");
        psUpdateToken.setLong(1, tokens);
        psUpdateToken.setInt(2, s.getId());
        
        psUpdateToken.executeUpdate();
        psUpdateToken.close();
        c.close();
    }

    public static Subscriber findById(int id) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from subscriber where idsubscriber = ?");
        psSelect.setInt(1, id);
        ResultSet resultSet = psSelect.executeQuery();
        Subscriber subscriber = null;
        while (resultSet.next()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(resultSet.getDate("birthdate"));
            subscriber = new Subscriber(resultSet.getInt("idsubscriber"),
                    resultSet.getString("firstname"),
                    resultSet.getString("lastname"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getLong("tokens"), cal);
        }
        resultSet.close();
        psSelect.close();
        c.close();
        return subscriber;
    }

    public static void update(Subscriber subscriber) throws SQLException {
        // 1 - Get a database connection from the class 'DatabaseConnection'
        Connection c = DataBaseConnection.getConnection();

        // 2 - Creating a Prepared Statement with the SQL instruction.
        // The parameters are represented by question marks.
        PreparedStatement psUpdate = c
                .prepareStatement("update subscriber set firstname=?, lastname=?, username=?, tokens=?, birthdate=? where idsubscriber=?");

        // 3 - Supplying values for the prepared statement parameters (question
        // marks).
        psUpdate.setString(1, subscriber.getFirstname());
        psUpdate.setString(2, subscriber.getLastname());
        psUpdate.setString(3, subscriber.getUsername());
        psUpdate.setLong(4, subscriber.getTokens());
        psUpdate.setDate(5, new java.sql.Date(subscriber.getBirthdate()
                .getTimeInMillis()));
        psUpdate.setInt(6, subscriber.getId());

        // Executing the prepared statement object among the database.
        // If needed, a return value (int) can be obtained. It contains
        // how many rows of a table were updated.
        // int nbRows = psUpdate.executeUpdate();
        psUpdate.executeUpdate();

        // 6 - Closing the Prepared Statement.
        psUpdate.close();

        // 7 - Closing the database connection.
        c.close();
    }

    public static void delete(Subscriber subscriber) throws SQLException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psUpdate = c
                .prepareStatement("delete from subscriber where idsubscriber=?");
        psUpdate.setInt(1, subscriber.getId());
        psUpdate.executeUpdate();
        psUpdate.close();
        c.close();
    }
}
