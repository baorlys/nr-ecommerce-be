package com.nr.ecommercebe.web;

import com.nr.ecommercebe.module.media.MediaServiceContext;
import com.nr.ecommercebe.module.media.StorageType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/upload",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(MultipartFile file) throws IOException {
        return mediaServiceContext.uploadImage(file, StorageType.CLOUDINARY);
    }

}
