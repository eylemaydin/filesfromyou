package com.filesfromyou.logprocessor.rest.impl;

import com.filesfromyou.logprocessor.api.ClientApiDelegate;
import com.filesfromyou.logprocessor.models.Response;
import com.filesfromyou.logprocessor.service.FileManagementService;
import com.filesfromyou.logprocessor.service.UploadDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ClientApiImpl implements ClientApiDelegate {

    Logger log = LoggerFactory.getLogger(ClientApiImpl.class);

    @Autowired
    FileManagementService fileManagementService;

    @Override
    public ResponseEntity<Response> uploadSystemFileByClientId(MultipartFile file, Integer clientId) {
        log.info("REST request to upload system file");
        try {
            fileManagementService.save(file, UploadDirectory.SYSTEM);
            return successfulResponse();
        } catch (Exception e) {
            return unsuccessfulResponse();
        }
    }

    @Override
    public ResponseEntity<Response> uploadLogFileBySystemId(MultipartFile file, Integer clientId, Integer systemId) {
        log.info("REST request to upload log file");
        try {
            fileManagementService.save(file, UploadDirectory.DETAIL);
            return successfulResponse();
        } catch (Exception e) {
            return unsuccessfulResponse();
        }
    }

    private ResponseEntity<Response> successfulResponse() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        buildResponse(true, "Uploaded the file successfully!")
                );
    }

    private ResponseEntity<Response> unsuccessfulResponse() {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(
                        buildResponse(false, "Could not upload the file!")
                );
    }

    private Response buildResponse(boolean success, String message) {
        Response response = new Response();
        response.setSuccess(success);
        response.setMessage(message);
        return response;
    }
}
