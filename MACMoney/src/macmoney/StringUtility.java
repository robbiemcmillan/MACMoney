
package macmoney;
import java.security.*;

/**
 * 
 * StringUtility.java 
 * Purpose: Utility class used to provide SHA256 hashing
 * functionality. Based on baeldung SHA256 hashing guide.
 * 
 * Source: http://www.baeldung.com/sha-256-hashing-java
 * 
 * @author William Robbie McMillan - 2035341
 * @version 1.0
 */

public class StringUtility {

	/**
	 * 
	 * Hashes the string input with SHA256 hash function
	 * Hash is converted into a readable hexadecimal string.
	 * 
	 * @param input
	 * @return readable hexadecimal hash
	 * @throws NoSuchAlgorithmException
	 */
	public synchronized static String applySHA256(String input) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(input.getBytes());
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}

		return hexString.toString();

	}

	
}
