package fr.uv1;

import fr.uv1.bettingServices.*;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.tests.validation.*;
import fr.uv1.tests.validation.withoutPersistence.FirstIncrementValidationTests;

public class ValidationTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new FirstIncrementValidationTests() {

			@Override
			public Betting plugToBetting() {
				try {
					return new BettingSoft("ilesCaimans");
				} catch (BadParametersException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public String getManagerPassword() {
				return "ilesCaimans";
			}
		};

	}
}