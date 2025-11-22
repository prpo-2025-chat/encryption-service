package com.prpo.chat.encryption.api.dto;

public class EncryptionResponseDto {
    private final String encrypted;

    public EncryptionResponseDto(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getEncrypted() {
        return encrypted;
    }
}
