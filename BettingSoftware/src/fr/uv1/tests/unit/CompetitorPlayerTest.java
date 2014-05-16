package fr.uv1.tests.unit;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.CompetitorTeam;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.utils.MyCalendar;

public class CompetitorPlayerTest {

    @Test
    public void testCompetitorPlayer() throws BadParametersException {
        CompetitorPlayer p = new CompetitorPlayer("Duran", "Miguel",
                new MyCalendar(1990, 03, 27));
        assertTrue(p.getFirstName() == "Miguel");
        assertTrue(p.getName() == "Duran");
        assertTrue(p.getBorndate().get(Calendar.DAY_OF_MONTH) == 27);
        assertTrue(p.getBorndate().get(Calendar.MONTH) == 02); // 02 car dans
                                                               // Calendar le
                                                               // premier mois
                                                               // est 0
        assertTrue(p.getBorndate().get(Calendar.YEAR) == 1990);
    }

    @Test(expected = BadParametersException.class)
    public void testNullLastNameCompetitorPlayer()
            throws BadParametersException {
        new CompetitorPlayer(null, "Miguel", new MyCalendar(1990, 03, 27));
    }

    @Test(expected = BadParametersException.class)
    public void testNullFirstNameCompetitorPlayer()
            throws BadParametersException {
        new CompetitorPlayer("Duran", null, new MyCalendar(1990, 03, 27));
    }

    @Test(expected = BadParametersException.class)
    public void testNullBorndateCompetitorPlayer()
            throws BadParametersException {
        new CompetitorPlayer("Duran", "Miguel", null);
    }

    @Test(expected = BadParametersException.class)
    public void testInvalidLastNameCompetitorPlayer()
            throws BadParametersException {
        new CompetitorPlayer("", "Miguel", new MyCalendar(1990, 03, 27));
    }

    @Test(expected = BadParametersException.class)
    public void testInvalidFirstNameCompetitorPlayer()
            throws BadParametersException {
        new CompetitorPlayer("Durand", "", new MyCalendar(1990, 03, 27));
    }

    @Test
    public void testEquals() throws BadParametersException {
        CompetitorPlayer p = new CompetitorPlayer("Duran", "Miguel",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer q = new CompetitorPlayer("Duran", "Miguel",
                new MyCalendar(1990, 03, 27));
        assertTrue(p.equals(q));
    }

    @Test
    public void testDifferentEquals() throws BadParametersException {
        CompetitorPlayer p = new CompetitorPlayer("Duran", "Miguel",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer q = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        assertFalse(p.equals(q));
    }

    @Test
    public void testNullEquals() throws BadParametersException {
        CompetitorPlayer p = new CompetitorPlayer("Duran", "Miguel",
                new MyCalendar(1990, 03, 27));
        assertFalse(p.equals(null));
    }

    @Test
    public void testDifferentClassEquals() throws BadParametersException {
        CompetitorPlayer p = new CompetitorPlayer("Duran", "Miguel",
                new MyCalendar(1990, 03, 27));
        CompetitorTeam t = new CompetitorTeam("OM");
        assertFalse(p.equals(t));
    }

}
