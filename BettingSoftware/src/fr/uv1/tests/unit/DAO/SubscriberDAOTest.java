package fr.uv1.tests.unit.DAO;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.uv1.bettingServices.Subscriber;
import fr.uv1.bettingServices.bd.SubscriberDAO;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.DataBaseConnection;
import fr.uv1.utils.MyCalendar;

public class SubscriberDAOTest {

    private static Connection connection;
    
    @BeforeClass
    public static void openConnection() throws SQLException {
        connection = DataBaseConnection.getConnection();
    }
    
    @Before
    public void setMyCalendarToCurrentDate() throws SQLException {
        MyCalendar.setDate();
        PreparedStatement psRemove = connection.prepareStatement(" DELETE FROM competitionparticipants; DELETE FROM bet; DELETE FROM teammembers; DELETE FROM competitionranking; DELETE FROM competition; DELETE FROM competitor; DELETE FROM subscriber;");
        psRemove.executeUpdate();
        psRemove.close();
    }
    
    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }
    
    @Test(expected = SQLException.class)
    public void testPersistDeuxMemeSubscriber() throws BadParametersException, SubscriberException, SQLException {
        Subscriber s1 = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        Subscriber s2 = new Subscriber(new String("Duran"), new String("Miguel"),
                new String("worldChamp"), new GregorianCalendar(1991, 06, 31));
        SubscriberDAO.persist(s1);
        SubscriberDAO.persist(s2);
    }

}
