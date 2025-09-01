package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认通过 {@link ConcurrentHashMap} 存储插件
 */
public class DefaultPluginRepository extends AbstractPluginRepository {

    @Override
    protected Map<String, Plugin> createMap() {
        return new ConcurrentHashMap<>();
    }
}
