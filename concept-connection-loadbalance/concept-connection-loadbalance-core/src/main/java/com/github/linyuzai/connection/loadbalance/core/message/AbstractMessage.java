package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 消息抽象类。
 * <p>
 * Abstract message.
 */
@Getter
@Setter
public abstract class AbstractMessage<T> implements Message {

    private Map<String, String> headers = new LinkedHashMap<>();

    private T payload;
}
