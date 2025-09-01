package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 通过 {@link LinkedHashMap} 存储插件
 */
public class LinkedPluginRepository extends AbstractPluginRepository {

    @Override
    protected Map<String, Plugin> createMap() {
        return new LinkedHashMap<>();
    }
}
