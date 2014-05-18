package fr.uv1.bettingServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Set;

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

	// Liste des compétiteurs (personnes) inscrits
	private Collection<Competitor> competitors;

	// Liste des équipes inscrites
	private Collection<Competitor> teams;

	private Collection<Bet> bets;

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
		this.subscribers = new ArrayList<Subscriber>();
		this.competitions = new ArrayList<Competition>();
		this.competitors = new ArrayList<Competitor>();
		this.teams = new ArrayList<Competitor>();
		this.bets = new ArrayList<Bet>();
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
			for (Bet b : bets) {
				if (b.getSubscriber() == s) {
					bets.remove(b);
				}
			}
			subscribers.remove(s); // remove it
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
		ArrayList<String> subsData = new ArrayList<String>();
		for (Subscriber s : subscribers) {
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
	 * Méthode permettant de chercher un compétiteur à partir de son nom, son
	 * prénom et sa date de naissance
	 * 
	 * @param lastName
	 *            nom du compétiteur à rechercher
	 * @param firstName
	 *            prénom du compétiteur à rechercher
	 * @param borndate
	 *            date de naissance du compétiteur à rechercher
	 * @return Competiteur correspondant à ces données s'il existe, null sinon
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
	 * Méthode permettant de chercher une équipe é partir de son nom
	 * 
	 * @param name
	 *            nom de l'équipe é chercher
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
	 * Méthode permettant de chercher une compétition à partir de son nom
	 * 
	 * @param name
	 *            nom de la compétition à chercher
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
	 * Méthode convertissant une chaine String au format jj/mm/aaaa en un objet
	 * Calendar
	 * 
	 * @param date
	 *            chaine contenant la date au format jj/mm/aaaa
	 * @return objet Calendar contenant la date donnée
	 * @throws BadParametersException
	 *             si la date n'a pas pu être convertie
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

		// On cherche si une compétition avec le même nom existe
		if (searchCompetitionByName(competition) != null) {
			throw new ExistingCompetitionException();
		}

		Competition c = new Competition(competition, closingDate, competitors);

		// Ajout de la compétition à la liste des compétitions
		competitions.add(c);
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
		for (Bet b : bets) {
			if (b.getCompetitionName() == competition) {
				bets.remove(b);
			}
		}
	}

	@Override
	public void addCompetitor(String competition, Competitor competitor,
			String managerPwd) throws AuthenticationException,
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException {
		authenticateMngr(managerPwd);

		// On cherche la compétition correspondant au nom donné
		Competition comp = searchCompetitionByName(competition);
		if (comp == null) {
			throw new ExistingCompetitionException();
		}
		// On vérifie si la date de cloture de la compétition est passée
		if (comp.isClosed()) {
			throw new CompetitionException();
		}
		// On vérifie que le compétiteur à ajouter ne participe pas déjà à la
		// compétition
		if (comp.getCompetitors().contains(competitor)) {
			throw new ExistingCompetitorException();
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
		 * On cherche si le compétiteur à ajouter est déjà présent ou non dans
		 * la liste. On retourne l'instance existante le cas échéant. Sinon on
		 * l'ajoute à la liste
		 */
		Competitor c = searchPlayerCompetitorByInfos(lastName, firstName, date);
		if (c != null) {
			return c;
		} else {
			CompetitorPlayer comp = new CompetitorPlayer(lastName, firstName,
					date);
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
		 * On cherche si une équipe portant le méme nom existe déjà. Si oui, on
		 * retoune l'instance existante. Sinon, on créé une nouvelle équipe et
		 * on l'ajoute à la liste.
		 */
		Competitor t = searchTeamByName(name);
		if (t != null) {
			return t;
		} else {
			CompetitorTeam team = new CompetitorTeam(name);
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

		if (comp.isClosed() || comp.getCompetitors().size() <= 2) {
			throw new CompetitionException();
		}

		if (!comp.getCompetitors().contains(competitor)) {
			throw new ExistingCompetitorException();
		}

		comp.getCompetitors().remove(competitor);
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
		// Liste des vainqueurs associés au nombre de jetons qu'ils ont pariés
		Hashtable<Subscriber, Long> tokensPerWinner = new Hashtable<Subscriber, Long>();
		// Nombre total de jetons pariés
		long totalTokens = 0;
		// Nombre total de jetons pariés sur la vainqueur
		long totalTokensOnWinner = 0;
		for (Bet b : bets) {
			if (!b.isBetOnPodium()
					&& b.getCompetitionName().equals(competition)) {
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
				creditSubscriber(w.getUsername(), redistributedToken,
						managerPassword);
			} catch (ExistingSubscriberException e) {
				e.printStackTrace();
			} catch (BadParametersException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void settlePodium(String competition, Competitor winner,
			Competitor second, Competitor third, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException,
			CompetitionException {
		authenticateMngr(managerPwd);
		// Liste des vainqueurs associés au nombre de jetons qu'ils ont pariés
		Hashtable<Subscriber, Long> tokensPerWinner = new Hashtable<Subscriber, Long>();
		// Nombre total de jetons pariés
		long totalTokens = 0;
		// Nombre total de jetons pariés sur la vainqueur
		long totalTokensOnWinner = 0;
		for (Bet b : bets) {
			if (b.isBetOnPodium() && b.getCompetitionName().equals(competition)) {
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
					creditSubscriber(w.getUsername(), redistributedToken,
							managerPassword);
				} catch (ExistingSubscriberException e) {
					e.printStackTrace();
				} catch (BadParametersException e) {
					e.printStackTrace();
				}
			}
		} else {
			for (Bet b : bets) {
				try {
					b.getSubscriber().creditTokens(b.getTokenNumber());
				} catch (BadParametersException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void betOnWinner(long numberTokens, String competition,
			Competitor winner, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException,
			ExistingCompetitionException, SubscriberException,
			BadParametersException {
		// On authentifie le joueur
		Subscriber s = searchSubscriberByUsername(username);
		if (s == null)
			throw new SubscriberException();
		s.authenticateSubscriber(pwdSubs);
		
		// On vérifie que la compétition existe et est encore ouverte
		Competition c = searchCompetitionByName(competition);
		if (c == null)
			throw new ExistingCompetitionException();
		if (c.isClosed())
			throw new CompetitionException();
		
		// On vérifie que le compétiteur sur lequel le joueur parie participe à
		// la compétition
		if (!c.getCompetitors().contains(winner))
			throw new CompetitionException();

		// On vérifie que le joueur ne participe pas à la compétition
		if (s.participates(c)) {
			throw new CompetitionException();
		}

		Bet b = s.betOnWinner(winner, numberTokens, competition);
		this.bets.add(b);
	}

	@Override
	public void betOnPodium(long numberTokens, String competition,
			Competitor winner, Competitor second, Competitor third,
			String username, String pwdSubs) throws AuthenticationException,
			CompetitionException, ExistingCompetitionException,
			SubscriberException, BadParametersException {
		// On authentifie le joueur
		Subscriber s = searchSubscriberByUsername(username);
		if (s == null)
			throw new AuthenticationException();
		s.authenticateSubscriber(pwdSubs);
		
		// On vérifie que la compétition existe et n'est pas terminée
		Competition c = searchCompetitionByName(competition);
		if (c == null)
			throw new ExistingCompetitionException();
		if (c.isClosed())
			throw new CompetitionException();
		
		// On vérifie que les compétiteurs sur lesquels le joueur parie
		// participent bien à la compétition
		Collection<Competitor> competitors = c.getCompetitors();
		if (!competitors.contains(winner) || !competitors.contains(second)
				|| !competitors.contains(third))
			throw new CompetitionException();
		
		// On vérifie que le joueur ne participe pas à la compétition
		if (s.participates(c)) {
			throw new CompetitionException();
		}
		
		// On débite le nombre de jetons pariés du compte du joueur
		Bet b = s.betOnPodium(winner, second, third, numberTokens, competition);
		this.bets.add(b);

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
		// TODO Auto-generated method stub
		ArrayList<String> infos = new ArrayList<String>();
		Subscriber s = searchSubscriberByUsername(username);
		s.authenticateSubscriber(pwdSubs);
		infos.add(s.getLastname());
		infos.add(s.getFirstname());
		SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy");
		infos.add(dateParser.format(s.getBirthdate()));
		infos.add(s.getUsername());
		infos.add(Long.toString(s.getTokens()));
		return infos;
	}

	@Override
	public void deleteBetsCompetition(String competition, String username,
			String pwdSubs) throws AuthenticationException,
			CompetitionException, ExistingCompetitionException {
		// TODO Auto-generated method stub
		Subscriber s = searchSubscriberByUsername(username);
		s.authenticateSubscriber(pwdSubs);
		for (Bet bet : this.bets) {
			if (bet.getCompetitionName().equals(competition)) {
				bets.remove(bet);
				
			}
		}
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
		ArrayList<String> bets = new ArrayList<String>();
		for (Bet bet : this.bets) {
			if (bet.getCompetitionName().equals(competition)) {
				bets.add(bet.toString());
			}
		}
		return bets;
	}

	public static void main(String[] arg) throws BadParametersException,
			AuthenticationException, ExistingCompetitionException,
			CompetitionException, SubscriberException,
			ExistingSubscriberException {
		String mgr_pwd = "Kangourou";
		BettingSoft bettingProgram = new BettingSoft(mgr_pwd);
		bettingProgram.subscribe("Bleu", "Jean", "BlueJeans", "13-03-1990",
				mgr_pwd);
		String passSub1 = bettingProgram
				.searchSubscriberByUsername("BlueJeans").getPassword();
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
		bettingProgram.addCompetition("100m Olympique", new GregorianCalendar(
				2014, 5, 6), competitors, mgr_pwd);
		bettingProgram.betOnPodium(200, "100m Olympique", c1, c2, c3,
				"BlueJeans", passSub1);
		System.out.println(bettingProgram.listCompetitions());
	}
}
