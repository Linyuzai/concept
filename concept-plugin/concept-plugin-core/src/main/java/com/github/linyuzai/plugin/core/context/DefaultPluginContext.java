package com.github.linyuzai.plugin.core.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 插件上下文默认实现
 */
public class DefaultPluginContext extends AbstractPluginContext {

    private final Map<Object, Object> ctx = new ConcurrentHashMap<>();

    public DefaultPluginContext() {
        this(null);
    }

    public DefaultPluginContext(PluginContext parent) {
        super(parent);
    }

    @Override
    public PluginContext createSubContext() {
        return new DefaultPluginContext(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Object key) {
        return (T) ctx.get(key);
    }

    @Override
    public void set(Object key, Object value) {
        ctx.put(key, value);
    }

    @Override
    public boolean contains(Object key) {
        return ctx.containsKey(key);
    }

    @Override
    public void remove(Object key) {
        ctx.remove(key);
    }
}
