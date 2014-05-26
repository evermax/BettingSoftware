package fr.uv1.tests.unit.DAO;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    
    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
