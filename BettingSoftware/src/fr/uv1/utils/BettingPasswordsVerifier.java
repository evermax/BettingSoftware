package fr.uv1.utils;

/*
 * check the validity of a password
 * letters and digits are allowed. Password length should at least be LONG_PWD characters
 */
public class BettingPasswordsVerifier {

	public static boolean verify(String password) {
		if (password == null)
			return false;
		if (password.toCharArray().length < Constraints.LONG_PWD)
			return false;

		return password.matches("[[0-9]*|[a-zA-Z]*]*");
	}
}
