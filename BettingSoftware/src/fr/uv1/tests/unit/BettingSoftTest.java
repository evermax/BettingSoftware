package fr.uv1.tests.unit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

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

public class BettingSoftTest {

    private BettingSoft bettingProgram;

    @Test(expected = BadParametersException.class)
    public void testBettingSoftWithEmptyPassword()
            throws BadParametersException {
        new BettingSoft("");
    }

    @Test(expected = AuthenticationException.class)
    public void testBettingSoftSubscribeAuthenticationException()
            throws BadParametersException, AuthenticationException,
            ExistingSubscriberException, SubscriberException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        bettingProgram.subscribe("Bleu", "Jean", "BlueJeans", "13-03-1990",
                null);
    }

    @Test
    public void testBettingSoft() throws BadParametersException,
            AuthenticationException, ExistingSubscriberException,
            SubscriberException, ExistingCompetitionException, CompetitionException {
        String mgr_pwd = "Kangourou";
        bettingProgram = new BettingSoft(mgr_pwd);
        String passSub1 = bettingProgram.subscribe("Bleu", "Jean", "BlueJeans", "13-03-1990",
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
                "BlueJeans", passSub1);
        System.out.println(bettingProgram.listCompetitions());
        System.out.println(bettingProgram.consultBetsCompetition("100mOlympique"));
    }
}
