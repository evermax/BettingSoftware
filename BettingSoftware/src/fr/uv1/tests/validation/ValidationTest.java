package fr.uv1.tests.validation;

import fr.uv1.bettingServices.Betting;

public abstract class ValidationTest {

    private Betting betting;

    public ValidationTest() {
        this.setBetting(this.plugToBetting());

    }

    protected Betting getBetting() {
        return betting;
    }

    protected void setBetting(Betting betting) throws NullPointerException {
        if (betting == null)
            throw new NullPointerException("The bettingSoft cannot be null");
        this.betting = betting;
    }

    /*
     * Return a Betting object
     */
    public abstract Betting plugToBetting();

    /*
     * Return the manager's password
     */
    public abstract String getManagerPassword();
}