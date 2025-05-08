package com.nr.ecommercebe.module.media.application.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.nr.ecommercebe.module.media.event.ImageDeleteEvent;
import com.nr.ecommercebe.shared.exception.DeleteImageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    private MultipartFile multipartFile;
    private ImageDeleteEvent imageDeleteEvent;

    @BeforeEach
    void setUp() {
        // Mock MultipartFile
        multipartFile = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "test content".getBytes());

        // Mock ImageDeleteEvent
        imageDeleteEvent = new ImageDeleteEvent("https://res.cloudinary.com/demo/image/upload/v1234567890/folder/test.jpg");
    }

    @Test
    void uploadImage_success_returnsUrl() throws IOException {
        // Arrange
        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "https://res.cloudinary.com/demo/image/upload/v1234567890/folder/test.jpg");
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        // Act
        String result = cloudinaryService.uploadImage(multipartFile, "folder");

        // Assert
        assertEquals("https://res.cloudinary.com/demo/image/upload/v1234567890/folder/test.jpg", result);
        verify(uploader, times(1)).upload(any(byte[].class), eq(Map.of("folder", "folder")));
    }

    @Test
    void uploadImage_ioException_throwsIOException() throws IOException {
        // Arrange
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Upload failed"));

        // Act & Assert
        IOException exception = assertThrows(IOException.class,
                () -> cloudinaryService.uploadImage(multipartFile, "folder"));
        assertEquals("Upload failed", exception.getMessage());
        verify(uploader, times(1)).upload(any(byte[].class), anyMap());
    }

    @Test
    void deleteImage_success_deletesImage() throws IOException {
        // Arrange
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(eq("folder/test"), anyMap())).thenReturn(new HashMap<>());

        // Act
        cloudinaryService.deleteImage(imageDeleteEvent);

        // Assert
        verify(uploader, times(1)).destroy(eq("folder/test"), anyMap());
    }

    @Test
    void deleteImage_failure_throwsDeleteImageException() throws IOException {
        // Arrange
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(eq("folder/test"), anyMap())).thenThrow(new IOException("Deletion failed"));

        // Act & Assert
        DeleteImageException exception = assertThrows(DeleteImageException.class,
                () -> cloudinaryService.deleteImage(imageDeleteEvent));
        assertTrue(exception.getMessage().contains("Failed to delete image"));
        verify(uploader, times(1)).destroy(eq("folder/test"), anyMap());
    }

    @Test
    void extractPublicIdFromUrl_withVersion_success() throws IOException {
        // Arrange
        String url = "https://res.cloudinary.com/demo/image/upload/v1234567890/folder/test.jpg";

        // Act
        String publicId = cloudinaryService.extractPublicIdFromUrl(url);

        // Assert
        assertEquals("folder/test", publicId);
    }

    @Test
    void extractPublicIdFromUrl_withoutVersion_success() throws IOException {
        // Arrange
        String url = "https://res.cloudinary.com/demo/image/upload/folder/test.png";

        // Act
        String publicId = cloudinaryService.extractPublicIdFromUrl(url);

        // Assert
        assertEquals("folder/test", publicId);
    }

    @Test
    void extractPublicIdFromUrl_invalidUrl_returnsNull() throws IOException {
        // Arrange
        String url = "https://res.cloudinary.com/demo/invalid/path/test.jpg";

        // Act
        String publicId = cloudinaryService.extractPublicIdFromUrl(url);

        // Assert
        assertNull(publicId);
    }

    @Test
    void extractPublicIdFromUrl_malformedUrl_throwsIOException() {
        // Arrange
        String url = "invalid-url";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cloudinaryService.extractPublicIdFromUrl(url));
    }

}