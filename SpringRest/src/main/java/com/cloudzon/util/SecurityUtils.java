//This code is the property of CloudZon Inc.
//Copyright CloudZon Inc.

package com.cloudzon.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.codec.Base64;

/**
 * This Class contains data Security related methods.
 */
public class SecurityUtils {

	public static final int ENCRYPTION_KEY_LENGTH = 24;
	private static final Logger LOG = Logger.getLogger(SecurityUtils.class);

	/**
	 * This method computes Message Digest using MD5 algorithm for the given
	 * input.
	 * 
	 * @param input
	 *            String for which message digest to be calculated
	 * @return String message digest
	 * @throws Exception
	 */
	public static String getMD5(String input) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		digest.reset();
		digest.update(input.getBytes());
		byte[] hash = digest.digest();
		return byteArrayToHexString(hash);
	}

	/**
	 * This method Generates a String using HMAC-MD5 hashing algorithm
	 * 
	 * @param input
	 * @return secret
	 */
	public static String getAuthenticationTokenOrSecret(String input) {
		LOG.debug(SecurityUtils.class + ": getAuthenticationTokenOrSecret()");
		String secret = "";
		try {
			// Generate a key for the HMAC-MD5 keyed-hashing
			// algorithm; see RFC
			// 2104
			// In practice, you would save this key.
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
			SecretKey key = keyGen.generateKey();

			// Create a MAC object using HMAC-MD5 and initialize
			// with key
			Mac mac = Mac.getInstance(key.getAlgorithm());
			mac.init(key);

			// Encode the string into bytes using utf-8 and digest
			// it
			byte[] utf8 = input.getBytes("UTF8");
			byte[] digest = mac.doFinal(utf8);

			// If desired, convert the digest into a string
			// secret = new sun.misc.BASE64Encoder().encode(digest);

			secret = new String(Base64.encode(digest));
		} catch (InvalidKeyException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		}
		return secret;
	}

	/**
	 * This method encrypts the input string using key
	 * 
	 * @param key
	 * @param input
	 * @return encrypted token
	 */
	public static String getEncryptedToken(String key, String input) {
		LOG.debug(SecurityUtils.class + ": getEncryptedToken()");
		String encToken = "";
		try {
			Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
			// Create the cipher
			Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// Initialize the cipher for encryption
			aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);

			// Our cleartext
			byte[] cleartext = input.getBytes();

			// Encrypt the cleartext
			byte[] ciphertext = aesCipher.doFinal(cleartext);
			// sun.misc.BASE64Encoder().encode(ciphertext);
			encToken = new String(Base64.encode(ciphertext));
			LOG.debug("encToken=" + encToken);

		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		}
		return encToken;
	}

	// encrypt the input string using key and match with provided signature
	/*
	 * public static boolean matchSignature(String key, String input, String
	 * signature) { LOG.debug("matchSignature()"); String encToken = ""; boolean
	 * match = false; try { Key secretKey = new SecretKeySpec(key.getBytes(),
	 * "AES"); // Create the cipher Cipher aesCipher = Cipher
	 * .getInstance("AES/ECB/PKCS5Padding"); // Initialize the cipher for
	 * encryption aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
	 * 
	 * // Our cleartext byte[] cleartext = input.getBytes();
	 * 
	 * // Encrypt the cleartext byte[] ciphertext =
	 * aesCipher.doFinal(cleartext); encToken = new sun.misc.BASE64Encoder()
	 * .encode(ciphertext);
	 * 
	 * match = encToken.equals(signature); } catch (NoSuchAlgorithmException e)
	 * { LOG.error(e.toString(), e);e.printStackTrace(); } catch
	 * (NoSuchPaddingException e) { LOG.error(e.toString(),
	 * e);e.printStackTrace(); } catch (IllegalBlockSizeException e) {
	 * LOG.error(e.toString(), e);e.printStackTrace(); } catch
	 * (BadPaddingException e) { LOG.error(e.toString(), e);e.printStackTrace();
	 * } catch (InvalidKeyException e) { LOG.error(e.toString(),
	 * e);e.printStackTrace(); } return match; }
	 */

	/**
	 * This method match signature matching using MD5
	 * 
	 * @param key
	 * @param input
	 * @param signature
	 * @return boolean
	 */
	public static boolean matchSignatureUsingMD5(String key, String input,
			String signature) {
		LOG.debug(SecurityUtils.class + ": matchSignatureUsingMD5()");
		String encToken = "";
		boolean match = false;
		try {
			encToken = getMD5(input + key);
			match = encToken.equals(signature);
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (BadPaddingException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		} catch (Exception e) {
			LOG.error(e.toString(), e);
			e.printStackTrace();
		}
		return match;
	}

	/**
	 * This method converts an array of bytes into a hex string
	 * 
	 * @param input
	 * @return String
	 */
	private static String byteArrayToHexString(byte input[]) {

		StringBuffer hexString = new StringBuffer();
		byte n1, n2;

		for (int c = 0; c < input.length; c++) {
			n1 = (byte) ((input[c] & 0xF0) >> 4);
			n2 = (byte) ((input[c] & 0x0F));

			hexString.append(n1 >= 0xA ? (char) (n1 - 0xA + 'a')
					: (char) (n1 + '0'));
			hexString.append(n2 >= 0xA ? (char) (n2 - 0xA + 'a')
					: (char) (n2 + '0'));
		}

		return hexString.toString();
	}

	public static void main(String[] args) {
		System.out.println(getAuthenticationTokenOrSecret("hitesh"));
	}
}
