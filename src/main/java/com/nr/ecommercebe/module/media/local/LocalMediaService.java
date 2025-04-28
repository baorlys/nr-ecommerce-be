package com.nr.ecommercebe.module.media.local;

import com.nr.ecommercebe.module.media.MediaService;
import org.springframework.web.multipart.MultipartFile;

public class LocalMediaService implements MediaService {
    @Override
    public String uploadImage(MultipartFile file, String folderName) {
        return "";
    }
}
