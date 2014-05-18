package fr.uv1.bettingServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Set;

import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.MyCalendar;

public class Competition {

    private static final String REGEX_COMPNAME = new String("[a-zA-Z0-9\\-_]*");

    private String name;
    private Calendar closingDate;
    /**
     * Booléen indiquant si la compétition a été soldée ou non
     */
    private boolean settled;

    /**
     * Liste des participants à la compétition
     */
    private Collection<Competitor> competitors;

    /**
     * Liste de tous les joueurs participant à la compétition, comprenant les
     * membres des équipes
     */
    private Collection<Competitor> players;

    /**
     * Liste de tous les paris effectués sur cette compétition
     */
    private Collection<Bet> bets;

    /**
     * Constructeur de la classe competition
     * 
     * @param name
     *            est le nom que l'on souhaite donner à la compétition
     * @param closingDate
     *            est la date de fermeture de la compétition
     * @param competitors
     *            est la liste de compétiteurs participant à la compétition
     * @throws BadParametersException
     *             est levée lorsque le nom est nulle ou ne correspond pas aux
     *             contraintes, lorsque la date est nulle ou lorsque la liste de
     *             compétiteurs est nulle
     * @throws CompetitionException
     */

    public Competition(String name, Calendar closingDate,
            Collection<Competitor> competitors) throws BadParametersException,
            CompetitionException {
        if (name == null || !name.matches(REGEX_COMPNAME)
                || closingDate == null || competitors == null) {
            throw new BadParametersException();
        }
        // Exception si la date de cloture est déjà passée
        if (closingDate.before(MyCalendar.getDate())) {
            throw new CompetitionException();
        }
        // Exception s'il y a moins de 2 compétiteurs dans la liste
        if (competitors.size() < 2) {
            throw new CompetitionException();
        }

        /*
         * Vérification de la présence de doublons dans la liste des
         * compétiteurs On vérifie aussi si un joueur appartient à deux équipes
         * participant à la compétition
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

    public String getName() {
        return name;
    }

    public Calendar getClosingDate() {
        return closingDate;
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

    public boolean isACompetitor(Competitor comp) {
        return competitors.contains(comp);
    }

    public boolean areCompetitors(Competitor comp1, Competitor comp2,
            Competitor comp3) {
        return (competitors.contains(comp1) && competitors.contains(comp2) && competitors
                .contains(comp3));
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
        // On authentifie le joueur
        s.authenticateSubscriber(pwdSubs);
        // On vérifie que la compétition n'est pas terminée
        if (isClosed())
            throw new CompetitionException();

        // On vérifie que le compétiteur sur lequel le joueur parie participe à
        // la compétition
        if (!isACompetitor(winner))
            throw new CompetitionException();

        // On vérifie que le joueur ne participe pas à la compétition
        if (s.participates(this)) {
            throw new CompetitionException();
        }
        Bet b = s.betOnWinner(winner, numberTokens, this.name);

        this.bets.add(b);
        return b;
    }

    public Bet betOnPodium(Subscriber s, String pwdSubs, Competitor winner,
            Competitor second, Competitor third, long numberTokens)
            throws AuthenticationException, CompetitionException,
            BadParametersException, SubscriberException {
        // On authentifie le joueur
        s.authenticateSubscriber(pwdSubs);
        // On vérifie que la compétition n'est pas terminée
        if (isClosed())
            throw new CompetitionException();

        // On vérifie que les compétiteurs sur lesquels le joueur parie
        // participent à la compétition
        if (!areCompetitors(winner, second, third))
            throw new CompetitionException();

        // On vérifie que le joueur ne participe pas à la compétition
        if (s.participates(this)) {
            throw new CompetitionException();
        }

        // On débite le nombre de jetons pariés du compte du joueur
        Bet b = s.betOnPodium(winner, second, third, numberTokens, this.name);

        this.bets.add(b);
        return b;
    }

    public void settleWinner(Competitor winner, BettingSoft bettingProgram,
            String managerPassword) throws AuthenticationException,
            CompetitionException {
        if (!isClosed())
            throw new CompetitionException();
        if (!isACompetitor(winner))
            throw new CompetitionException();
        // Liste des vainqueurs associés au nombre de jetons qu'ils ont pariés
        Hashtable<Subscriber, Long> tokensPerWinner = new Hashtable<Subscriber, Long>();
        // Nombre total de jetons pariés
        long totalTokens = 0;
        // Nombre total de jetons pariés sur la vainqueur
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
        Set<Subscriber> winners = tokensPerWinner.keySet();
        for (Subscriber w : winners) {
            long redistributedToken = (totalTokens * tokensPerWinner.get(w))
                    / totalTokensOnWinner;
            try {
                bettingProgram.creditSubscriber(w.getUsername(), redistributedToken,
                        managerPassword);
            } catch (ExistingSubscriberException e) {
                e.printStackTrace();
            } catch (BadParametersException e) {
                e.printStackTrace();
            }
        }
        for (Bet b : bets) {
            if (!b.isBetOnPodium()) {
                bets.remove(b);
            }
        }
    }

    public void settlePodium(Competitor winner, Competitor second,
            Competitor third, BettingSoft bettingProgram, String managerPassword)
            throws CompetitionException, AuthenticationException {
        if (!isClosed())
            throw new CompetitionException();
        if (winner.equals(second) || winner.equals(third)
                || second.equals(third))
            throw new CompetitionException();
        if (!areCompetitors(winner, second, third))
            throw new CompetitionException();
        // Liste des vainqueurs associés au nombre de jetons qu'ils ont pariés
        Hashtable<Subscriber, Long> tokensPerWinner = new Hashtable<Subscriber, Long>();
        // Nombre total de jetons pariés
        long totalTokens = 0;
        // Nombre total de jetons pariés sur la vainqueur
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
                    bettingProgram.creditSubscriber(w.getUsername(),
                            redistributedToken, managerPassword);
                } catch (ExistingSubscriberException e) {
                    e.printStackTrace();
                } catch (BadParametersException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Bet b : bets) {
                if (b.isBetOnPodium()) {
                    try {
                        bettingProgram.creditSubscriber(b.getSubscriber()
                                .getUsername(), b.getTokenNumber(),
                                managerPassword);
                    } catch (BadParametersException e) {
                        e.printStackTrace();
                    } catch (ExistingSubscriberException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        for (Bet b : bets) {
            if (b.isBetOnPodium()) {
                bets.remove(b);
            }
        }
    }

    public void deleteBets(Subscriber s) {
        for (Bet bet : this.bets) {
            if (bet.getSubscriber().equals(s)) {
                bets.remove(bet);
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
        return "Competition [name=" + name + ", closingDate="
                + closingDate.get(GregorianCalendar.DAY_OF_MONTH) + "-"
                + closingDate.get(GregorianCalendar.MONTH) + "-"
                + closingDate.get(GregorianCalendar.YEAR) + ", settled="
                + settled + ", competitors=" + competitors + "]";
    }
}
