package com.spro.dbStoreImage.service;

import com.spro.dbStoreImage.entity.FileData;
import com.spro.dbStoreImage.entity.ImageData;
import com.spro.dbStoreImage.repository.FileDataRepository;
import com.spro.dbStoreImage.repository.StorageRepository;
import com.spro.dbStoreImage.util.ImageUtils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StorageService {

    private final StorageRepository storageRepository;
    private final FileDataRepository fileDataRepository;
    private final static String FOLDER_PATH = "src/main/resources/images/";

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

    @Transactional(readOnly = true)
    public byte[] downloadImage(String fileName) {
        Optional<ImageData> optionalImageData = storageRepository.findByName(fileName);
        return optionalImageData
                .map(imageData -> ImageUtils.decompressImage(imageData.getImageData()))
                .orElseGet(() -> new byte[0]);
    }


    public String uploadImageToFileSystem(MultipartFile file) {
        String filePath = FOLDER_PATH + file.getOriginalFilename();

        fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .build()
        );
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Error during saving file to local storage", e);
        }
        return "File uploaded successfully " + file.getOriginalFilename();

    }


    public byte[] downloadImageFromFileSystem(String fileName) {

        Optional<FileData> fileDataOptional = fileDataRepository.findByName(fileName);
        String filePath = "";
        if(fileDataOptional.isPresent()) {
            filePath = fileDataOptional.get().getFilePath();
        }
        try {
            return Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error during reading file from local storage",e);
        }
    }
}
