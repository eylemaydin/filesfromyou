package com.filesfromyou.logprocessor.rest.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.filesfromyou.logprocessor.api.ClientApiDelegate;
import com.filesfromyou.logprocessor.models.Response;
import com.filesfromyou.logprocessor.models.SystemInfo;
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
    public ResponseEntity<Response> saveSystemInformation(SystemInfo body, Integer clientId) {
        log.info("REST request to upload system file");
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
        try {
            fileManagementService.save(file, UploadDirectory.DETAIL);
            return successfulResponse("Uploaded the file successfully!");
        } catch (Exception e) {
            return unsuccessfulResponse("Could not upload the file!");
        }
    }

    private ResponseEntity<Response> successfulResponse(String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        buildResponse(true, message)
                );
    }

    private ResponseEntity<Response> unsuccessfulResponse(String message) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(
                        buildResponse(false, message)
                );
    }

    private Response buildResponse(boolean success, String message) {
        Response response = new Response();
        response.setSuccess(success);
        response.setMessage(message);
        return response;
    }

    private JsonNode convertToJson(Object dto) {
        JsonNode node = new ObjectMapper().valueToTree(dto);
        ObjectNode object = (ObjectNode) node;
        object.set("@timestamp", node.get("timestamp"));
        object.remove("timestamp");
        return node;
    }
}
