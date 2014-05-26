package fr.uv1.tests.unit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.uv1.bettingServices.BettingSoft;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.DataBaseConnection;
import fr.uv1.utils.MyCalendar;

public class BettingSoftTest {

    private BettingSoft bettingProgram;
    private static Connection connection;

    @BeforeClass
    public static void openConnection() throws SQLException {
        connection = DataBaseConnection.getConnection();
    }

    @Before
    public void setMyCalendarToCurrentDate() throws SQLException {
        MyCalendar.setDate();
        PreparedStatement psRemove = connection
                .prepareStatement(" DELETE FROM competitionparticipants; DELETE FROM bet; DELETE FROM teammembers; DELETE FROM competitionranking; DELETE FROM competition; DELETE FROM competitor; DELETE FROM subscriber;");
        psRemove.executeUpdate();
        psRemove.close();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test(expected = BadParametersException.class)
    public void testBettingProgramWithEmptyPassword()
            throws BadParametersException {
        new BettingSoft("");
    }

    @Test(expected = AuthenticationException.class)
    public void testBettingProgramSubscribeAuthenticationException()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        bettingProgram.subscribe("Bleu", "Jean", "BlueJeans", "13-03-1990",
                null);
    }

    @Test
    public void testBetting() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException,
            CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
                "BlueJeans", passSub1);
        System.out.println(bettingProgram.listCompetitions());
        System.out.println(bettingProgram
                .consultBetsCompetition("100mOlympique"));
    }

    @Test(expected = CompetitionException.class)
    public void testBettingNoSuchCompetitor() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException,
            CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        CompetitorPlayer c4 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudo", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c4,
                "BlueJeans", passSub1);
    }

    @Test(expected = CompetitionException.class)
    public void testBettingCompetitionClosed() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException,
            CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2013, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
                "BlueJeans", passSub1);
    }

    @Test(expected = AuthenticationException.class)
    public void testBettingIncorrectPassword() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException,
            CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        bettingProgram.subscribe("Bleu", "Jean", "BlueJeans", "13-03-1990",
                mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
                "BlueJeans", "Coucou");
    }

    @Test(expected = SubscriberException.class)
    public void testBettingNotEnoughToken() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException,
            CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 100, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
                "BlueJeans", passSub1);
    }

    @Test(expected = AuthenticationException.class)
    public void testBettingNoSuchSubscriber() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException,
            CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
                "BlueJeas", passSub1);
    }

    @Test(expected = BadParametersException.class)
    public void testBettingIncorrectNumberOfToken()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(-200, "100mOlympique", c1, c2, c3,
                "BlueJeans", passSub1);
    }

    @Test(expected = CompetitionException.class)
    public void testBettingSubscriberIsACompetitor()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bolt", "Usain",
                "BlueJeans", "21-07-1986", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        bettingProgram.addCompetition("100mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
                "BlueJeans", passSub1);
    }

    @Test(expected = AuthenticationException.class)
    public void testSettlePodiumIncorrectManagerPassword()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        String competitionName = "100mOlympique";
        bettingProgram.addCompetition(competitionName, new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, competitionName, c1, c2, c3,
                "BlueJeans", passSub1);
        MyCalendar.setDate(new MyCalendar(2017, 04, 13));
        bettingProgram.settlePodium(competitionName, c1, c2, c3, "Chameau");
    }

    @Test(expected = ExistingCompetitionException.class)
    public void testSettlePodiumCompetitionDoesNotExist()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        MyCalendar.setDate(new MyCalendar(2017, 04, 13));
        bettingProgram.settlePodium("Coucou", c1, c2, c3, mgr_pwd);
    }

    @Test(expected = CompetitionException.class)
    public void testSettlePodiumSameCompetitorOnPodium()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        String competitionName = "100mOlympique";
        bettingProgram.addCompetition(competitionName, new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, competitionName, c1, c2, c3,
                "BlueJeans", passSub1);
        MyCalendar.setDate(new MyCalendar(2017, 04, 13));
        bettingProgram.settlePodium(competitionName, c1, c2, c2, mgr_pwd);
    }

    @Test(expected = CompetitionException.class)
    public void testSettlePodiumNoCompetitorInCompetition()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        String competitionName = "100mOlympique";
        bettingProgram.addCompetition(competitionName, new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        MyCalendar.setDate(new MyCalendar(2017, 04, 13));
        bettingProgram.settlePodium(competitionName, c1, c2, c3, mgr_pwd);
    }

    @Test(expected = CompetitionException.class)
    public void testSettlePodiumCompetitionStillOpened()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans",
                "13-03-1990", mgr_pwd);
        bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
        Collection<Competitor> competitors = new ArrayList<Competitor>();
        CompetitorPlayer c1 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Bolt", "Usain", "21-07-1986", mgr_pwd);
        CompetitorPlayer c2 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Boyle", "Conan", "20-06-1985", mgr_pwd);
        CompetitorPlayer c3 = (CompetitorPlayer) bettingProgram
                .createCompetitor("Groudon", "Stolley", "13-03-1984", mgr_pwd);
        competitors.add(c1);
        competitors.add(c2);
        competitors.add(c3);
        String competitionName = "100mOlympique";
        bettingProgram.addCompetition(competitionName, new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        bettingProgram.betOnPodium(200, competitionName, c1, c2, c3,
                "BlueJeans", passSub1);
        bettingProgram.settlePodium(competitionName, c1, c2, c2, mgr_pwd);
    }
}
