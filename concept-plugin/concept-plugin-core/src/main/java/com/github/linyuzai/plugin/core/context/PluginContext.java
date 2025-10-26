package com.github.linyuzai.plugin.core.context;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;

/**
 * 插件上下文，用于保存在插件解析过程中的中间数据
 */
public interface PluginContext {

    /**
     * 获得Concept
     */
    PluginConcept getConcept();

    /**
     * 获得插件对象
     */
    Plugin getPlugin();

    /**
     * 获得根上下文
     */
    PluginContext getRoot();

    /**
     * 获得父上下文
     */
    PluginContext getParent();

    /**
     * 创建子上下文
     */
    PluginContext createSubContext();

    /**
     * 通过 key 获得 value
     */
    <T> T get(Object key);

    /**
     * 设置一对 key value
     */
    void set(Object key, Object value);

    /**
     * key 是否存在
     */
    boolean contains(Object key);

    /**
     * 移除一对 key value
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
