package com.filesfromyou.logprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileManagementService {

    Logger log = LoggerFactory.getLogger(FileManagementService.class);

    public void init() {
        log.info("Creating upload directories");
        try {
            for(UploadDirectory directory : UploadDirectory.values()) {
                Files.createDirectories(directory.getPath());
            }
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage());
            throw new RuntimeException("Could not initialize directory for upload!");
        }
    }

    public void save(MultipartFile file, UploadDirectory directory) {
        log.info("Saving " + file.getOriginalFilename());
        try {
            InputStream source = file.getInputStream();
            Path targetPath = directory.getPath().resolve(file.getOriginalFilename());
            Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException feEx) {
            log.error("FileAlreadyExistsException: " + feEx.getMessage());
            throw new RuntimeException("Could not save the file");
        } catch (IOException ioEx) {
            log.error("IOException: " + ioEx.getMessage());
            throw new RuntimeException("Could not save the file");
        }
    }
}
