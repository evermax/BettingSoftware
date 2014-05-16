package fr.uv1.bettingServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.utils.MyCalendar;

public class Competition {

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

	public Competition(String name, Calendar closingDate,
			Collection<Competitor> competitors) throws BadParametersException,
			CompetitionException {
		if (name == null || closingDate == null || competitors == null) {
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
			if (c instanceof CompetitorTeam) {
				for (Competitor c1 : ((CompetitorTeam) c).getMembers()) {
					if (!distinctCompetitors.contains(c1)) {
						distinctCompetitors.add(c1);
					} else {
						throw new CompetitionException();
					}
				}
			} else {
				if (!distinctCompetitors.contains(c)) {
					distinctCompetitors.add(c);
				} else {
					throw new CompetitionException();
				}
			}
		}
		
		this.players = distinctCompetitors;

		this.name = name;
		this.closingDate = closingDate;
		this.settled = false;
		this.competitors = competitors;
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
		return closingDate.before(Calendar.getInstance());
	}

	public Collection<Competitor> getCompetitors() {
		return competitors;
	}
	
	public boolean isAParticipant(Competitor comp) {
		return players.contains(comp);
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

}
