package com.github.linyuzai.connection.loadbalance.core.message;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class AbstractMessage implements Message {

    private final Map<String, String> headers = new LinkedHashMap<>();
}
