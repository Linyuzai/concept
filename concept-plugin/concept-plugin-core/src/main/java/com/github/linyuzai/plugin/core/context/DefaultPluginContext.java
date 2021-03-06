package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import lombok.Getter;
import lombok.NonNull;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link PluginContext} 默认实现。
 * 通过 {@link LinkedHashMap} 缓存数据。
 */
@SuppressWarnings("unchecked")
public class DefaultPluginContext implements PluginContext {

    private final PluginConcept concept;

    private final Plugin plugin;

    @Getter
    private final Map<Object, Object> content = new LinkedHashMap<>();

    public DefaultPluginContext(@NonNull PluginConcept concept, @NonNull Plugin plugin) {
        this.concept = concept;
        this.plugin = plugin;
    }

    @Override
    public <C extends PluginConcept> C getPluginConcept() {
        return (C) concept;
    }

    @Override
    public <P extends Plugin> P getPlugin() {
        return (P) plugin;
    }

    /**
     * 发布事件。
     * 尝试获得 {@link PluginEventPublisher} 并发布事件。
     *
     * @param event 事件
     */
    @Override
    public void publish(Object event) {
        PluginEventPublisher publisher = get(PluginEventPublisher.class);
        if (publisher != null) {
            publisher.publish(event);
        }
    }

    @Override
    public <T> T get(Object key) {
        return (T) content.get(key);
    }

    @Override
    public void set(Object key, Object value) {
        content.put(key, value);
    }

    @Override
    public boolean contains(Object key) {
        return content.containsKey(key);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        content.clear();
    }
}
