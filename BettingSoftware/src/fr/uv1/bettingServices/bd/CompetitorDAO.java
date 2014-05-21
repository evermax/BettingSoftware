package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.uv1.bettingServices.ACompetitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.utils.DataBaseConnection;

public class CompetitorDAO {
    public static ACompetitor persist(ACompetitor comp) throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        boolean isTeam = false;
        if ((comp instanceof CompetitorTeam)) {
            isTeam = true;
        }
        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection
                    .prepareStatement("INSERT INTO Competitor(name, firstname, birthdate, isteam)  values (?, ?, ?, ?)");

            psPersist.setString(1, comp.getName());
            if (!isTeam) {
                psPersist
                        .setString(2, ((CompetitorPlayer) comp).getFirstName());
                psPersist.setDate(3, new java.sql.Date(
                        ((CompetitorPlayer) comp).getBorndate()
                                .getTimeInMillis()));
            } else {
                psPersist.setString(2, null);
                psPersist.setDate(3, null);
            }
            psPersist.setBoolean(4, isTeam);

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
}
