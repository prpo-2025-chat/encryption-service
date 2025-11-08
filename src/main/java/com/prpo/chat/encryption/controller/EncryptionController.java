package com.prpo.chat.encryption.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/encryption")
public class EncryptionController {
    
    @PostMapping
    public String encryptMessage(@RequestBody String body) {
        return body;
    }
}
