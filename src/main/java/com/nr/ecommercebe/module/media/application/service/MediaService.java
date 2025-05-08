package com.nr.ecommercebe.module.media.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaService {
    String uploadImage(MultipartFile file, String folderName) throws IOException;

}
