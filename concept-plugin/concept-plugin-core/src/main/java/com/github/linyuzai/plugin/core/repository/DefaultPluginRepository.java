package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultPluginRepository extends AbstractPluginRepository {

    @Override
    protected Map<Object, Plugin> createMap() {
        return new ConcurrentHashMap<>();
    }
}