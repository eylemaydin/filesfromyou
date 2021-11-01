package com.filesfromyou.logprocessor.service;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum UploadDirectory {
    SYSTEM("upload/system"),
    DETAIL("upload/detail"),
    ERROR("upload/error");

    private final String path;

    UploadDirectory(String path) {
        this.path = path;
    }

    public Path getPath() {
        return Paths.get(path);
    }
}