package fr.uv1.bettingServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.utils.*;

/**
 * 
 * @author prou, segarra<br>
 * <br>
 *         This class represents a subscriber for a betting application. <br>
 * <br>
 *         The constructor of the class creates a password for the subscriber. <br>
 *         <ul>
 *         <li>subscriber's password validity:
 *         <ul>
 *         <li>only letters and digits are allowed</li>
 *         <li>password size should be at least 8 characters</li>
 *         </ul>
 *         </li>
 *         <li>for the username validity:
 *         <ul>
 *         <li>only letters and digits are allowed</li>
 *         <li>size should be at least 4 characters</li>
 *         </ul>
 *         </li>
 *         </ul>
 * 
 */
public class Subscriber implements Serializable {
	private static final long serialVersionUID = 6050931528781005411L;
	/*
	 * Minimal size for a subscriber's username
	 */
	private static final int LONG_USERNAME = 4;
	/*
	 * Constraints for last and firstname and username
	 */
	private static final String REGEX_NAME = new String(
			"[a-zA-Z][a-zA-Z\\-\\ ]*");
	private static final String REGEX_USERNAME = new String("[a-zA-Z0-9]*");

	private String firstname;
	private String lastname;
	/**
	 * @uml.property name="username"
	 */
	private String username;
	private String password;

	private Collection<Bet> bets;

	private long tokens = 0;
	private Calendar birthdate;

	/*
	 * the constructor calculates a password for the subscriber. No test on the
	 * validity of names
	 */
	public Subscriber(String a_name, String a_firstName, String a_username,
			Calendar birthdate) throws BadParametersException, SubscriberException {
		this.setLastname(a_name);
		this.setFirstname(a_firstName);
		this.setUsername(a_username);
		// Generate password
		password = RandPass.getPass(Constraints.LONG_PWD);
		this.setPassword(password);
		this.setBirthdate(birthdate);
		this.bets = new ArrayList<Bet>(); 
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

    public Calendar getBirthdate() {
        return birthdate;
    }

    /**
     * Setter du nom de famille
     * @param lastname
     * @throws BadParametersException si le nom est vide ou si il ne correspond pas aux contraintes.
     */
	public void setLastname(String lastname) throws BadParametersException {
		if (lastname == null)
			throw new BadParametersException("lastname is not valid");
		checkStringLastName(lastname);
		this.lastname = lastname;
	}

	/**
	 * Setter du prénom
	 * @param firstname
	 * @throws BadParametersException si le nom est vide ou si il ne correspond pas aux contraintes.
	 */
	public void setFirstname(String firstname) throws BadParametersException {
		if (firstname == null)
			throw new BadParametersException("firstname is not valid");
		checkStringFirstName(firstname);
		this.firstname = firstname;
	}

	/**
	 * Setter du pseudo
	 * @param username
	 * @throws BadParametersException si le nom est vide ou si il ne correspond pas aux contraintes.
	 */
	public void setUsername(String username) throws BadParametersException {
		if (username == null)
			throw new BadParametersException("username is not valid");
		checkStringUsername(username);
		this.username = username;
	}

	/**
	 * Setter du password
	 * @param password
	 * @throws BadParametersException si le mot de passe est vide ou si il ne correspond pas aux contraintes.
	 */
	private void setPassword(String password) throws BadParametersException {
		if (password == null)
			throw new BadParametersException("password is not valid");
		if (!BettingPasswordsVerifier.verify(password))
			throw new BadParametersException("password is not valid");
		this.password = password;
	}

	/**
	 * Setter de la date de naissance
	 * @param birthdate
	 * @throws BadParametersException si la date n'a pas un format valide
	 * @throws SubscriberException si la personne n'a pas 18 ans (au mois près)
	 */
    public void setBirthdate(Calendar birthdate) throws BadParametersException, SubscriberException {
        if (birthdate == null)
            throw new BadParametersException("date is not valid");
        MyCalendar now = MyCalendar.getDate();
        int diffMonth = (now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR))*12 + now.get(Calendar.MONTH) - birthdate.get(Calendar.MONTH);
        if (diffMonth < 18*12)
            throw new SubscriberException();
            
        this.birthdate = birthdate;
    }

    /*
	 * check if this subscriber has the username of the parameter
	 * 
	 * @param username the username to check
	 * 
	 * @return true if this username is the same as the parameter false
	 * otherwise
	 */
	public boolean hasUsername(String username) {
		if (username == null)
			return false;
		return this.username.equals(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subscriber other = (Subscriber) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return " " + firstname + " " + lastname + " " + username;
	}

	/**
	 * check the validity of a string for a subscriber lastname, letters, dashes
	 * and spaces are allowed. First character should be a letter. lastname
	 * length should at least be 1 character
	 * 
	 * @param a_lastname
	 *            string to check.
	 * 
	 * @throws BadParametersException
	 *             raised if invalid.
	 */
	private static void checkStringLastName(String a_lastname)
			throws BadParametersException {

		if (a_lastname == null)
			throw new BadParametersException("name not instantiated");
		if (a_lastname.length() < 1)
			throw new BadParametersException(
					"name length less than 1 character");
		// First character should be a letter ; then just letters, dashes or
		// spaces
		if (!a_lastname.matches(REGEX_NAME))
			throw new BadParametersException("the name " + a_lastname
					+ " does not verify constraints ");
	}

	/**
	 * check the validity of a string for a subscriber firstname, letters,
	 * dashes and spaces are allowed. First character should be a letter.
	 * firstname length should at least be 1 character
	 * 
	 * @param a_firstname
	 *            string to check.
	 * 
	 * @throws BadParametersException
	 *             raised if invalid.
	 */
	private static void checkStringFirstName(String a_firstname)
			throws BadParametersException {
		// Same rules as for the last name
		checkStringLastName(a_firstname);

	}

	/**
	 * check the validity of a string for a subscriber username, letters and
	 * digits are allowed. username length should at least be LONG_USERNAME
	 * characters
	 * 
	 * @param a_username
	 *            string to check.
	 * 
	 * @throws BadParametersException
	 *             raised if invalid.
	 */
	private static void checkStringUsername(String a_username)
			throws BadParametersException {
		if (a_username == null)
			throw new BadParametersException("username not instantiated");

		if (a_username.length() < LONG_USERNAME)
			throw new BadParametersException("username length less than "
					+ LONG_USERNAME + "characters");
		// Just letters and digits are allowed
		if (!a_username.matches(REGEX_USERNAME))
			throw new BadParametersException("the username " + a_username
					+ " does not verify constraints ");
	}

	/*
	 * Crédite ou débite le nombre de jetons donné par nbJetons
	 */
	public void creditTokens(long numberTokens) throws BadParametersException {
		if (numberTokens <= 0) {
			throw new BadParametersException();
		}
		tokens += numberTokens;
	}

	public void debitTokens(long numberTokens) throws BadParametersException,
			SubscriberException {
		if (numberTokens <= 0) {
			throw new BadParametersException();
		}
		if (tokens - numberTokens < 0) {
			throw new SubscriberException();
		}

		tokens -= numberTokens;
	}

	public long getTokens() {
		return tokens;
	}

	public void authenticateSubscriber(String subscriberPassword)
			throws AuthenticationException {
		if (subscriberPassword == null)
			throw new AuthenticationException("invalid subscriber's password");

		if (!this.password.equals(subscriberPassword))
			throw new AuthenticationException("incorrect subscriber's password");
	}

	public void deleteBetsCompetition(String competitionName) {
		for (Bet bet : this.bets) {
			if (bet.getCompetitionName().equals(competitionName)) {
				bets.remove(bet);
			}
		}
	}

	public void changePassword(String currentPassword, String newPassword)
			throws AuthenticationException, BadParametersException {
		this.authenticateSubscriber(currentPassword);
		this.setPassword(newPassword);
	}

	public void addBet(Bet bet) {
		this.bets.add(bet);
	}
}
