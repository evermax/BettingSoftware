package fr.uv1.tests.unit.DAO;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import fr.uv1.utils.DataBaseConnection;

public class CompetitionDAOTest {
    
    @Before
    public void removeEverythingFromDB() throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        PreparedStatement psRemove = connection.prepareStatement(" DELETE FROM competitionparticipants; DELETE FROM bet; DELETE FROM teammembers; DELETE FROM competitionranking; DELETE FROM competition; DELETE FROM competitor; DELETE FROM subscriber;");
        psRemove.executeQuery();
    }
    
    
    @Test
    public void test() {
        fail("Not yet implemented");
    }

}
