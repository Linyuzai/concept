package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.stream.Stream;

public interface PluginRepository {

    Plugin get(Object id);

    void add(Plugin plugin);

    Plugin remove(Object plugin);

    boolean contains(Object plugin);

    Stream<Plugin> stream();
}
