package com.nr.ecommercebe.module.media.application.service;

import com.nr.ecommercebe.module.media.application.domain.StorageType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

@Service
public class MediaServiceContext {
    private final Map<StorageType, MediaService> mediaServiceMap = new EnumMap<>(StorageType.class);
    private static final String DEFAULT_FOLDER = "vfood/images/";

    public MediaServiceContext(MediaService localMediaService, MediaService cloudinaryMediaService) {
        mediaServiceMap.put(StorageType.LOCAL, localMediaService);
        mediaServiceMap.put(StorageType.CLOUDINARY, cloudinaryMediaService);
    }

    public void registerMediaService(StorageType storageType, MediaService mediaService) {
        mediaServiceMap.put(storageType, mediaService);
    }

    public MediaService getMediaService(StorageType storageType) {
        return mediaServiceMap.get(storageType);
    }

    public String uploadImage(MultipartFile file, String folderName, StorageType storageType) throws IOException {
        MediaService mediaService = getMediaService(storageType);
        if (mediaService == null) {
            throw new IllegalArgumentException("No media service found for storage type: " + storageType);
        }
        return mediaService.uploadImage(file, folderName);
    }

    public String uploadImage(MultipartFile file, StorageType storageType) throws IOException {
        return uploadImage(file, DEFAULT_FOLDER, storageType);
    }







}
