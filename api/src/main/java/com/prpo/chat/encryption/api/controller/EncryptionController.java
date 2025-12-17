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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/encryption")
public class EncryptionController {

    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Operation(
        summary = "Encrypt a message",
        description = "Encrypts a plain text string and returns the encrypted value"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Message encrypted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public String encryptMessage(
        @Parameter(description = "Plain text to encrypt", required = true)
        @RequestBody String body
    ) throws Exception {
        return encryptionService.encryptString(body);
    }

    @Operation(
        summary = "Decrypt a message",
        description = "Decrypts an encrypted string and returns the original plain text"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Message decrypted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid encrypted input")
    })
    @PostMapping("/decryption")
    public String decryptMessage(
        @Parameter(description = "Encrypted text to decrypt", required = true)
        @RequestBody String body
    ) throws Exception {
        return encryptionService.decryptString(body);
    }

    @Operation(
        summary = "Decrypt messages in batch",
        description = "Decrypts a list of encrypted strings in a single request"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Messages decrypted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid encrypted input list")
    })
    @PostMapping(
        value = "/decryption/batch",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<String> decryptBatch(
        @Parameter(description = "List of encrypted strings", required = true)
        @RequestBody List<String> ciphertexts
    ) throws Exception {
        List<String> result = new ArrayList<>();
        for (String c : ciphertexts) {
            result.add(encryptionService.decryptString(c));
        }
        return result;
    }

    @Operation(
        summary = "Encrypt a file",
        description = "Encrypts a file and returns a Base64-encoded encrypted payload"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "File encrypted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file")
    })
    @PostMapping("/file")
    public FileEncryptionDto encryptFile(
        @Parameter(description = "File to encrypt", required = true)
        @RequestParam("file") MultipartFile file
    ) throws Exception {
        byte[] plainBytes = file.getBytes();
        byte[] encryptedBlob = encryptionService.encryptBytes(plainBytes);
        String base64 = Base64.getEncoder().encodeToString(encryptedBlob);
        return new FileEncryptionDto(file.getOriginalFilename(), base64);
    }

    @Operation(
        summary = "Decrypt a file",
        description = "Decrypts a Base64-encoded encrypted file and returns the original file bytes"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "File decrypted successfully",
            content = @Content(mediaType = "application/octet-stream")
        ),
        @ApiResponse(responseCode = "400", description = "Invalid encrypted file")
    })
    @PostMapping("/decryption/file")
    public ResponseEntity<byte[]> decryptFile(
        @Parameter(description = "Encrypted file payload", required = true)
        @RequestBody FileEncryptionDto fileEncryptionDto
    ) throws Exception {
        byte[] blob = Base64.getDecoder().decode(fileEncryptionDto.getData());
        byte[] plainBytes = encryptionService.decryptBytes(blob);

        return ResponseEntity.ok()
            .header("Content-Type", "application/octet-stream")
            .header(
                "Content-Disposition",
                "attachment; filename=\"" + fileEncryptionDto.getFileName() + "\""
            )
            .body(plainBytes);
    }
}
