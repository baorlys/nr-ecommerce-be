package com.nr.ecommercebe.shared.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PemUtilTest {

    @Mock
    private Resource resource;

    @Mock
    private InputStream inputStream;

    private KeyPair keyPair;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        // Generate a test RSA key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        keyPair = keyGen.generateKeyPair();
    }

    @Test
    void loadPrivateKey_success_returnsPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Arrange
        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
                "\n-----END PRIVATE KEY-----";
        when(resource.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(privateKeyPem.getBytes());

        // Act
        PrivateKey privateKey = PemUtil.loadPrivateKey(resource);

        // Assert
        assertNotNull(privateKey);
        assertEquals("RSA", privateKey.getAlgorithm());
        assertArrayEquals(keyPair.getPrivate().getEncoded(), privateKey.getEncoded());
        verify(resource, times(1)).getInputStream();
        verify(inputStream, times(1)).readAllBytes();
        verify(inputStream, times(1)).close();
    }

    @Test
    void loadPrivateKey_ioException_throwsIOException() throws IOException {
        // Arrange
        when(resource.getInputStream()).thenThrow(new IOException("Resource not accessible"));

        // Act & Assert
        IOException exception = assertThrows(IOException.class,
                () -> PemUtil.loadPrivateKey(resource));
        assertEquals("Resource not accessible", exception.getMessage());
        verify(resource, times(1)).getInputStream();
        verifyNoInteractions(inputStream);
    }

    @Test
    void loadPrivateKey_invalidKeySpec_throwsInvalidKeySpecException() throws IOException{
        // Arrange
        String invalidPrivateKeyPem =
                """
                        -----BEGIN PRIVATE KEY-----
                        invalidBase64Data
                        -----END PRIVATE KEY-----
                        """;
        when(resource.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(invalidPrivateKeyPem.getBytes());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> PemUtil.loadPrivateKey(resource));
        verify(resource, times(1)).getInputStream();
        verify(inputStream, times(1)).readAllBytes();
        verify(inputStream, times(1)).close();
    }

    @Test
    void loadPublicKey_success_returnsPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Arrange
        String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) +
                "\n-----END PUBLIC KEY-----";
        when(resource.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(publicKeyPem.getBytes());

        // Act
        PublicKey publicKey = PemUtil.loadPublicKey(resource);

        // Assert
        assertNotNull(publicKey);
        assertEquals("RSA", publicKey.getAlgorithm());
        assertArrayEquals(keyPair.getPublic().getEncoded(), publicKey.getEncoded());
        verify(resource, times(1)).getInputStream();
        verify(inputStream, times(1)).readAllBytes();
        verify(inputStream, times(1)).close();
    }

    @Test
    void loadPublicKey_ioException_throwsIOException() throws IOException {
        // Arrange
        when(resource.getInputStream()).thenThrow(new IOException("Resource not accessible"));

        // Act & Assert
        IOException exception = assertThrows(IOException.class,
                () -> PemUtil.loadPublicKey(resource));
        assertEquals("Resource not accessible", exception.getMessage());
        verify(resource, times(1)).getInputStream();
        verifyNoInteractions(inputStream);
    }

    @Test
    void loadPublicKey_invalidKeySpec_throwsInvalidKeySpecException() throws IOException {
        // Arrange
        String invalidPublicKeyPem =
                """
                -----BEGIN PUBLIC KEY-----
                invalidBase64Data
                -----END PUBLIC KEY-----
                """;
        when(resource.getInputStream()).thenReturn(inputStream);
        when(inputStream.readAllBytes()).thenReturn(invalidPublicKeyPem.getBytes());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> PemUtil.loadPublicKey(resource));
        verify(resource, times(1)).getInputStream();
        verify(inputStream, times(1)).readAllBytes();
        verify(inputStream, times(1)).close();
    }
}