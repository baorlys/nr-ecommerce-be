package com.nr.ecommercebe.module.media.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nr.ecommercebe.module.media.MediaService;
import com.nr.ecommercebe.shared.exception.DeleteImageException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@Service
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
        return (String) data.get("url");
    }

    @RabbitListener
    public void deleteImage(String imageUrl) {
        try {
            String publicId = extractPublicIdFromUrl(imageUrl);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new DeleteImageException("Failed to delete image: " + e.getMessage());
        }
    }

    private String extractPublicIdFromUrl(String imageUrl) throws IOException {
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
