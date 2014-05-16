package fr.uv1.bettingServices;

import java.util.ArrayList;

import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;

public class CompetitorTeam extends ACompetitor {

    private static final String REGEX_NAME = new String("[a-zA-Z][a-zA-Z\\-]*");

    private ArrayList<Competitor> members;

    public CompetitorTeam(String name) throws BadParametersException {
        super(name);
        if (!name.matches(REGEX_NAME)) {
            throw new BadParametersException();
        }
        members = new ArrayList<Competitor>();
    }

    @Override
    public boolean hasValidName() {
        if (name == null)
            return false;
        if (!name.matches(REGEX_NAME)) {
            return false;
        }
        return true;
    }

    public ArrayList<Competitor> getMembers() {
        return members;
    }
    
    @Override
    public void addMember(Competitor member)
            throws ExistingCompetitorException, BadParametersException {
        if (member == null || member == this) {
            throw new BadParametersException();
        }
        
        if (members.contains(member)) {
            throw new ExistingCompetitorException();
        }
        members.add(member);
    }
    
    @Override
    public void deleteMember(Competitor member) throws BadParametersException,
            ExistingCompetitorException {
        if (member == null) {
            throw new BadParametersException();
        }

        if (!members.remove(member)) {
            throw new ExistingCompetitorException();
        }
    }

    @Override
    public String toString() {
        return "CompetitorTeam [members=" + members + ", name=" + name + "]";
    }
}
