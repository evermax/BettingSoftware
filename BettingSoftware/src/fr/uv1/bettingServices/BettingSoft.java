package fr.uv1.bettingServices;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import fr.uv1.bettingServices.bd.BetDAO;
import fr.uv1.bettingServices.bd.CompetitionDAO;
import fr.uv1.bettingServices.bd.CompetitorPlayerDAO;
import fr.uv1.bettingServices.bd.CompetitorTeamDAO;
import fr.uv1.bettingServices.bd.SubscriberDAO;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.BettingPasswordsVerifier;

/**
 * 
 * @author prou + mallet <br>
 * <br>
 *         This class implements methods of the interface Betting. <br>
 * <br>
 *         <ul>
 *         <li>manager password validity:
 *         <ul>
 *         <li>only letters and digits are allowed</li>
 *         <li>password size should be at least 8 characters</li>
 *         </ul>
 *         </li>
 *         </ul>
 */

public class BettingSoft implements Betting {

    /*
     * Manager password
     */
    private String managerPassword;

    /*
     * Subscribers of the betting software
     */
    /**
     * @uml.property name="subscribers"
     * @uml.associationEnd multiplicity="(0 -1)"
     *                     inverse="bettingSoft:fr.uv1.bettingServices.Subscriber"
     */
    private Collection<Subscriber> subscribers;

    private Collection<Competition> competitions;

    // Liste des comp�titeurs (personnes) inscrits
    private Collection<Competitor> competitors;

    // Liste des �quipes inscrites
    private Collection<Competitor> teams;

    /**
     * constructor of BettingSoft
     * 
     * @param a_managerPwd
     *            manager password.
     * 
     * @throws BadParametersException
     *             raised if a_managerPwd is incorrect.
     */
    public BettingSoft(String a_managerPwd) throws BadParametersException {
        // The password should be valid
        setManagerPassword(a_managerPwd);
        try {
            this.subscribers = SubscriberDAO.findAll();
            this.competitions = CompetitionDAO.findAll();
            this.competitors = CompetitorPlayerDAO.findAll();
            this.teams = CompetitorTeamDAO.findAll();
        } catch (SQLException | CompetitionException e) {
            e.printStackTrace();
        }
        this.competitors = new ArrayList<Competitor>();
        this.teams = new ArrayList<Competitor>();
    }

    private void setManagerPassword(String managerPassword)
            throws BadParametersException {
        if (managerPassword == null)
            throw new BadParametersException("manager's password not valid");
        // The password should be valid
        if (!BettingPasswordsVerifier.verify(managerPassword))
            throw new BadParametersException("manager's password not valid");
        this.managerPassword = managerPassword;
    }

    /**
     * From Betting interface
     * 
     * @return
     */
    @Override
    public long unsubscribe(String a_username, String a_managerPwd)
            throws AuthenticationException, ExistingSubscriberException {
        // Authenticate manager
        authenticateMngr(a_managerPwd);
        // Look if a subscriber with the same username already exists
        Subscriber s = searchSubscriberByUsername(a_username);
        if (s != null) {
            // On supprime tous les paris de ce joueur
            for (Competition c : competitions) {
                c.deleteBets(s);
            }
            subscribers.remove(s); // remove it
            try {
                SubscriberDAO.delete(s);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return s.getTokens();
        } else
            throw new ExistingSubscriberException("Subscriber does not exist");
    }

    /**
     * From Betting interface
     */
    @Override
    public ArrayList<ArrayList<String>> listSubscribers(String a_managerPwd)
            throws AuthenticationException {
        // Authenticate manager
        authenticateMngr(a_managerPwd);
        // Calculate the list of subscribers
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

        for (Subscriber s : subscribers) {
            ArrayList<String> subsData = new ArrayList<String>();

            subsData.add(s.getLastname());
            subsData.add(s.getFirstname());
            subsData.add(s.getUsername());

            result.add(subsData);
        }
        return result;
    }

    public void authenticateMngr(String a_managerPwd)
            throws AuthenticationException {
        if (a_managerPwd == null)
            throw new AuthenticationException("invalid manager's password");

        if (!this.managerPassword.equals(a_managerPwd))
            throw new AuthenticationException("incorrect manager's password");
    }

    /**
     * From Betting interface
     */
    @Override
    public void changeMngrPwd(String newPwd, String currentPwd)
            throws AuthenticationException, BadParametersException {
        // Authenticate manager
        authenticateMngr(currentPwd);
        // Change password if valid
        setManagerPassword(newPwd);
    }

    /**
     * search a subscriber by username
     * 
     * @param a_username
     *            the username of the subscriber.
     * 
     * @return the found subscriber or null
     */
    private Subscriber searchSubscriberByUsername(String a_username) {
        if (a_username == null)
            return null;
        for (Subscriber s : subscribers) {
            if (s.hasUsername(a_username))
                return s;
        }
        return null;
    }

    /**
     * M�thode permettant de chercher un comp�titeur � partir de son nom, son
     * pr�nom et sa date de naissance
     * 
     * @param lastName
     *            nom du comp�titeur � rechercher
     * @param firstName
     *            pr�nom du comp�titeur � rechercher
     * @param borndate
     *            date de naissance du comp�titeur � rechercher
     * @return Competiteur correspondant � ces donn�es s'il existe, null sinon
     * @throws BadParametersException
     */
    private Competitor searchPlayerCompetitorByInfos(String lastName,
            String firstName, Calendar borndate) throws BadParametersException {
        if (lastName == null || firstName == null || borndate == null) {
            return null;
        }
        CompetitorPlayer comp = new CompetitorPlayer(lastName, firstName,
                borndate);
        for (Competitor c : competitors) {
            if (c.equals(comp)) {
                return c;
            }
        }
        return null;
    }

    /**
     * M�thode permettant de chercher une �quipe � partir de son nom
     * 
     * @param name
     *            nom de l'�quipe � chercher
     * @return Equipe portant ce nom si elle existe, null sinon
     * @throws BadParametersException
     */
    private Competitor searchTeamByName(String name)
            throws BadParametersException {
        if (name == null) {
            return null;
        }
        CompetitorTeam team = new CompetitorTeam(name);
        for (Competitor t : competitors) {
            if (t.equals(team)) {
                return t;
            }
        }
        return null;
    }

    /**
     * M�thode permettant de chercher une comp�tition � partir de son nom
     * 
     * @param name
     *            nom de la comp�tition � chercher
     * @return Competition portant ce nom si elle existe, null sinon
     */
    private Competition searchCompetitionByName(String name) {
        if (name == null) {
            return null;
        }

        for (Competition c : competitions) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * M�thode convertissant une chaine String au format jj/mm/aaaa en un objet
     * Calendar
     * 
     * @param date
     *            chaine contenant la date au format jj/mm/aaaa
     * @return objet Calendar contenant la date donn�e
     * @throws BadParametersException
     *             si la date n'a pas pu �tre convertie
     */
    private Calendar convertStringToDate(String date)
            throws BadParametersException {
        SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy");
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(dateParser.parse(date));
        } catch (ParseException e) {
            throw new BadParametersException("Invalid birthdate");
        }
        return calendar;
    }

    @Override
    public String subscribe(String lastName, String firstName, String username,
            String borndate, String managerPwd) throws AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            BadParametersException {
        // Authenticate manager
        authenticateMngr(managerPwd);
        // Look if a subscriber with the same username already exists
        Subscriber s = searchSubscriberByUsername(username);
        if (s != null)
            throw new ExistingSubscriberException(
                    "A subscriber with the same username already exists");
        Calendar birthdate = convertStringToDate(borndate);
        // Creates the new subscriber
        s = new Subscriber(lastName, firstName, username, birthdate);
        try {
            SubscriberDAO.persist(s);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // Add it to the collection of subscribers
        subscribers.add(s);
        return s.getPassword();
    }

    @Override
    public void addCompetition(String competition, Calendar closingDate,
            Collection<Competitor> competitors, String managerPwd)
            throws AuthenticationException, ExistingCompetitionException,
            CompetitionException, BadParametersException {
        // Authentification du manager
        authenticateMngr(managerPwd);

        // On cherche si une comp�tition avec le m�me nom existe
        if (searchCompetitionByName(competition) != null) {
            throw new ExistingCompetitionException();
        }

        Competition c = new Competition(competition, closingDate, competitors);

        // Ajout de la comp�tition � la liste des comp�titions
        competitions.add(c);
        try {
            CompetitionDAO.persist(c);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void cancelCompetition(String competition, String managerPwd)
            throws AuthenticationException, ExistingCompetitionException,
            CompetitionException {
        authenticateMngr(managerPwd);

        if (competition == null) {
            throw new ExistingCompetitionException();
        }

        Competition comp = searchCompetitionByName(competition);
        if (comp == null) {
            throw new ExistingCompetitionException();
        }

        if (comp.isClosed()) {
            throw new CompetitionException();
        }

        competitions.remove(comp);
    }

    @Override
    public void addCompetitor(String competition, Competitor competitor,
            String managerPwd) throws AuthenticationException,
            ExistingCompetitionException, CompetitionException,
            ExistingCompetitorException, BadParametersException {
        authenticateMngr(managerPwd);

        // On cherche la comp�tition correspondant au nom donn�
        Competition comp = searchCompetitionByName(competition);
        if (comp == null) {
            throw new ExistingCompetitionException();
        }
        // On v�rifie si la date de cloture de la comp�tition est pass�e
        if (comp.isClosed()) {
            throw new CompetitionException();
        }
        // On v�rifie que le comp�titeur � ajouter ne participe pas d�j� � la
        // comp�tition
        if (comp.getCompetitors().contains(competitor)) {
            throw new ExistingCompetitorException();
        }

        try {
            CompetitionDAO.addCompetitorInCompetition((ACompetitor) competitor,
                    comp);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        comp.getCompetitors().add(competitor);

    }

    @Override
    public Competitor createCompetitor(String lastName, String firstName,
            String borndate, String managerPwd) throws AuthenticationException,
            BadParametersException {

        // Authentification du manager
        authenticateMngr(managerPwd);

        if (firstName == null || lastName == null || borndate == null) {
            throw new BadParametersException();
        }

        /*
         * Conversion de la date de naissance dans un objet Calendar. On suppose
         * que la date est au format jj/mm/aaaa
         */
        Calendar date = convertStringToDate(borndate);

        /*
         * On cherche si le comp�titeur � ajouter est d�j� pr�sent ou non dans
         * la liste. On retourne l'instance existante le cas �ch�ant. Sinon on
         * l'ajoute � la liste
         */
        Competitor c = searchPlayerCompetitorByInfos(lastName, firstName, date);
        if (c != null) {
            return c;
        } else {
            CompetitorPlayer comp = new CompetitorPlayer(lastName, firstName,
                    date);
            try {
                comp = CompetitorPlayerDAO.persist(comp);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            competitors.add(comp);
            return comp;
        }
    }

    @Override
    public Competitor createCompetitor(String name, String managerPwd)
            throws AuthenticationException, BadParametersException {

        // Authentification du manager
        authenticateMngr(managerPwd);

        if (name == null) {
            throw new BadParametersException();
        }

        /*
         * On cherche si une �quipe portant le m�me nom existe d�j�. Si oui, on
         * retoune l'instance existante. Sinon, on cr�� une nouvelle �quipe et
         * on l'ajoute � la liste.
         */
        Competitor t = searchTeamByName(name);
        if (t != null) {
            return t;
        } else {
            CompetitorTeam team = new CompetitorTeam(name);

            try {
                team = CompetitorTeamDAO.persist(team);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            teams.add(team);
            return team;
        }
    }

    @Override
    public void deleteCompetitor(String competition, Competitor competitor,
            String managerPwd) throws AuthenticationException,
            ExistingCompetitionException, CompetitionException,
            ExistingCompetitorException {
        authenticateMngr(managerPwd);

        Competition comp = searchCompetitionByName(competition);
        if (comp == null) {
            throw new ExistingCompetitionException();
        }
        comp.deleteCompetitor(competitor);
        try {
            CompetitionDAO.removeCompetitorFromCompetition(
                    (ACompetitor) competitor, comp);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void creditSubscriber(String username, long numberTokens,
            String managerPwd) throws AuthenticationException,
            ExistingSubscriberException, BadParametersException {

        authenticateMngr(managerPwd);
        Subscriber subscriber = searchSubscriberByUsername(username);
        if (subscriber == null) {
            throw new ExistingSubscriberException();
        }

        subscriber.creditTokens(numberTokens);
    }

    @Override
    public void debitSubscriber(String username, long numberTokens,
            String managerPwd) throws AuthenticationException,
            ExistingSubscriberException, SubscriberException,
            BadParametersException {

        authenticateMngr(managerPwd);
        Subscriber subscriber = searchSubscriberByUsername(username);
        if (subscriber == null) {
            throw new ExistingSubscriberException();
        }

        subscriber.debitTokens(numberTokens);

    }

    @Override
    public void settleWinner(String competition, Competitor winner,
            String managerPwd) throws AuthenticationException,
            ExistingCompetitionException, CompetitionException {
        authenticateMngr(managerPwd);
        Competition c = searchCompetitionByName(competition);
        if (c == null)
            throw new ExistingCompetitionException();
        c.settleWinner(winner);
    }

    @Override
    public void settlePodium(String competition, Competitor winner,
            Competitor second, Competitor third, String managerPwd)
            throws AuthenticationException, ExistingCompetitionException,
            CompetitionException {
        authenticateMngr(managerPwd);
        Competition c = searchCompetitionByName(competition);
        if (c == null)
            throw new ExistingCompetitionException();
        c.settlePodium(winner, second, third);
    }

    @Override
    public void betOnWinner(long numberTokens, String competition,
            Competitor winner, String username, String pwdSubs)
            throws AuthenticationException, CompetitionException,
            ExistingCompetitionException, SubscriberException,
            BadParametersException {
        // On identifie le joueur
        Subscriber s = searchSubscriberByUsername(username);
        if (s == null)
            throw new SubscriberException();

        // On authentifie le joueur
        s.authenticateSubscriber(pwdSubs);

        // On v�rifie que la comp�tition existe
        Competition c = searchCompetitionByName(competition);
        if (c == null)
            throw new ExistingCompetitionException();

        Bet bet = c.betOnWinner(s, pwdSubs, winner, numberTokens);
        try {
            BetDAO.persist(bet);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void betOnPodium(long numberTokens, String competition,
            Competitor winner, Competitor second, Competitor third,
            String username, String pwdSubs) throws AuthenticationException,
            CompetitionException, ExistingCompetitionException,
            SubscriberException, BadParametersException {
        // On identifie le joueur
        Subscriber s = searchSubscriberByUsername(username);
        if (s == null)
            throw new AuthenticationException();

        // On authentifie le joueur
        s.authenticateSubscriber(pwdSubs);

        // On v�rifie que la comp�tition existe
        Competition c = searchCompetitionByName(competition);
        if (c == null)
            throw new ExistingCompetitionException();
        Bet bet = c
                .betOnPodium(s, pwdSubs, winner, second, third, numberTokens);
        try {
            BetDAO.persist(bet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void changeSubsPwd(String username, String newPwd, String currentPwd)
            throws AuthenticationException, BadParametersException {
        Subscriber s = searchSubscriberByUsername(username);
        s.authenticateSubscriber(currentPwd);
        s.changePassword(currentPwd, newPwd);
    }

    @Override
    public ArrayList<String> infosSubscriber(String username, String pwdSubs)
            throws AuthenticationException {
        Subscriber s = searchSubscriberByUsername(username);
        return s.getInfos(pwdSubs);
    }

    @Override
    public void deleteBetsCompetition(String competition, String username,
            String pwdSubs) throws AuthenticationException,
            CompetitionException, ExistingCompetitionException {
        Subscriber s = searchSubscriberByUsername(username);
        s.authenticateSubscriber(pwdSubs);
        Competition comp = searchCompetitionByName(competition);
        if (comp == null) {
            throw new ExistingCompetitionException();
        }
        comp.deleteBets(s);
    }

    @Override
    public Collection<Competition> listCompetitions() {
        return competitions;
    }

    @Override
    public Collection<Competitor> listCompetitors(String competition)
            throws ExistingCompetitionException, CompetitionException {
        Competition comp = searchCompetitionByName(competition);
        if (comp == null) {
            throw new ExistingCompetitionException();
        }
        if (comp.isClosed()) {
            throw new CompetitionException();
        }
        return comp.getCompetitors();
    }

    @Override
    public ArrayList<String> consultBetsCompetition(String competition)
            throws ExistingCompetitionException {
        Competition comp = searchCompetitionByName(competition);
        if (comp == null) {
            throw new ExistingCompetitionException();
        }
        return comp.consultBets();
    }

    public static void main(String[] arg) throws BadParametersException,
            AuthenticationException, ExistingCompetitionException,
            CompetitionException, SubscriberException,
            ExistingSubscriberException {
        String mgr_pwd = "Kangourou";
        BettingSoft bettingProgram = new BettingSoft(mgr_pwd);
        // String passSub1 = bettingProgram.subscribe("Bleu", "Jean",
        // "BlueJeans", "13-03-1990",
        // mgr_pwd);
        // Subscriber s =
        // bettingProgram.searchSubscriberByUsername("BlueJeans");
        // bettingProgram.creditSubscriber("BlueJeans", 1000, mgr_pwd);
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
        bettingProgram.addCompetition("200mOlympique", new GregorianCalendar(
                2014, 5, 6), competitors, mgr_pwd);
        // bettingProgram.betOnPodium(200, "100mOlympique", c1, c2, c3,
        // "BlueJeans", passSub1);
        System.out.println(bettingProgram.listCompetitions());
        // bettingProgram.unsubscribe("BlueJeans", mgr_pwd);
    }
}
