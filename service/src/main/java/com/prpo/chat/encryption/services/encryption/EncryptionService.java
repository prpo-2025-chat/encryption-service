package com.prpo.chat.encryption.services.encryption;

public interface EncryptionService {
    public abstract String encryptString(String plainText) throws Exception;
    public abstract String decryptString(String cipherText) throws Exception;
    public abstract byte[] encryptBytes(byte[] data) throws Exception;
    public abstract byte[] decryptBytes(byte[] blob) throws Exception;
}