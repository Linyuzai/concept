package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.event.PluginEventPublisher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link PluginContext} 默认实现。
 * 通过 {@link LinkedHashMap} 缓存数据。
 */
@RequiredArgsConstructor
public class DefaultPluginContext implements PluginContext {

    @Getter
    private final PluginContext parent;

    private final Map<Object, Object> map = new LinkedHashMap<>();

    @Override
    public PluginContext createSubContext(boolean inherit) {
        DefaultPluginContext context = new DefaultPluginContext(this);
        if (inherit) {
            context.map.putAll(this.map);
        }
        return context;
    }

    /**
     * 发布事件。
     *
     * @param event 事件
     */
    @Override
    public void publish(Object event) {
        getConcept().getEventPublisher().publish(event);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key) {
        return (T) map.get(key);
    }

    @Override
    public void set(Object key, Object value) {
        map.put(key, value);
    }

    @Override
    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    @Override
    public void remove(Object key) {
        map.remove(key);
    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {
        map.clear();
    }
}
