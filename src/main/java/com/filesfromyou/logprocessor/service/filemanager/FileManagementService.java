package com.filesfromyou.logprocessor.service.filemanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileManagementService {

    Logger log = LoggerFactory.getLogger(FileManagementService.class);

    public void init() {
        createUploadDirectories();
        retryProcessingFiles();
    }

    public void createUploadDirectories() {
        log.info("Creating upload directories");
        try {
            for (UploadDirectory directory : UploadDirectory.values()) {
                Files.createDirectories(directory.getPath());
            }
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage());
            throw new RuntimeException("Could not initialize directory for upload!");
        }
    }

    public void retryProcessingFiles() {
        log.info("Moving directories to re-process again");
        try {
            for (UploadDirectory directory : UploadDirectory.values()) {
                File[] listOfFiles = directory.getPath().resolve("error").toFile().listFiles();
                if (listOfFiles != null) {
                    for (File child : listOfFiles ) {
                        child.renameTo(new File(directory.getPath() + "\\" + child.getName()));
                    }
                    directory.getPath().resolve("error").toFile().delete();
                }
            }
        } catch (Exception e) {
            log.error("IOException: " + e.getMessage());
            throw new RuntimeException("Could not move directory for re-processing!");
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

    public void save(String fileContent, String fileName, UploadDirectory directory) {
        log.info("Saving " + fileName);
        try {
            Path targetPath = directory.getPath().resolve(fileName);
            Files.writeString(targetPath, fileContent);
        } catch (FileAlreadyExistsException feEx) {
            log.error("FileAlreadyExistsException: " + feEx.getMessage());
            throw new RuntimeException("Could not save the file");
        } catch (IOException ioEx) {
            log.error("IOException: " + ioEx.getMessage());
            throw new RuntimeException("Could not save the file");
        }
    }
}
