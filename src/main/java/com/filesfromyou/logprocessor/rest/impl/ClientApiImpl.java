package com.filesfromyou.logprocessor.rest.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.filesfromyou.logprocessor.api.ClientApiDelegate;
import com.filesfromyou.logprocessor.exception.InvalidClientException;
import com.filesfromyou.logprocessor.models.Response;
import com.filesfromyou.logprocessor.models.SystemInfo;
import com.filesfromyou.logprocessor.service.filemanager.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import static com.filesfromyou.logprocessor.utils.Utility.*;


@Service
public class ClientApiImpl implements ClientApiDelegate {

    Logger log = LoggerFactory.getLogger(ClientApiImpl.class);

    @Autowired
    FileManagementService fileManagementService;

    @Override
    public ResponseEntity<Response> saveSystemInformation(SystemInfo body, Integer clientId) {
        log.info("REST request to upload system file");
        validateClient(clientId);
        try {
            JsonNode json = convertToJson(body);
            String systemInformation = json.toString();
            String fileName = (json.get("clientId").textValue() + json.get("id").textValue()).hashCode() + ".log";
            fileManagementService.save(systemInformation, fileName, UploadDirectory.SYSTEM);
            return successfulResponse("Saved system log successfully!");
        } catch (Exception e) {
            return unsuccessfulResponse("Could not save the system log!");
        }
    }

    @Override
    public ResponseEntity<Response> uploadLogFile(MultipartFile file, Integer clientId) {
        log.info("REST request to upload log file");
        validateClient(clientId);
        try {
            fileManagementService.save(file, UploadDirectory.DETAIL);
            return successfulResponse("Uploaded the file successfully!");
        } catch (Exception e) {
            return unsuccessfulResponse("Could not upload the file!");
        }
    }

    private void validateClient(Integer clientId) throws InvalidClientException {
        if (!isClientValid(clientId)) {
            throw new InvalidClientException();
        }
    }

    private boolean isClientValid(Integer clientId) {
        //Check client if it is valid or not
        return true;
    }
}
