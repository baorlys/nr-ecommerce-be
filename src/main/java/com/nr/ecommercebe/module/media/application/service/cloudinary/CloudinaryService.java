package com.nr.ecommercebe.module.media.application.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nr.ecommercebe.module.media.application.service.MediaService;
import com.nr.ecommercebe.module.messaging.config.RabbitConfig;
import com.nr.ecommercebe.module.media.event.ImageDeleteEvent;
import com.nr.ecommercebe.shared.exception.DeleteImageException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService implements MediaService {
    Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        Map<?, ?> data = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap("folder", folderName)
        );
        log.info("Image uploaded to Cloudinary: {}", data.get("url"));
        return (String) data.get("url");
    }

    @Retryable(
            retryFor = {DeleteImageException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 5000))
    @RabbitListener(queues = RabbitConfig.IMAGE_DELETE_QUEUE)
    public void deleteImage(ImageDeleteEvent event) {
        try {
            String publicId = extractPublicIdFromUrl(event.getImageUrl());
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Image deleted from Cloudinary: {}", event.getImageUrl());
        } catch (Exception e) {
            log.error("Failed to delete image from Cloudinary: {}", event.getImageUrl(), e);
            throw new DeleteImageException("Failed to delete image" , e);
        }
    }

    public String extractPublicIdFromUrl(String imageUrl) throws IOException {
        URI uri = URI.create(imageUrl);
        URL url = uri.toURL();
        String path = url.getPath();

        int uploadIndex = path.indexOf("/upload/");
        if (uploadIndex == -1) return null;

        String afterUpload = path.substring(uploadIndex + "/upload/".length());
        String[] segments = afterUpload.split("/");

        // Skip version (e.g., v12345678)
        if (segments.length >= 2 && segments[0].startsWith("v")) {
            afterUpload = afterUpload.substring(segments[0].length() + 1);
        }

        // Remove file extension
        int dotIndex = afterUpload.lastIndexOf('.');
        return (dotIndex != -1) ? afterUpload.substring(0, dotIndex) : afterUpload;
    }
}
