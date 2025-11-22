package com.prpo.chat.encryption.services.password;

public interface PasswordHashingService {
    public abstract String hashPassword(String password) throws Exception;
    public abstract Boolean comparePassword(String password, String hashedPassword) throws Exception;
}