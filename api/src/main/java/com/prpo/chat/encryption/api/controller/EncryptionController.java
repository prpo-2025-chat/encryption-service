package com.prpo.chat.encryption.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prpo.chat.encryption.api.dto.EncryptionRequestDto;
import com.prpo.chat.encryption.api.dto.EncryptionResponseDto;
import com.prpo.chat.encryption.services.encryption.EncryptionService;

@RestController
@RequestMapping("/encryption")
public class EncryptionController {
    
    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping
    public EncryptionResponseDto encryptMessage(@RequestBody EncryptionRequestDto body) throws Exception {
        String encryptedMessage = encryptionService.encrypt(body.getMessage());
        return new EncryptionResponseDto(encryptedMessage);
    }

    @PostMapping
    @RequestMapping("/decryption")
    public EncryptionResponseDto decryptMessage(@RequestBody EncryptionRequestDto body) throws Exception {
        String decryptedMessage = encryptionService.decrypt(body.getMessage());
        return new EncryptionResponseDto(decryptedMessage);
    }
}
