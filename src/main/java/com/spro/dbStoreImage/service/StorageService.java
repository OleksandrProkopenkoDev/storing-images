package com.spro.dbStoreImage.service;

import com.spro.dbStoreImage.entity.ImageData;
import com.spro.dbStoreImage.repository.StorageRepository;
import com.spro.dbStoreImage.util.ImageUtils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;

    public String uploadImage(MultipartFile file) {
        try {
            ImageData imageData = storageRepository.save(ImageData.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build());
            return "File uploaded successfully " + file.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("Error during converting image to byte array ", e);
        }
    }

    @Transactional(readOnly=true)
    public byte[] downloadImage(String fileName){
        Optional<ImageData> optionalImageData = storageRepository.findByName(fileName);
        return optionalImageData
                .map(imageData -> ImageUtils.decompressImage(imageData.getImageData()))
                .orElseGet(() -> new byte[0]);
    }
}
