package com.github.linyuzai.event.core.utils;

import com.github.linyuzai.event.core.config.PropertiesConfig;

import java.util.Map;

public class InheritUtils {

    public static <K, V> void inherit(Map<K, V> child, Map<K, V> parent) {
        for (Map.Entry<K, V> entry : parent.entrySet()) {
            K key = entry.getKey();
            if (child.containsKey(key)) {
                continue;
            }
            child.put(key, entry.getValue());
        }
    }

    public static void inherit(PropertiesConfig child, PropertiesConfig parent) {
        inherit(child.getMetadata(), parent.getMetadata());
        if (child.getEncoder() == null) {
            child.setEncoder(parent.getEncoder());
        }
        if (child.getDecoder() == null) {
            child.setDecoder(parent.getDecoder());
        }
        if (child.getErrorHandler() == null) {
            child.setErrorHandler(parent.getErrorHandler());
        }
        if (child.getPublisher() == null) {
            child.setPublisher(parent.getPublisher());
        }
        if (child.getSubscriber() == null) {
            child.setSubscriber(parent.getSubscriber());
        }
    }
}
