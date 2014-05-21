package fr.uv1.bettingServices;

import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;

public abstract class ACompetitor implements Competitor {
	
	protected String name;
	protected int id;

	public ACompetitor(String name) throws BadParametersException {
		if (name == null) {
			throw new BadParametersException();
		}
		this.name = name;
	}
	
	public ACompetitor(int id, String name) throws BadParametersException {
        if (name == null) {
            throw new BadParametersException();
        }
        this.name = name;
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
	public boolean hasValidName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addMember(Competitor member)
			throws ExistingCompetitorException, BadParametersException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMember(Competitor member) throws BadParametersException,
			ExistingCompetitorException {
		// TODO Auto-generated method stub

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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ACompetitor)) {
            return false;
        }
        ACompetitor other = (ACompetitor) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
