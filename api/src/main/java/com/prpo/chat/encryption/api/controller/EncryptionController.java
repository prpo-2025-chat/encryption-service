package com.prpo.chat.encryption.api.controller;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prpo.chat.encryption.api.dto.EncryptionRequestDto;
import com.prpo.chat.encryption.api.dto.EncryptionResponseDto;
import com.prpo.chat.encryption.api.dto.FileEncryptionDto;
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
        String encryptedMessage = encryptionService.encryptString(body.getMessage());
        return new EncryptionResponseDto(encryptedMessage);
    }

    @PostMapping
    @RequestMapping("/decryption")
    public EncryptionResponseDto decryptMessage(@RequestBody EncryptionRequestDto body) throws Exception {
        String decryptedMessage = encryptionService.decryptString(body.getMessage());
        return new EncryptionResponseDto(decryptedMessage);
    }

    @PostMapping("/file")
    public FileEncryptionDto encryptFile(@RequestParam("file") MultipartFile file) throws Exception {
        byte[] plainBytes = file.getBytes();
        byte[] encryptedBlob = encryptionService.encryptBytes(plainBytes);
        String base64 = Base64.getEncoder().encodeToString(encryptedBlob);
        return new FileEncryptionDto(file.getOriginalFilename(), base64);
    }

    @PostMapping("/decryption/file")
    public ResponseEntity<byte[]> decryptFile(@RequestBody FileEncryptionDto fileEncryptionDto) throws Exception {
        byte[] blob = Base64.getDecoder().decode(fileEncryptionDto.getData());
        byte[] plainBytes = encryptionService.decryptBytes(blob);

        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
            .header("Content-Type", contentType)
            .header("Content-Disposition", "attachment; filename=\"" + fileEncryptionDto.getFileName() + "\"")
            .body(plainBytes);
    }
}
