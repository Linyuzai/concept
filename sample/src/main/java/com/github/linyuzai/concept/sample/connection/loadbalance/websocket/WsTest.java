package com.github.linyuzai.concept.sample.connection.loadbalance.websocket;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.websocket.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class WsTest {

    private static final Map<String, String> idNoMap = new ConcurrentHashMap<>();

    private static final Map<String, List<String>> noMessageMap = new ConcurrentHashMap<>();

    public static void log() {
        for (Map.Entry<String, List<String>> entry : noMessageMap.entrySet()) {
            String no = idNoMap.getOrDefault(entry.getKey(), entry.getKey());
            log.info("{}:{}", no, entry.getValue());
        }
    }

    @SneakyThrows
    public static Session connect(WebSocketContainer container, String no) {
        URI uri = new URI("ws://localhost:9000/demo-application-any/api/concept-websocket/test?no=" + no);
        Session session = container.connectToServer(Client.class, uri);
        idNoMap.put(session.getId(), no);
        session.getBasicRemote().sendText(no);
        return session;
    }

    @ClientEndpoint
    public static class Client {

        @OnOpen
        public void onOpen(Session session) {
        }

        @OnClose
        public void onClose(Session session, CloseReason reason) {
            log.info("连接已关闭({}):{}", getTag(idNoMap.get(session.getId())), reason);
        }

        @OnMessage
        public void onMessage(Session session, String message) {
            noMessageMap.computeIfAbsent(session.getId(), no -> new CopyOnWriteArrayList<>())
                    .add(message);
            //log.info("接收到消息({}):{}", getTag(idNoMap.get(session.getId())), message);
        }

        /*@OnMessage
        public void onMessage(Session session, PongMessage message) {

        }*/

        /*@OnMessage
        public void onMessage(Session session, ByteBuffer message) {

        }*/

        @OnError
        public void onError(Session session, Throwable e) {
            log.error("连接异常({}):", getTag(idNoMap.get(session.getId())), e);
        }
    }

    /*public static void connect(int no) {
        try {
            URI uri = new URI("ws://localhost:9000/demo-application-any/api/concept-websocket/test?no=" + no);
            WebSocketClient client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    send("connect:" + no);
                }

                @Override
                public void onMessage(String message) {
                    log.info("接收到消息({}):{}", getTag(no), message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("连接已关闭({}):{},{}", getTag(no), code, reason);
                }

                @Override
                public void onError(Exception e) {
                    log.error("连接异常({}):", getTag(no), e);
                }
            };

            client.connect(); // 连接到WebSocket服务器
            // 在此处添加代码，处理你的业务逻辑
        } catch (Exception e) {
            log.error("error:{}", getTag(no), e);
        }
    }*/

    private static String getTag(String no) {
        return no;
    }
}
