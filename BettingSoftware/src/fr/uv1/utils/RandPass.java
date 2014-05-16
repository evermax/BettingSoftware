/*
 * Generate random passwords.
 * 
 */

package fr.uv1.utils;

import java.security.SecureRandom;

/**
 * Generates a random String using a cryptographically secure random number
 * generator.
 * 
 * @author Stephen Ostermiller
 *         http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.00.00
 */
public class RandPass {

	/**
	 * Alphabet consisting of upper and lower case letters A-Z and the digits
	 * 0-9.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static final char[] NUMBERS_AND_LETTERS_ALPHABET = { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', };

	/**
	 * Fill the given buffer with random characters.
	 * @param pass
	 *            buffer that will hold the password.
	 * @return the buffer, filled with random characters.
	 * 
	 */
	public static char[] getPassChars(char[] pass) {
		SecureRandom rand = new SecureRandom();
		int length = pass.length;
		char[] useAlph = NUMBERS_AND_LETTERS_ALPHABET;
		for (int i = 0; i < length; i++) {
			pass[i] = useAlph[rand.nextInt(NUMBERS_AND_LETTERS_ALPHABET.length)];
		}
		return pass;
	}

	/**
	 * Generate a random password of the given length.
	 * <p>
	 * NOTE: Strings can not be modified. If it is possible for a hacker to
	 * examine memory to find passwords, getPassChars() should be used so that
	 * the password can be zeroed out of memory when no longer in use.
	 * 
	 * @param length
	 *            The desired length of the generated password.
	 * @return a random password
	 * 
	 * @see #getPassChars(int)
	 * @since ostermillerutils 1.00.00
	 */
	public static String getPass(int length) {
		return (new String(getPassChars(new char[length])));
	}
}