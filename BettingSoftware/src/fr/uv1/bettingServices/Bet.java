package fr.uv1.bettingServices;

public class Bet {
	private long tokenNumber;
	private Subscriber subscriber;
	private Competition competition;
	private Competitor winner; // Should contain the choices of the the
								// Subscriber
	private Competitor second;
	private Competitor third;
	private boolean isBetOnPodium;

	/**
	 * Constructeur du pari sur un gagnant
	 * 
	 * @param tokenNumber
	 *            Nombre de jetons pari�s sur le gagnant
	 * @param subscriber
	 *            Nom du joueur qui a effectu� ce pari
	 * @param competition
	 *            Nom de la comp�tition sur laquelle a �t� effectu� le pari
	 * @param winner
	 *            Nom du comp�titeur sur lequel le joueur a pari�
	 */
	public Bet(long tokenNumber, Subscriber subscriber, Competition competition,
			Competitor winner) {
		this.tokenNumber = tokenNumber;
		this.subscriber = subscriber;
		this.competition = competition;
		this.winner = winner;
		this.second = null;
		this.third = null;
		this.isBetOnPodium = false;
	}

	/**
	 * Constructeur du pari sur un podium
	 * 
	 * @param tokenNumber
	 *            Nombre de jetons pari�s sur le gagnant
	 * @param subscriber
	 *            Nom du joueur qui a effectu� ce pari
	 * @param competition
	 *            Nom de la comp�tition sur laquelle a �t� effectu� le pari
	 * @param winner
	 *            Nom du premier comp�titeur du podium
	 * @param second
	 *            Nom du second comp�titeur du podium
	 * @param third
	 *            Nom du troisi�me comp�titeur du podium
	 */
	public Bet(long tokenNumber, Subscriber subscriber, Competition competition,
			Competitor winner, Competitor second, Competitor third) {
		this.tokenNumber = tokenNumber;
		this.subscriber = subscriber;
		this.competition = competition;
		this.winner = winner;
		this.second = second;
		this.third = third;
		this.isBetOnPodium = true;

	}

	/**
	 * 
	 * @return true si le pari est sur un podium, false si le pari est sur un
	 *         vainqueur
	 */
	public boolean isBetOnPodium() {
		return isBetOnPodium;
	}

	/**
	 * Getter pour le nombre de jetons
	 * 
	 * @return le nombre de jetons du pari
	 */
	public long getTokenNumber() {
		return tokenNumber;
	}

	/**
	 * Getter pour le joueur
	 * 
	 * @return l'instance du joueur qui a effectu� le pari
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * Getter du nom de la comp�tition
	 * 
	 * @return le nom de la comp�tition
	 */
	public Competition getCompetition() {
		return competition;
	}

	public Competitor getWinner() {
		return winner;
	}

	public void setWinner(Competitor winner) {
		this.winner = winner;
	}

	public Competitor getSecond() {
		return second;
	}

	public void setSecond(Competitor second) {
		this.second = second;
	}

	public Competitor getThird() {
		return third;
	}

	public void setThird(Competitor third) {
		this.third = third;
	}

	public void cancelBet() {
		// We have to remove it from the list, which in fact is not easy to do
		// from here, as a Bet method.
	}

	@Override
	public String toString() {
		return "Bet [tokenNumber=" + tokenNumber + ", subscriber=" + subscriber
				+ ", competitionName=" + competition + ", winner=" + winner
				+ ", second=" + second + ", third=" + third + "]";
	}
}
