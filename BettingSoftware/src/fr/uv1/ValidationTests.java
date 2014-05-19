package fr.uv1;

import fr.uv1.bettingServices.*;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.tests.validation.withoutPersistence.FirstIncrementValidationTests;
import fr.uv1.tests.validation.withoutPersistence.SecondIncrementValidationTests;

@SuppressWarnings("unused")
public class ValidationTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new SecondIncrementValidationTests() {

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