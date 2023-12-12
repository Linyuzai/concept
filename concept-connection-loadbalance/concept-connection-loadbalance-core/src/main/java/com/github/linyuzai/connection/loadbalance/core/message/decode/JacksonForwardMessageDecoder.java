package com.github.linyuzai.connection.loadbalance.core.message.decode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.core.concept.ConnectionLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.core.message.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

/**
 * 通过 jackson 来解析 json。
 * <p>
 * Parse json by jackson.
 */
@Getter
@RequiredArgsConstructor
public class JacksonForwardMessageDecoder implements MessageDecoder {

    private final ObjectMapper objectMapper;

    public JacksonForwardMessageDecoder() {
        this(new ObjectMapper());
    }

    @SneakyThrows
    @Override
    public Message decode(Object message, Connection connection, ConnectionLoadBalanceConcept concept) {
        JsonNode node = readTree(message);
        JsonNode headersNode = node.get("headers");
        String headersJson = headersNode.toString();
        LinkedHashMap<String, String> headers = objectMapper.readValue(headersJson,
                new TypeReference<LinkedHashMap<String, String>>() {
                });
        String binary = headers.getOrDefault(Message.BINARY, Boolean.FALSE.toString());
        JsonNode payloadNode = node.get("payload");
        if (Boolean.parseBoolean(binary)) {
            BinaryMessage decoded = new BinaryMessage();
            decoded.setHeaders(headers);
            decoded.setPayload(payloadNode.binaryValue());
            return decoded;
        } else {
            TextMessage decoded = new TextMessage();
            decoded.setHeaders(headers);
            decoded.setPayload(payloadNode.toString());
            return decoded;
        }
    }

    @SneakyThrows
    protected JsonNode readTree(Object message) {
        if (message instanceof String) {
            return objectMapper.readTree((String) message);
        } else if (message instanceof ByteBuffer) {
            return objectMapper.readTree(((ByteBuffer) message).array());
        } else if (message instanceof byte[]) {
            return objectMapper.readTree((byte[]) message);
        } else {
            throw new MessageDecodeException(message);
        }
    }
}
