package fr.uv1.bettingServices;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import fr.uv1.bettingServices.bd.BetDAO;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.MyCalendar;

public class Competition {

    private static final String REGEX_COMPNAME = new String("[a-zA-Z0-9\\-_]*");

    private String name;
    private Calendar closingDate;
    private Integer id;
    /**
     * Bool�en indiquant si la comp�tition a �t� sold�e ou non
     */
    private boolean settled;

    /**
     * Liste des participants � la comp�tition
     */
    private Collection<Competitor> competitors;

    /**
     * Liste de tous les joueurs participant � la comp�tition, comprenant les
     * membres des �quipes
     */
    private Collection<Competitor> players;

    /**
     * Liste de tous les paris effectu�s sur cette comp�tition
     */
    private Collection<Bet> bets;

    /**
     * Constructeur de la classe competition
     * 
     * @param name
     *            est le nom que l'on souhaite donner � la comp�tition
     * @param closingDate
     *            est la date de fermeture de la comp�tition
     * @param competitors
     *            est la liste de comp�titeurs participant � la comp�tition
     * @throws BadParametersException
     *             est lev�e lorsque le nom est nulle ou ne correspond pas aux
     *             contraintes, lorsque la date est nulle ou lorsque la liste de
     *             comp�titeurs est nulle
     * @throws CompetitionException
     */

    public Competition(String name, Calendar closingDate,
            Collection<Competitor> competitors) throws BadParametersException,
            CompetitionException {
        if (name == null || !name.matches(REGEX_COMPNAME)
                || closingDate == null || competitors == null) {
            throw new BadParametersException();
        }
        // Exception si la date de cloture est d�j� pass�e
        if (closingDate.before(MyCalendar.getDate())) {
            throw new CompetitionException();
        }
        // Exception s'il y a moins de 2 comp�titeurs dans la liste
        if (competitors.size() < 2) {
            throw new CompetitionException();
        }

        /*
         * V�rification de la pr�sence de doublons dans la liste des
         * comp�titeurs On v�rifie aussi si un joueur appartient � deux �quipes
         * participant � la comp�tition
         */
        ArrayList<Competitor> distinctCompetitors = new ArrayList<Competitor>();
        for (Competitor c : competitors) {
            if (!distinctCompetitors.contains(c)) {
                distinctCompetitors.add(c);
                if (c instanceof CompetitorTeam) {
                    for (Competitor c1 : ((CompetitorTeam) c).getMembers()) {
                        if (!distinctCompetitors.contains(c1)) {
                            distinctCompetitors.add(c1);
                        } else {
                            throw new CompetitionException();
                        }
                    }
                }
            } else {
                throw new CompetitionException();
            }
        }

        this.players = distinctCompetitors;

        this.name = name;
        this.closingDate = closingDate;
        this.settled = false;
        this.competitors = competitors;
        this.bets = new ArrayList<Bet>();
    }

    public Competition(int id, String name, Calendar closingDate,
            Collection<Competitor> competitors) throws BadParametersException,
            CompetitionException {
        this(name, closingDate, competitors);
        this.id = id;
        try {
            this.bets = BetDAO.findByCompetition(this);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public Calendar getClosingDate() {
        return closingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSettled() {
        return settled;
    }

    public boolean isClosed() {
        return closingDate.before(MyCalendar.getDate());
    }

    public Collection<Competitor> getCompetitors() {
        return competitors;
    }

    public boolean isAParticipant(Competitor comp) {
        return players.contains(comp);
    }

    public boolean areCompetitors(Competitor... comps) {
        for (Competitor comp : comps) {
            if (!competitors.contains(comp)) {
                return false;
            }
        }
        return true;
    }

    public void deleteCompetitor(Competitor comp) throws CompetitionException,
            ExistingCompetitorException {
        if (isClosed() || competitors.size() <= 2) {
            throw new CompetitionException();
        }

        if (!isAParticipant(comp)) {
            throw new ExistingCompetitorException();
        }

        competitors.remove(comp);
    }

    public Bet betOnWinner(Subscriber s, String pwdSubs, Competitor winner,
            long numberTokens) throws AuthenticationException,
            CompetitionException, BadParametersException, SubscriberException {
        // On v�rifie que la comp�tition n'est pas termin�e
        if (isClosed())
            throw new CompetitionException();

        // On v�rifie que le comp�titeur sur lequel le joueur parie participe �
        // la comp�tition
        if (!areCompetitors(winner))
            throw new CompetitionException();

        // On v�rifie que le joueur ne participe pas � la comp�tition
        if (s.participates(this)) {
            throw new CompetitionException();
        }
        Bet b = new Bet(numberTokens, s, this, winner);
        s.debitTokens(numberTokens);

        this.bets.add(b);
        return b;
    }

    public Bet betOnPodium(Subscriber s, String pwdSubs, Competitor winner,
            Competitor second, Competitor third, long numberTokens)
            throws AuthenticationException, CompetitionException,
            BadParametersException, SubscriberException {
        // On v�rifie que la comp�tition n'est pas termin�e
        if (isClosed())
            throw new CompetitionException();

        // On v�rifie que les comp�titeurs sur lesquels le joueur parie
        // participent � la comp�tition
        if (!areCompetitors(winner, second, third))
            throw new CompetitionException();

        // On v�rifie que le joueur ne participe pas � la comp�tition
        if (s.participates(this)) {
            throw new CompetitionException();
        }

        // On d�bite le nombre de jetons pari�s du compte du joueur
        Bet b = new Bet(numberTokens, s, this, winner, second, third);
        s.debitTokens(numberTokens);

        this.bets.add(b);
        return b;
    }

    public void settleWinner(Competitor winner) throws AuthenticationException,
            CompetitionException {
        if (!isClosed())
            throw new CompetitionException();
        if (!areCompetitors(winner))
            throw new CompetitionException();
        // Liste des vainqueurs associ�s au nombre de jetons qu'ils ont pari�s
        Hashtable<Subscriber, Long> tokensPerWinner = new Hashtable<Subscriber, Long>();
        // Nombre total de jetons pari�s
        long totalTokens = 0;
        // Nombre total de jetons pari�s sur la vainqueur
        long totalTokensOnWinner = 0;
        for (Bet b : bets) {
            if (!b.isBetOnPodium()) {
                totalTokens += b.getTokenNumber();
                if (b.getWinner().equals(winner)) {
                    totalTokensOnWinner += b.getTokenNumber();
                    Subscriber s = b.getSubscriber();
                    if (tokensPerWinner.containsKey(s)) {
                        tokensPerWinner.put(s,
                                tokensPerWinner.get(s) + b.getTokenNumber());
                    } else {
                        tokensPerWinner.put(s, b.getTokenNumber());
                    }
                }
            }
        }
        if (!tokensPerWinner.isEmpty()) {
            Set<Subscriber> winners = tokensPerWinner.keySet();
            for (Subscriber w : winners) {
                long redistributedToken = (totalTokens * tokensPerWinner.get(w))
                        / totalTokensOnWinner;
                try {
                    w.creditTokens(redistributedToken);
                } catch (BadParametersException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Bet b : bets) {
                if (!b.isBetOnPodium()) {
                    try {
                        b.getSubscriber().creditTokens(b.getTokenNumber());
                    } catch (BadParametersException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void settlePodium(Competitor winner, Competitor second,
            Competitor third) throws CompetitionException,
            AuthenticationException {
        if (!isClosed())
            throw new CompetitionException();
        if (winner.equals(second) || winner.equals(third)
                || second.equals(third))
            throw new CompetitionException();
        if (!areCompetitors(winner, second, third))
            throw new CompetitionException();
        // Liste des vainqueurs associ�s au nombre de jetons qu'ils ont pari�s
        Hashtable<Subscriber, Long> tokensPerWinner = new Hashtable<Subscriber, Long>();
        // Nombre total de jetons pari�s
        long totalTokens = 0;
        // Nombre total de jetons pari�s sur la vainqueur
        long totalTokensOnWinner = 0;
        for (Bet b : bets) {
            if (b.isBetOnPodium()) {
                totalTokens += b.getTokenNumber();
                if (b.getWinner().equals(winner)
                        && b.getSecond().equals(second)
                        && b.getThird().equals(third)) {
                    totalTokensOnWinner += b.getTokenNumber();
                    Subscriber s = b.getSubscriber();
                    if (tokensPerWinner.containsKey(s)) {
                        tokensPerWinner.put(s,
                                tokensPerWinner.get(s) + b.getTokenNumber());
                    } else {
                        tokensPerWinner.put(s, b.getTokenNumber());
                    }
                }
            }
        }

        if (!tokensPerWinner.isEmpty()) {
            Set<Subscriber> winners = tokensPerWinner.keySet();
            for (Subscriber w : winners) {
                long redistributedToken = (totalTokens * tokensPerWinner.get(w))
                        / totalTokensOnWinner;
                try {
                    w.creditTokens(redistributedToken);
                } catch (BadParametersException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Bet b : bets) {
                if (b.isBetOnPodium()) {
                    try {
                        b.getSubscriber().creditTokens(b.getTokenNumber());
                    } catch (BadParametersException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void deleteBets(Subscriber s) {
        Iterator<Bet> iter = bets.iterator();
        while (iter.hasNext()) {
            Bet bet = iter.next();
            if (bet.getSubscriber().equals(s)) {
                try {
                    BetDAO.delete(bet);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                iter.remove();
            }
        }
    }

    public ArrayList<String> consultBets() {
        ArrayList<String> betsString = new ArrayList<String>();
        for (Bet bet : bets) {
            betsString.add(bet.toString());
        }
        return betsString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Competition other = (Competition) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "\nCompetition [name = " + name + "\nclosing date = "
                + closingDate.get(GregorianCalendar.DAY_OF_MONTH) + "-"
                + closingDate.get(GregorianCalendar.MONTH) + "-"
                + closingDate.get(GregorianCalendar.YEAR) + "\nsettled = "
                + settled + "\ncompetitors = " + competitors + "]\n";
    }
}
