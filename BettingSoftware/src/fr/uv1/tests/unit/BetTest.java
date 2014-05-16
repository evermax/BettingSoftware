package fr.uv1.tests.unit;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.Test;

import fr.uv1.bettingServices.Bet;
import fr.uv1.bettingServices.CompetitorPlayer;
import fr.uv1.bettingServices.Subscriber;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.SubscriberException;

public class BetTest {

    private Bet bet;
    
    @Test
    public void testBet() throws BadParametersException, SubscriberException {
        Subscriber sub = new Subscriber("Bleu", "Jean", "BlueJeans", new GregorianCalendar(1990, 02, 04));
        CompetitorPlayer winner = new CompetitorPlayer("Bolt", "Usain", new GregorianCalendar(1984, 02, 04));
        bet = new Bet(200, sub, "100mOlympique", winner);
        assertTrue(bet.getTokenNumber() == 200);
        assertTrue(bet.getCompetitionName().equals("100mOlympique"));
        assertTrue(bet.getWinner().equals(winner));
        assertTrue(bet.getSubscriber().equals(sub));
    }
}
