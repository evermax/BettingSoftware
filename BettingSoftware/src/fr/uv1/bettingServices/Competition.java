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

	public Competition(String name, Calendar closingDate,
			Collection<Competitor> competitors) throws BadParametersException,
			CompetitionException {
		if (name == null || closingDate == null || competitors == null) {
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
