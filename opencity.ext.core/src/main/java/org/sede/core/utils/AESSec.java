package org.sede.core.utils;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESSec {
	private AESSec() {
		super();
	}
	private static final String ALGO = "AES";
	private static final byte[] keyValueDefault = new byte[] { 'C', 'a', '4', 'M',
			'Q', 'A', 'S', 'C', 'V', '7', 'Z', 'X', 'l', 'F', 'a', '6' };

	private static final byte[] keyValueAddentra = new byte[] { '5', 'a', '4', 'G',
		'N', 'T', 's', 'C', 'V', '9', 'Z', 'w', 'L', 'X', 'b', '6' };
	
	public static String encrypt(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = generateKeyDefault();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data.getBytes());
		return Base64.encodeBase64String(encVal);
	}

	public static String encryptAddentra(String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = generateKeyAddentra();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data.getBytes());
		return Base64.encodeBase64String(encVal);
	}
	
	public static String decrypt(String encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = generateKeyDefault();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.decodeBase64(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		return new String(decValue);
	}

	public static String decryptAddentra(String encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Key key = generateKeyAddentra();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = Base64.decodeBase64(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		return new String(decValue);
	}
	
	private static Key generateKeyDefault() {
		return new SecretKeySpec(keyValueDefault, ALGO);
	}
	
	private static Key generateKeyAddentra() {
		return new SecretKeySpec(keyValueAddentra, ALGO);
	}

}
