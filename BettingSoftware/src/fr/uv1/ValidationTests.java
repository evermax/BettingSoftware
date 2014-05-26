package fr.uv1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.uv1.bettingServices.*;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.tests.validation.withoutPersistence.FirstIncrementValidationTests;
import fr.uv1.tests.validation.withoutPersistence.SecondIncrementValidationTests;
import fr.uv1.utils.DataBaseConnection;

@SuppressWarnings("unused")
public class ValidationTests {
    private static Connection connection;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
        PreparedStatement psRemove;
        try {
            connection = DataBaseConnection.getConnection();
            psRemove = connection.prepareStatement(" DELETE FROM competitionparticipants; DELETE FROM bet; DELETE FROM teammembers; DELETE FROM competitionranking; DELETE FROM competition; DELETE FROM competitor; DELETE FROM subscriber;");
            psRemove.executeUpdate();
            psRemove.close();
            connection.close();
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

		new SecondIncrementValidationTests() {

			@Override
			public Betting plugToBetting() {
				try {
					return new BettingSoft("ilesCaimans");
				} catch (BadParametersException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String getManagerPassword() {
				return "ilesCaimans";
			}
		};

	}
}