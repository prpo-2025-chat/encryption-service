package com.prpo.chat.encryption.services.encryption;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int KEY_LENGTH = 256;
    private static final int PBKDF2_ITERATIONS = 100_000;
    private static final int GCM_TAG_LENGTH = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    private String getPassword() {
        String password = System.getenv("CHAT_APP_ENCRYPTION_PASSWORD");
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("CHAT_APP_ENCRYPTION_PASSWORD is not set!");
        }
        return password;
    }

    private SecretKey deriveKeyFromPassword(byte[] salt) throws GeneralSecurityException {
        String password = this.getPassword();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Override
    public String encryptString(String plaintext) throws Exception {
        byte[] ciphertextBlob = encryptBytes(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(ciphertextBlob);
    }

    @Override
    public String decryptString(String ciphertextBase64) throws Exception {
        byte[] blob = Base64.getDecoder().decode(ciphertextBase64);
        byte[] plaintextBytes = decryptBytes(blob);
        return new String(plaintextBytes, StandardCharsets.UTF_8);
    }

    @Override
    public byte[] encryptBytes(byte[] data) throws Exception {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        SecretKey key = deriveKeyFromPassword(salt);

        byte[] iv = new byte[IV_LENGTH];
        secureRandom.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec);

        byte[] ciphertextWithTag = cipher.doFinal(data);

        byte[] blob = new byte[SALT_LENGTH + IV_LENGTH + ciphertextWithTag.length];
        System.arraycopy(salt, 0, blob, 0, SALT_LENGTH);
        System.arraycopy(iv, 0, blob, SALT_LENGTH, IV_LENGTH);
        System.arraycopy(ciphertextWithTag, 0, blob, SALT_LENGTH + IV_LENGTH, ciphertextWithTag.length);

        return blob;
    }

    @Override
    public byte[] decryptBytes(byte[] blob) throws Exception {
        if (blob.length < SALT_LENGTH + IV_LENGTH + 1) {
            throw new IllegalArgumentException("Ciphertext blob too short");
        }

        byte[] salt = new byte[SALT_LENGTH];
        byte[] iv = new byte[IV_LENGTH];
        byte[] ciphertextWithTag = new byte[blob.length - SALT_LENGTH - IV_LENGTH];

        System.arraycopy(blob, 0, salt, 0, SALT_LENGTH);
        System.arraycopy(blob, SALT_LENGTH, iv, 0, IV_LENGTH);
        System.arraycopy(blob, SALT_LENGTH + IV_LENGTH, ciphertextWithTag, 0, ciphertextWithTag.length);

        SecretKey key = deriveKeyFromPassword(salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec);

        return cipher.doFinal(ciphertextWithTag);
    }
}
