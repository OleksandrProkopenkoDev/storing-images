package com.spro.dbStoreImage.controller;

import com.spro.dbStoreImage.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ImageController {

    private final StorageService storageService;

    @PostMapping("/database")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile file){
        String uploadImage = storageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/database/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
        byte[] downloadedImage = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpg"))
                .body(downloadedImage);
    }

    @PostMapping("/filesystem")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image")MultipartFile file){
        String uploadImage = storageService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/filesystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName){
        byte[] downloadedImage = storageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpg"))
                .body(downloadedImage);
    }
}
