package com.github.linyuzai.plugin.core.repository;

import com.github.linyuzai.plugin.core.concept.Plugin;

import java.util.stream.Stream;

public interface PluginRepository {

    Plugin get(Object o);

    void add(Object o, Plugin plugin);

    Plugin remove(Object o);

    boolean contains(Object o);

    Stream<Plugin> stream();
}
