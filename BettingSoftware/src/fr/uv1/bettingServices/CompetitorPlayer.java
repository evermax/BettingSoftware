package fr.uv1.bettingServices;

import java.util.Calendar;

import fr.uv1.bettingServices.exceptions.BadParametersException;

public class CompetitorPlayer extends ACompetitor {

    private String firstName;
    private Calendar borndate;

    private static final String REGEX_NAME = new String(
            "[a-zA-Z][a-zA-Z\\-\\ ]*");

    public CompetitorPlayer(String lastName, String firstName, Calendar borndate)
            throws BadParametersException {
        super(lastName);
        if (firstName == null || borndate == null) {
            throw new BadParametersException();
        }
        if (!name.matches(REGEX_NAME) || !firstName.matches(REGEX_NAME)) {
            throw new BadParametersException();
        }
        this.firstName = firstName;
        this.borndate = borndate;
    }

    public String getFirstName() {
        return firstName;
    }

    public Calendar getBorndate() {
        return borndate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((borndate == null) ? 0 : borndate.hashCode());
        result = prime * result
                + ((firstName == null) ? 0 : firstName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CompetitorPlayer)) {
            return false;
        }
        CompetitorPlayer other = (CompetitorPlayer) obj;
        if (borndate == null) {
            if (other.borndate != null) {
                return false;
            }
        } else if (!borndate.equals(other.borndate)) {
            return false;
        }
        if (firstName == null) {
            if (other.firstName != null) {
                return false;
            }
        } else if (!firstName.equals(other.firstName)) {
            return false;
        }
        return true;
    }
}
