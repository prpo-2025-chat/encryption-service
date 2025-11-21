package com.prpo.chat.encryption.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.prpo.chat.encryption")
public class EncryptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(EncryptionApplication.class, args);
    }
}
