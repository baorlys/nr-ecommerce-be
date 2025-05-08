package com.nr.ecommercebe.module.media.web;

import com.nr.ecommercebe.module.media.application.service.MediaServiceContext;
import com.nr.ecommercebe.module.media.application.domain.StorageType;
import com.nr.ecommercebe.module.messaging.infrastructure.ImageDeletePublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/media")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Media", description = "APIs for uploading and deleting media files")
public class MediaController {

    MediaServiceContext mediaServiceContext;
    ImageDeletePublisher imageDeletePublisher;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Upload image",
            description = "Uploads an image file to Cloudinary and returns its URL",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid file or format"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<String> upload(
            @Parameter(description = "Image file to upload", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        String imgUrl = mediaServiceContext.uploadImage(file, StorageType.CLOUDINARY);
        return ResponseEntity.ok(imgUrl);
    }

    @DeleteMapping("/{imageUrl}")
    @Operation(
            summary = "Delete image",
            description = "Deletes an image by its URL using messaging queue",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Image deletion request accepted"),
                    @ApiResponse(responseCode = "400", description = "Missing or invalid image URL")
            }
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "URL of the image to delete", required = true)
            @PathVariable String imageUrl) {
        imageDeletePublisher.publish(imageUrl);
        return ResponseEntity.noContent().build();
    }
}
