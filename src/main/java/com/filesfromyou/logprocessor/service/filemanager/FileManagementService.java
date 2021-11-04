package com.filesfromyou.logprocessor.service.filemanager;

import com.filesfromyou.logprocessor.exception.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;

@Service
public class FileManagementService {

    @Value("#{'${logprocessor.folders}'.split(';')}")
    private List<String> folders;

    Logger log = LoggerFactory.getLogger(FileManagementService.class);

    public void init() {
        createUploadDirectories();
        retryProcessingFiles();
    }

    public void createUploadDirectories() {
        log.info("Creating upload directories");
        try {
            for (String folder : folders) {
                Files.createDirectories(Path.of(folder));
            }
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage());
            throw new RuntimeException("Could not initialize directory for upload!");
        }
    }

    public void retryProcessingFiles() {
        log.info("Moving directories to re-process again");
        try {
            for (String folder : folders) {
                Path folderPath = Path.of(folder);
                File[] listOfFiles = folderPath.resolve("error").toFile().listFiles();
                if (listOfFiles != null) {
                    for (File child : listOfFiles ) {
                        child.renameTo(new File(folderPath + "\\" + child.getName()));
                    }
                    folderPath.resolve("error").toFile().delete();
                }
            }
        } catch (Exception e) {
            log.error("IOException: " + e.getMessage());
            throw new RuntimeException("Could not move directory for re-processing!");
        }
    }

    public void save(MultipartFile file, Path filePath) {
        log.info("Saving " + file.getOriginalFilename());
        try {
            InputStream source = file.getInputStream();
            Path targetPath = filePath.resolve(file.getOriginalFilename());
            Files.copy(source, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (FileAlreadyExistsException feEx) {
            log.error("FileAlreadyExistsException: " + feEx.getMessage());
            throw new FileUploadException();
        } catch (IOException ioEx) {
            log.error("IOException: " + ioEx.getMessage());
            throw new FileUploadException();
        }
    }

    public void save(String fileContent, String fileName, Path filePath) {
        log.info("Saving " + fileName);
        try {
            Path targetPath = filePath.resolve(fileName);
            Files.writeString(targetPath, fileContent);
        } catch (FileAlreadyExistsException feEx) {
            log.error("FileAlreadyExistsException: " + feEx.getMessage());
            throw new FileUploadException();
        } catch (IOException ioEx) {
            log.error("IOException: " + ioEx.getMessage());
            throw new FileUploadException();
        }
    }
}
