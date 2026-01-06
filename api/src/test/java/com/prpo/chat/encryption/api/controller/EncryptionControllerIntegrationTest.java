package com.prpo.chat.encryption.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.prpo.chat.encryption.api.EncryptionApplication;

@SpringBootTest(classes = EncryptionApplication.class)
@AutoConfigureMockMvc
class EncryptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void encryptThenDecrypt_roundTripThroughHttp() throws Exception {
        String plainText = "integration-test-message";

        MvcResult encryptResult = mockMvc.perform(post("/encryption")
                .contentType(MediaType.TEXT_PLAIN)
                .content(plainText))
            .andExpect(status().isOk())
            .andReturn();

        String cipherText = encryptResult.getResponse().getContentAsString();

        assertNotNull(cipherText);
        assertFalse(cipherText.isBlank());

        MvcResult decryptResult = mockMvc.perform(post("/encryption/decryption")
                .contentType(MediaType.TEXT_PLAIN)
                .content(cipherText))
            .andExpect(status().isOk())
            .andReturn();

        String decrypted = decryptResult.getResponse().getContentAsString();

        assertEquals(plainText, decrypted);
    }
}
