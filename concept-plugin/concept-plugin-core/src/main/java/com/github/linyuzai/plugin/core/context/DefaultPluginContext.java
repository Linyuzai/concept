package com.github.linyuzai.plugin.core.context;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 插件上下文默认实现
 * <p>
 * 通过 {@link LinkedHashMap} 缓存数据
 */
public class DefaultPluginContext extends AbstractPluginContext {

    private final Map<Object, Object> ctx = new LinkedHashMap<>();

    @Override
    public PluginContext createSubContext() {
        DefaultPluginContext context = new DefaultPluginContext();
        context.setParent(this);
        return context;
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
