package com.filesfromyou.logprocessor.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

@Service
public class MapUtil {
    public JsonNode convertToJson(Object dto) {
        JsonNode node = new ObjectMapper().valueToTree(dto);
        ObjectNode object = (ObjectNode) node;
        object.set("@timestamp", node.get("timestamp"));
        object.remove("timestamp");
        return node;
    }
}
