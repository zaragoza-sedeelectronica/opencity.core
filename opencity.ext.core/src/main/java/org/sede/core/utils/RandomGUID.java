package org.sede.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In the multitude of java GUID generators, I found none that guaranteed randomness. GUIDs are guaranteed to be
 * globally unique by using ethernet MACs, IP addresses, time elements, and sequential numbers. GUIDs are not expected
 * to be random and most often are easy/possible to guess given a sample from a given generator. SQL Server, for example
 * generates GUID that are unique but sequencial within a given instance.
 * 
 * GUIDs can be used as security devices to hide things such as files within a filesystem where listings are unavailable
 * (e.g. files that are served up from a Web server with indexing turned off). This may be desireable in cases where
 * standard authentication is not appropriate. In this scenario, the RandomGUIDs are used as directories. Another
 * example is the use of GUIDs for primary keys in a database where you want to ensure that the keys are secret. Random
 * GUIDs can then be used in a URL to prevent hackers (or users) from accessing records by guessing or simply by
 * incrementing sequential numbers.
 * 
 * There are many other possiblities of using GUIDs in the realm of security and encryption where the element of
 * randomness is important. This class was written for these purposes but can also be used as a general purpose GUID
 * generator as well.
 * 
 * RandomGUID generates truly random GUIDs by using the system's IP address (name/IP), system time in milliseconds (as
 * an integer), and a very large random number joined together in a single String that is passed through an MD5 hash.
 * The IP address and system time make the MD5 seed globally unique and the random number guarantees that the generated
 * GUIDs will have no discernable pattern and cannot be guessed given any number of previously generated GUIDs. It is
 * generally not possible to access the seed information (IP, time, random number) from the resulting GUIDs as the MD5
 * hash algorithm provides one way encryption.
 * 
 * ----> Security of RandomGUID: <----- RandomGUID can be called one of two ways -- with the basic java Random number
 * generator or a cryptographically strong random generator (SecureRandom). The choice is offered because the secure
 * random generator takes about 3.5 times longer to generate its random numbers and this performance hit may not be
 * worth the added security especially considering the basic generator is seeded with a cryptographically strong random
 * seed.
 * 
 * Seeding the basic generator in this way effectively decouples the random numbers from the time component making it
 * virtually impossible to predict the random number component even if one had absolute knowledge of the System time.
 * Thanks to Ashutosh Narhari for the suggestion of using the static method to prime the basic random generator.
 * 
 * Using the secure random option, this class compies with the statistical random number generator tests specified in
 * FIPS 140-2, Security Requirements for Cryptographic Modules, secition 4.9.1.
 * 
 * I converted all the pieces of the seed to a String before handing it over to the MD5 hash so that you could print it
 * out to make sure it contains the data you expect to see and to give a nice warm fuzzy. If you need better
 * performance, you may want to stick to byte[] arrays.
 * 
 * I believe that it is important that the algorithm for generating random GUIDs be open for inspection and
 * modification. This class is free for all uses.
 * 
 *  @author Marc
 */
public class RandomGUID extends Object {
	private static final Logger logger = LoggerFactory.getLogger(RandomGUID.class);
	/**
	 * 
	 */
	private String valueBeforeMD5 = "";
	/**
	 * 
	 */
	private String valueAfterMD5 = "";
	/**
	 * 
	 */
	private static Random myRand;
	/**
	 * 
	 */
	private static SecureRandom mySecureRand;
	/**
	 * 
	 */
	private static String sId;
	/*
	 * Static block to take care of one time secureRandom seed. It takes a few seconds to initialize SecureRandom. You
	 * might want to consider removing this static block or replacing it with a "time since first loaded" seed to reduce
	 * this time. This block will run only once per JVM instance.
	 */

	static {
		mySecureRand = new SecureRandom();
		final long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			sId = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			;
		}

	}

	/**
	 * Default constructor. With no specification of security option, this constructor defaults to lower security, high
	 * performance.
	 */
	public RandomGUID() {
		super();
		getRandomGUID(false);
	}

	/**
	 * Constructor with security option. Setting secure true enables each random number generated to be
	 * cryptographically strong. Secure false defaults to the standard Random function seeded with a single
	 * cryptographically strong random number.
	 * @param secure Security
	 */
	public RandomGUID(final boolean secure) {
		super();
		getRandomGUID(secure);
	}

	/**
	 * Method to generate the random GUID
	 * @param secure Security
	 */
	private void getRandomGUID(final boolean secure) {
		MessageDigest md5 = null;
		final StringBuilder sbValueBeforeMD5 = new StringBuilder();

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			;
		}

		try {
			final long time = System.currentTimeMillis();
			long rand;

			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}

			// This StringBuilder can be a long as you need; the MD5
			// hash will always return 128 bits. You can change
			// the seed to include anything you want here.
			// You could even stream a file through the MD5 making
			// the odds of guessing it at least as great as that
			// of guessing the contents of the file!
			sbValueBeforeMD5.append(sId);
			sbValueBeforeMD5.append(':');
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(':');
			sbValueBeforeMD5.append(Long.toString(rand));

			valueBeforeMD5 = sbValueBeforeMD5.toString();
			md5.update(valueBeforeMD5.getBytes());

			final byte[] array = md5.digest();
			final StringBuilder strBu = new StringBuilder();
			for (int j = 0; j < array.length; ++j) {
				final int bit = array[j] & 0xFF;
				if (bit < 0x10) {
					strBu.append('0');
				}
				strBu.append(Integer.toHexString(bit));
			}

			valueAfterMD5 = strBu.toString();

		} catch (NullPointerException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Convert to the standard format for GUID (Useful for SQL Server UniqueIdentifiers, etc.) Example:
	 * C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
	 * @return String
	 */
	public String toString() {
		final String raw = valueAfterMD5.toUpperCase(new Locale("es", "ES"));
		final StringBuilder strbu = new StringBuilder();
		strbu.append(raw.substring(0, 8));
		strbu.append('-');
		strbu.append(raw.substring(8, 12));
		strbu.append('-');
		strbu.append(raw.substring(12, 16));
		strbu.append('-');
		strbu.append(raw.substring(16, 20));
		strbu.append('-');
		strbu.append(raw.substring(20));

		return strbu.toString().toLowerCase();
	}

}
