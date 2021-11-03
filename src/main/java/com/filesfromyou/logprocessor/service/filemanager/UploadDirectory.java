package com.filesfromyou.logprocessor.service.filemanager;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum UploadDirectory {
    SYSTEM("upload/clientsystemlog"),
    DETAIL("upload/clientapplog");

    private final String path;

    UploadDirectory(String path) {
        this.path = path;
    }

    public Path getPath() {
        return Paths.get(path);
    }
}