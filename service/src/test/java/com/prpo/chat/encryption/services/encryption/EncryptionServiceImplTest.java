package com.prpo.chat.encryption.services.encryption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class EncryptionServiceImplTest {

    @Test
    void encryptAndDecryptString_roundTrip() throws Exception {
        EncryptionServiceImpl service = new EncryptionServiceImpl();
        String plainText = "unit-test-payload";

        String cipherText = service.encryptString(plainText);

        assertNotNull(cipherText);
        assertFalse(cipherText.isBlank());

        String decrypted = service.decryptString(cipherText);

        assertEquals(plainText, decrypted);
    }
}
