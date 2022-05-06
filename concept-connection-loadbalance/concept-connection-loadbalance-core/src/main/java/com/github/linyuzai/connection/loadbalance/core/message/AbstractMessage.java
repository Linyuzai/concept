package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractMessage<T> implements Message {

    private Map<String, String> headers = new LinkedHashMap<>();

    private T payload;
}
