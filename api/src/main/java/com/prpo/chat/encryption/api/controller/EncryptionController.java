package com.prpo.chat.encryption.api.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public String encryptMessage(@RequestBody String body) throws Exception {
        return encryptionService.encryptString(body);
    }

    @PostMapping
    @RequestMapping("/decryption")
    public String decryptMessage(@RequestBody String body) throws Exception {
        return encryptionService.decryptString(body);
    }

    @PostMapping(
        value = "/decryption/batch",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<String> decryptBatch(@RequestBody List<String> ciphertexts) throws Exception {
        List<String> result = new ArrayList<>();
        for (String c : ciphertexts) {
            result.add(encryptionService.decryptString(c));
        }
        return result;
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
