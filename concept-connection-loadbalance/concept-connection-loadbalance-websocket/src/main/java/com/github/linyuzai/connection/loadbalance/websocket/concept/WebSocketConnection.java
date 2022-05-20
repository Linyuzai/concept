package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class WebSocketConnection extends AbstractConnection {

    private volatile Map<String, String> queryParameterMap;

    private final Object parseQueryParameterMapLock = new Object();

    public WebSocketConnection(String type) {
        super(type);
    }

    public WebSocketConnection(String type, Map<Object, Object> metadata) {
        super(type, metadata);
    }

    public abstract URI getUri();

    public abstract boolean isOpen();

    @Override
    public void close(String reason) {
        close(1000, reason);
    }

    public String getQueryParameter(String name) {
        checkQueryParameterMap();
        return queryParameterMap.get(name);
    }

    public Map<String, String> getQueryParameterMap() {
        checkQueryParameterMap();
        return queryParameterMap;
    }

    public void checkQueryParameterMap() {
        if (queryParameterMap == null) {
            synchronized (parseQueryParameterMapLock) {
                if (queryParameterMap == null) {
                    queryParameterMap = new LinkedHashMap<>();
                    parseQueryParameterMap(queryParameterMap);
                }
            }
        }
    }

    public void parseQueryParameterMap(Map<String, String> map) {
        URI uri = getUri();
        if (uri == null) {
            return;
        }
        String query = uri.getQuery();
        if (query == null) {
            return;
        }
        String[] split = query.split("&");
        for (String s : split) {
            String[] kv = s.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
    }
}
