package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * 插件上下文。
 * 用于保存在插件解析过程中的中间数据。
 */
public interface PluginContext {

    /**
     * 获得 {@link PluginConcept}
     *
     * @return {@link PluginConcept}
     */
    default PluginConcept getConcept() {
        return getPlugin().getConcept();
    }

    /**
     * 获得插件 {@link Plugin}
     *
     * @return {@link Plugin}
     */
    default Plugin getPlugin() {
        return get(Plugin.class);
    }

    default PluginContext getRoot() {
        PluginContext parent = getParent();
        if (parent == null) {
            return this;
        }
        return parent.getRoot();
    }

    PluginContext getParent();

    PluginContext createSubContext();

    void publish(Object event);

    /**
     * 通过 key 获得 value。
     *
     * @param key key
     * @param <T> value 类型
     * @return value 或 null
     */
    <T> T get(Object key);

    /**
     * 设置一对 key value
     *
     * @param key   key
     * @param value value
     */
    void set(Object key, Object value);

    /**
     * key 是否存在
     *
     * @param key key
     * @return 如果存在返回 true，否则返回 false
     */
    boolean contains(Object key);

    /**
     * 通过键移除一个键值对。
     *
     * @param key 键
     */
    void remove(Object key);

    /**
     * 初始化
     */
    void initialize();

    /**
     * 销毁
     */
    void destroy();
}
