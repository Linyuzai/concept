package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedPluginRepository extends AbstractPluginRepository {

    @Override
    protected Map<Object, Plugin> createMap() {
        return new LinkedHashMap<>();
    }
}
