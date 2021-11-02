package com.filesfromyou.logprocessor.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.filesfromyou.logprocessor.models.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Utility {

    private static Response buildResponse(boolean success, String message) {
        Response response = new Response();
        response.setSuccess(success);
        response.setMessage(message);
        return response;
    }

    public static ResponseEntity<Response> successfulResponse(String message) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        buildResponse(true, message)
                );
    }

    public static ResponseEntity<Response> unsuccessfulResponse(String message) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(
                        buildResponse(false, message)
                );
    }

    public static JsonNode convertToJson(Object dto) {
        JsonNode node = new ObjectMapper().valueToTree(dto);
        ObjectNode object = (ObjectNode) node;
        object.set("@timestamp", node.get("timestamp"));
        object.remove("timestamp");
        return node;
    }
}
