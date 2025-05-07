package com.nr.ecommercebe.module.media.web;

import com.nr.ecommercebe.module.media.application.service.MediaServiceContext;
import com.nr.ecommercebe.module.media.application.domain.StorageType;
import com.nr.ecommercebe.module.messaging.infrastructure.ImageDeletePublisher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/media")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MediaController {
    MediaServiceContext mediaServiceContext;
    ImageDeletePublisher imageDeletePublisher;

    @PostMapping(value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(MultipartFile file) throws IOException {
        String imgUrl = mediaServiceContext.uploadImage(file, StorageType.CLOUDINARY);
        return ResponseEntity.ok(imgUrl);
    }

    @DeleteMapping()
    public ResponseEntity<Void> delete(String imgUrl) {
        imageDeletePublisher.publish(imgUrl);
        return ResponseEntity.noContent().build();
    }

}
