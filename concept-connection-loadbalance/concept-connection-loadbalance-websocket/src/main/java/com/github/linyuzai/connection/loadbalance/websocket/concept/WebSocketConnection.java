package com.github.linyuzai.connection.loadbalance.websocket.concept;

import com.github.linyuzai.connection.loadbalance.core.concept.AbstractConnection;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ws 连接
 */
public abstract class WebSocketConnection extends AbstractConnection {

    /**
     * 连接参数
     */
    private volatile Map<String, String> queryParameterMap;

    private final Object parseQueryParameterMapLock = new Object();

    public abstract URI getUri();

    public abstract boolean isOpen();

    @Override
    public boolean isClosed() {
        return super.isClosed() || !isOpen();
    }

    public abstract Object getCloseReason(int code, String reason);

    public void close(int code, String reason) {
        close(getCloseReason(code, reason));
    }

    @Override
    public void close() {
        close(getCloseReason(1000, null));
    }

    public String getQueryParameter(String name) {
        checkQueryParameterMap();
        return queryParameterMap.get(name);
    }

    public Map<String, String> getQueryParameterMap() {
        checkQueryParameterMap();
        return queryParameterMap;
    }

    protected void checkQueryParameterMap() {
        if (queryParameterMap == null) {
            synchronized (parseQueryParameterMapLock) {
                if (queryParameterMap == null) {
                    queryParameterMap = new LinkedHashMap<>();
                    parseQueryParameterMap(queryParameterMap);
                }
            }
        }
    }

    protected void parseQueryParameterMap(Map<String, String> map) {
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
