package com.prpo.chat.encryption.services.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordHashingServiceImpl implements PasswordHashingService {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hashPassword(String password) throws Exception {
        String hashed = encoder.encode(password);
        return hashed;
    }

    @Override
    public Boolean comparePassword(String password, String hashedPassword) throws Exception {
        boolean matches = encoder.matches(password, hashedPassword);
        if (matches) {
            return true;
        } else {
            throw new Exception("Incorrect password");
        }
    }
}