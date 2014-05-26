package fr.uv1.tests.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.utils.MyCalendar;

public class CompetitorTeamTest {

    @Test
    public void testCompetitorTeam() throws BadParametersException {
        CompetitorTeam t = new CompetitorTeam("OM");
        assertTrue(t.getName() == "OM");
        assertTrue(t.hasValidName());
    }

    @Test(expected = BadParametersException.class)
    public void testNullTeamNameCompetitorTeam() throws BadParametersException {
        new CompetitorTeam(null);
    }

    @Test(expected = BadParametersException.class)
    public void testInvalidTeamNameCompetitorTeam()
            throws BadParametersException {
        new CompetitorTeam("");
    }

    @Test
    public void testAddMember() throws BadParametersException,
            ExistingCompetitorException {
        CompetitorTeam t = new CompetitorTeam("OM");
        Competitor c1 = new CompetitorPlayer("Bakke", "Bo", new MyCalendar(
                1973, 11, 30));
        Competitor c2 = new CompetitorPlayer("Duran", "Dil", new MyCalendar(
                1990, 03, 27));
        t.addMember(c1);
        t.addMember(c2);
        assertTrue(t.getMembers().contains(c1));
        assertTrue(t.getMembers().contains(c2));
    }

    @Test(expected = BadParametersException.class)
    public void testAddInvalidMember() throws BadParametersException,
            ExistingCompetitorException {
        CompetitorTeam t = new CompetitorTeam("OM");
        t.addMember(null);
    }

    @Test(expected = ExistingCompetitorException.class)
    public void testAddDuplicateMember() throws BadParametersException,
            ExistingCompetitorException {
        CompetitorTeam t = new CompetitorTeam("OM");
        Competitor c1 = new CompetitorPlayer("Bakke", "Bo", new MyCalendar(
                1973, 11, 30));
        Competitor c2 = new CompetitorPlayer("Duran", "Dil", new MyCalendar(
                1990, 03, 27));
        Competitor c3 = new CompetitorPlayer("Bakke", "Bo", new MyCalendar(
                1973, 11, 30));
        t.addMember(c1);
        t.addMember(c2);
        t.addMember(c3);
    }

    @Test
    public void testDeleteMember() throws BadParametersException,
            ExistingCompetitorException {
        CompetitorTeam t = new CompetitorTeam("OM");
        Competitor c1 = new CompetitorPlayer("Bakke", "Bo", new MyCalendar(
                1973, 11, 30));
        Competitor c2 = new CompetitorPlayer("Duran", "Dil", new MyCalendar(
                1990, 03, 27));
        Competitor c3 = new CompetitorPlayer("Duran", "Dil", new MyCalendar(
                1990, 03, 27));
        t.addMember(c1);
        t.addMember(c2);
        assertTrue(t.getMembers().contains(c1));
        assertTrue(t.getMembers().contains(c2));
        t.deleteMember(c1);
        t.deleteMember(c3);
    }

    @Test(expected = BadParametersException.class)
    public void testDeleteInvalidMember() throws BadParametersException,
            ExistingCompetitorException {
        CompetitorTeam t = new CompetitorTeam("OM");
        Competitor c1 = new CompetitorPlayer("Bakke", "Bo", new MyCalendar(
                1973, 11, 30));
        t.addMember(c1);
        assertTrue(t.getMembers().contains(c1));
        t.deleteMember(null);
    }

    @Test(expected = ExistingCompetitorException.class)
    public void testDeleteUnregisteredMember() throws BadParametersException,
            ExistingCompetitorException {
        CompetitorTeam t = new CompetitorTeam("OM");
        Competitor c1 = new CompetitorPlayer("Bakke", "Bo", new MyCalendar(
                1973, 11, 30));
        Competitor c2 = new CompetitorPlayer("Duran", "Dil", new MyCalendar(
                1990, 03, 27));
        Competitor c3 = new CompetitorPlayer("Loen", "Sjur", new MyCalendar(
                1980, 02, 14));
        t.addMember(c1);
        t.addMember(c2);
        assertTrue(t.getMembers().contains(c1));
        assertTrue(t.getMembers().contains(c2));
        t.deleteMember(c1);
        t.deleteMember(c3);
    }
}
