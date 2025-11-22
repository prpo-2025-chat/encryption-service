package com.prpo.chat.encryption.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prpo.chat.encryption.api.dto.PasswordHashDto;
import com.prpo.chat.encryption.services.password.PasswordHashingService;

@RestController
@RequestMapping("/password")
public class PasswordHashController {
    
    private final PasswordHashingService passwordHashingService;

    public PasswordHashController(PasswordHashingService passwordHashingService) {
        this.passwordHashingService = passwordHashingService;
    }

    @PostMapping
    public PasswordHashDto hashPassword(@RequestBody PasswordHashDto body) throws Exception {
        String hashedPassword = passwordHashingService.hashPassword(body.getPassword());
        body.setHashedPassword(hashedPassword);
        return body;
    }

    @PostMapping
    @RequestMapping("/validation")
    public Boolean validatePassword(@RequestBody PasswordHashDto body) throws Exception {
        Boolean validated = passwordHashingService.comparePassword(body.getPassword(), body.getHashedPassword());
        return validated;
    }
}