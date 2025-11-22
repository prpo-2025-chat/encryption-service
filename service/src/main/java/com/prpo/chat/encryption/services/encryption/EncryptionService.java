package com.prpo.chat.encryption.services.encryption;

public interface EncryptionService {
    public abstract String encrypt(String plainText) throws Exception;
    public abstract String decrypt(String cipherText) throws Exception;
}