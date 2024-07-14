package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandlerChain;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Set;

/**
 * 插件
 */
public interface Plugin {

    /**
     * 插件唯一标识
     */
    Object getId();

    /**
     * 获得初始对象
     */
    Object getSource();

    /**
     * 设置初始对象
     */
    void setSource(Object source);

    /**
     * 获得插件配置
     */
    PluginMetadata getMetadata();

    /**
     * 设置插件配置
     */
    void setMetadata(PluginMetadata metadata);

    PluginConcept getConcept();

    void setConcept(PluginConcept concept);

    /**
     * 添加加载监听
     */
    void addLoadListener(LoadListener listener);

    /**
     * 移除加载监听
     */
    void removeLoadListener(LoadListener listener);

    /**
     * 添加销毁监听
     */
    void addDestroyListener(DestroyListener listener);

    /**
     * 移除销毁监听
     */
    void removeDestroyListener(DestroyListener listener);

    /**
     * 初始化
     */
    void initialize();

    /**
     * 销毁
     */
    void destroy();

    /**
     * 准备
     */
    void prepare(PluginContext context);

    /**
     * 加载
     */
    void load(PluginHandlerChain chain, PluginContext context);

    /**
     * 标准配置
     */
    @Data
    class StandardMetadata {

        /**
         * 插件名
         */
        private String name;

        private HandlerMetadata handler = new HandlerMetadata();

        private DependencyMetadata dependency = new DependencyMetadata();

        /**
         * 插件处理器配置
         */
        @Data
        public static class HandlerMetadata {

            /**
             * 是否启用插件处理器解析插件
             */
            private boolean enabled = true;
        }

        /**
         * 依赖配置
         */
        @Data
        public static class DependencyMetadata {

            /**
             * 依赖的插件名称
             */
            private Set<String> names = Collections.emptySet();
        }
    }

    /**
     * 插件条目
     */
    interface Entry {

        /**
         * 唯一标识
         */
        Object getId();

        /**
         * 获得条目名称
         */
        String getName();

        /**
         * 获得插件
         */
        Plugin getPlugin();

        /**
         * 获得内容
         */
        Content getContent();
    }

    /**
     * 插件内容
     */
    interface Content {

        /**
         * 获得数据流
         */
        InputStream getInputStream() throws IOException;
    }

    /**
     * 加载监听器
     */
    interface LoadListener {

        /**
         * 插件加载
         */
        void onLoad(Plugin plugin);
    }

    /**
     * 销毁监听器
     */
    interface DestroyListener {

        /**
         * 插件销毁
         */
        void onDestroy(Plugin plugin);
    }
}
