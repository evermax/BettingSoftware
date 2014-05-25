package fr.uv1.bettingServices.bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.ACompetitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.utils.DataBaseConnection;

public class CompetitorTeamDAO {
    public static CompetitorTeam persist(CompetitorTeam comp)
            throws SQLException {
        Connection connection = DataBaseConnection.getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement psPersist = connection
                    .prepareStatement("INSERT INTO Competitor(name, firstname, birthdate, isteam)  values (?, ?, ?, ?)");

            psPersist.setString(1, comp.getName());
            psPersist.setString(2, null);
            psPersist.setDate(3, null);
            psPersist.setBoolean(4, true);

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

            comp.setId(id);

            for (Competitor c : comp.getMembers()) {
                PreparedStatement psAddMember = connection
                        .prepareStatement("INSERT INTO teammembers(idteam, idcompetitor)  values (?, ?)");
                psAddMember.setInt(1, comp.getId());
                psAddMember.setInt(2, ((ACompetitor) c).getId());

                psAddMember.executeUpdate();

                psAddMember.close();
            }

            connection.commit();

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

    public static List<CompetitorTeam> findAll() throws SQLException,
            BadParametersException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competitor where isteam = true order by idcompetitor");
        ResultSet resultSet = psSelect.executeQuery();
        List<CompetitorTeam> competitorTeams = new ArrayList<CompetitorTeam>();
        while (resultSet.next()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(resultSet.getDate("birthdate"));
            competitorTeams.add(new CompetitorTeam(resultSet
                    .getInt("idcompetitor"), resultSet.getString("name")));
        }
        resultSet.close();
        psSelect.close();
        c.close();

        return competitorTeams;
    }

    /**
     * Fonction retournant la liste des compétiteurs appartenant à l'équipe dont
     * l'id est idTeam
     * 
     * @param idTeam
     *            id de l'équipe dont on souhaite trouver les membres
     * @return Objet ArrayList<Competitor> comprenant tous les membres de
     *         l'équipe.
     * @throws SQLException
     * @throws BadParametersException
     * @throws ExistingCompetitorException
     */
    public static List<Competitor> findPlayersOfTeam(int idTeam)
            throws SQLException, BadParametersException,
            ExistingCompetitorException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select idcompetitor, isteam from teammembers natural join competitor where idteam = ?");
        psSelect.setInt(1, idTeam);
        ResultSet resultSet = psSelect.executeQuery();
        List<Competitor> members = new ArrayList<Competitor>();

        // Pour chaque résultat obtenu, on cherche le compétiteur correspondant
        // dans la base de données et on l'ajoute à la liste members
        while (resultSet.next()) {
            int idCompetitor = resultSet.getInt("idcompetitor");
            boolean isTeam = resultSet.getBoolean("isteam");
            if (!isTeam)
                members.add(CompetitorPlayerDAO.findById(idCompetitor));
            else
                members.add(CompetitorTeamDAO.findById(idCompetitor));
        }
        resultSet.close();
        psSelect.close();
        c.close();

        return members;
    }

    private static CompetitorTeam findById(int id) throws SQLException,
            BadParametersException, ExistingCompetitorException {
        Connection c = DataBaseConnection.getConnection();
        PreparedStatement psSelect = c
                .prepareStatement("select * from competitor where idcompetitor = ?");
        psSelect.setInt(1, id);
        ResultSet resultSet = psSelect.executeQuery();
        CompetitorTeam team = null;
        while (resultSet.next()) {
            team = new CompetitorTeam(id, resultSet.getString("name"));
        }
        List<Competitor> members = findPlayersOfTeam(id);
        for (Competitor comp : members)
            team.addMember(comp);
        resultSet.close();
        psSelect.close();

        c.close();
        return team;
    }

    public static void update(CompetitorTeam competitorTeam)
            throws SQLException {
        // 1 - Get a database connection from the class 'DatabaseConnection'
        Connection c = DataBaseConnection.getConnection();

        // 2 - Creating a Prepared Statement with the SQL instruction.
        // The parameters are represented by question marks.
        PreparedStatement psUpdate = c
                .prepareStatement("update competitor set name=?, firstname=?, birthdate=?, isteam=? where idcompetitor=?");

        // 3 - Supplying values for the prepared statement parameters (question
        // marks).
        psUpdate.setString(1, competitorTeam.getName());
        psUpdate.setString(2, null);
        psUpdate.setString(3, null);
        psUpdate.setBoolean(4, true);
        psUpdate.setInt(6, competitorTeam.getId());

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
}
