package com.khee.dev.component;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class DefaultComponent implements CommandLineRunner {

	private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
	private static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
	private static final int IV_LENGTH_BYTE = 12;
	private static final int SALT_LENGTH_BYTE = 16;

	private static final String cryptograpyKey = "secret";
	private static final String unencryptedFileName = "unencrypted_file";
	private static final String encryptedFileName = "encrypted_file";

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void run(String... args) throws Exception {

		Resource resource = resourceLoader.getResource("classpath:" + unencryptedFileName);

		if (resource.exists()) {
			try {
				encryptFile(resource);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println(unencryptedFileName + " could not be found in resources folder");
		}

		resource = resourceLoader.getResource("classpath:" + encryptedFileName);

		if (resource.exists()) {
			try {
				decryptFile(resource);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println(encryptedFileName + " could not be found in resources folder");
		}

		System.exit(0);
	}

	private void encryptFile(Resource resource) throws Exception {
		System.out.println("File encryption process running on [" + resource.getFilename() + "]...");

		// read a normal txt file
		byte[] fileContent = Files.readAllBytes(Paths.get(resource.getURI()));

		// encrypt with a password
		byte[] encryptedText = encrypt(fileContent, cryptograpyKey);

		// save a file
		Path path = Paths.get("src/main/resources/" + encryptedFileName);

		Files.write(path, encryptedText);

		System.out.println("File encryption process completed...Encrypted file is [" + encryptedFileName + "]");
	}

	private byte[] encrypt(byte[] pText, String password) throws Exception {
		// 16 bytes salt
		byte[] salt = getRandomNonce(SALT_LENGTH_BYTE);

		// GCM recommended 12 bytes iv?
		byte[] iv = getRandomNonce(IV_LENGTH_BYTE);

		// secret key from password
		SecretKey aesKeyFromPassword = getAESKeyFromPassword(password.toCharArray(), salt);

		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

		// ASE-GCM needs GCMParameterSpec
		cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

		byte[] cipherText = cipher.doFinal(pText);

		// prefix IV and Salt to cipher text
		return ByteBuffer.allocate(iv.length + salt.length + cipherText.length).put(iv).put(salt).put(cipherText)
				.array();
	}

	private void decryptFile(Resource resource) {
		// read a file
		byte[] fileContent = null;
		byte[] decryptedText = null;

		try {
			fileContent = Files.readAllBytes(Paths.get(resource.getURI()));
			decryptedText = decrypt(fileContent, cryptograpyKey);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		String pText = new String(decryptedText, StandardCharsets.UTF_8);

		Scanner scanner = new Scanner(pText);

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println(line);
		}

		scanner.close();
	}

	private byte[] decrypt(byte[] cText, String password) throws Exception {
		// get back the iv and salt that was prefixed in the cipher text
		ByteBuffer bb = ByteBuffer.wrap(cText);

		byte[] iv = new byte[12];
		bb.get(iv);

		byte[] salt = new byte[16];
		bb.get(salt);

		byte[] cipherText = new byte[bb.remaining()];
		bb.get(cipherText);

		// get back the aes key from the same password and salt
		SecretKey aesKeyFromPassword = getAESKeyFromPassword(password.toCharArray(), salt);

		Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

		cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

		return cipher.doFinal(cipherText);
	}

	private byte[] getRandomNonce(int numBytes) {
		byte[] nonce = new byte[numBytes];
		new SecureRandom().nextBytes(nonce);
		return nonce;
	}

	// AES 256 bits secret key derived from a password
	private SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		// iterationCount = 65536
		// keyLength = 256
		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);

		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}
}
