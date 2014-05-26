package fr.uv1.tests.unit;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Test;

import fr.uv1.bettingServices.Competition;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.utils.MyCalendar;

public class CompetitionTest {

    @Test
    public void testCompetition() throws BadParametersException,
            CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer cp2 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        CompetitorPlayer cp3 = new CompetitorPlayer("Bakke", "Bo",
                new MyCalendar(1973, 11, 30));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);
        competitors.add(cp2);
        competitors.add(cp3);

        Competition c = new Competition("Curling_Tournament",
                new GregorianCalendar(2016, 05, 14), competitors);

        assertTrue(c.getName() == "Curling_Tournament");
        assertTrue(c.getClosingDate().equals(
                new GregorianCalendar(2016, 05, 14)));
        assertTrue(c.areCompetitors(cp1, cp2, cp3));
    }

    @Test(expected = CompetitionException.class)
    public void testDoubleCompetitorCompetition()
            throws BadParametersException, CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer cp2 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        CompetitorPlayer cp3 = new CompetitorPlayer("Bakke", "Bo",
                new MyCalendar(1973, 11, 30));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);
        competitors.add(cp2);
        competitors.add(cp3);
        competitors.add(cp2);

        new Competition("Curling_Tournament", new GregorianCalendar(2016, 05,
                14), competitors);
    }

    @Test(expected = CompetitionException.class)
    public void testDoubleCompetitorWithDifferentInstanceCompetition()
            throws BadParametersException, CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer cp2 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        CompetitorPlayer cp3 = new CompetitorPlayer("Bakke", "Bo",
                new MyCalendar(1973, 11, 30));
        CompetitorPlayer cp4 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);
        competitors.add(cp2);
        competitors.add(cp3);
        competitors.add(cp4);

        assertTrue(cp2.equals(cp4));

        new Competition("Curling_Tournament", new GregorianCalendar(2016, 05,
                14), competitors);
    }

    @Test(expected = BadParametersException.class)
    public void testNullNameCompetition() throws BadParametersException,
            CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer cp2 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        CompetitorPlayer cp3 = new CompetitorPlayer("Bakke", "Bo",
                new MyCalendar(1973, 11, 30));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);
        competitors.add(cp2);
        competitors.add(cp3);

        new Competition(null, new GregorianCalendar(2016, 05, 14), competitors);
    }

    @Test(expected = BadParametersException.class)
    public void testNullClosingDateCompetition() throws BadParametersException,
            CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer cp2 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        CompetitorPlayer cp3 = new CompetitorPlayer("Bakke", "Bo",
                new MyCalendar(1973, 11, 30));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);
        competitors.add(cp2);
        competitors.add(cp3);

        new Competition("Curling_Tournament", null, competitors);
    }

    @Test(expected = CompetitionException.class)
    public void testPassedClosingDateCompetition()
            throws BadParametersException, CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        CompetitorPlayer cp2 = new CompetitorPlayer("Loen", "Sjur",
                new MyCalendar(1980, 02, 14));
        CompetitorPlayer cp3 = new CompetitorPlayer("Bakke", "Bo",
                new MyCalendar(1973, 11, 30));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);
        competitors.add(cp2);
        competitors.add(cp3);

        new Competition("Curling_Tournament", new MyCalendar(1998, 10, 04),
                competitors);
    }

    @Test(expected = BadParametersException.class)
    public void testNullCompetitorsCompetition() throws BadParametersException,
            CompetitionException {
        new Competition("Curling_Tournament", new MyCalendar(2016, 05, 14),
                null);
    }

    @Test(expected = CompetitionException.class)
    public void testNotEnoughCompetitorsCompetition()
            throws BadParametersException, CompetitionException {
        CompetitorPlayer cp1 = new CompetitorPlayer("Duran", "Dil",
                new MyCalendar(1990, 03, 27));
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors.add(cp1);

        new Competition("Curling_Tournament", new MyCalendar(2016, 05, 14),
                competitors);
    }
}
