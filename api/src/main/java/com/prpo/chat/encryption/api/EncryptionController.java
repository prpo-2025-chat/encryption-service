package com.prpo.chat.encryption.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prpo.chat.encryption.services.EncryptionService;

@RestController
@RequestMapping("/encryption")
public class EncryptionController {
    
    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public String encryptMessage(@RequestBody String body) throws Exception {
        return encryptionService.encrypt(body);
    }

    @PostMapping
    @RequestMapping("/decryption")
    public String decryptMessage(@RequestBody String body) throws Exception {
        return encryptionService.decrypt(body);
    }
}
