package com.prpo.chat.encryption.api.dto;

public class FileEncryptionDto {
    private String fileName;
    private String data;

    public FileEncryptionDto(String fileName, String data) {
        this.fileName = fileName;
        this.data = data;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getData() {
        return this.data;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setData(String data) {
        this.data = data;
    }
}
