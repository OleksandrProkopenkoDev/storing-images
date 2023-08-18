package com.spro.dbStoreImage.repository;

import com.spro.dbStoreImage.entity.FileData;
import com.spro.dbStoreImage.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {
    Optional<FileData> findByName(String name);
}
