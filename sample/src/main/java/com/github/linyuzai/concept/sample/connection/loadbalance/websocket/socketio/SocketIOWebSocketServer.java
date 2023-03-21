/*package com.github.linyuzai.concept.sample.connection.loadbalance.websocket.socketio;

import io.socket.engineio.server.Emitter;
import io.socket.engineio.server.EngineIoServer;
import io.socket.socketio.server.SocketIoServer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/websocket/*")
public class SocketIOWebSocketServer extends HttpServlet {

    private final EngineIoServer mEngineIoServer = new EngineIoServer();
    private final SocketIoServer mSocketIoServer = new SocketIoServer(mEngineIoServer);

    public SocketIOWebSocketServer() {
        mSocketIoServer.namespace("/websocket/").on("connection", new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mEngineIoServer.handleRequest(request, response);
        //mSocketIoServer.namespace("/websocket/*").broadcast();
    }
}*/
