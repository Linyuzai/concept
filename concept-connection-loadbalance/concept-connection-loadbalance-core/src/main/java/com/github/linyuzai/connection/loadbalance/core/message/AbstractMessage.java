package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息抽象类
 *
 * @param <T> 消息体类型
 */
@Getter
@Setter
public abstract class AbstractMessage<T> implements Message {

    private Map<String, String> headers = new LinkedHashMap<>();

    private T payload;

    @Override
    public boolean isBroadcast() {
        String broadcast = getHeaders().getOrDefault(BROADCAST, Boolean.TRUE.toString());
        return Boolean.parseBoolean(broadcast);
    }

    @Override
    public void setBroadcast(boolean broadcast) {
        getHeaders().put(BROADCAST, Boolean.valueOf(broadcast).toString());
    }

    @Override
    public boolean isForward() {
        return getHeaders().containsKey(FORWARD);
    }

    @Override
    public void setForward(boolean forward) {
        if (forward) {
            getHeaders().put(FORWARD, Boolean.TRUE.toString());
        } else {
            getHeaders().remove(FORWARD);
        }
    }

    @Override
    public String getId() {
        return getHeaders().get(ID);
    }

    @Override
    public void setId(String id) {
        getHeaders().put(ID, id);
    }

    @Override
    public String getFrom() {
        return getHeaders().get(FROM);
    }

    @Override
    public void setFrom(String from) {
        getHeaders().put(FORWARD, from);
    }
}
