package com.github.linyuzai.plugin.core.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.listener.PluginListener;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

/**
 * 插件
 */
public interface Plugin {

    /**
     * 获得插件定义
     */
    PluginDefinition getDefinition();

    /**
     * 设置插件定义
     */
    void setDefinition(PluginDefinition definition);

    /**
     * 获得插件配置
     */
    PluginMetadata getMetadata();

    /**
     * 设置插件配置
     */
    void setMetadata(PluginMetadata metadata);

    /**
     * 获得Concept
     */
    PluginConcept getConcept();

    /**
     * 设置Concept
     */
    void setConcept(PluginConcept concept);

    /**
     * 添加监听
     */
    void addListener(PluginListener listener);

    /**
     * 移除监听
     */
    void removeListener(PluginListener listener);

    /**
     * 卸载
     */
    void unload();

    /**
     * 加载
     */
    void load(PluginContext context);

    /**
     * 是否已加载
     */
    boolean isLoaded();

    /**
     * 标准配置
     */
    @Data
    class StandardMetadata {

        /**
         * 插件名
         */
        private String name;

        /**
         * 处理器元数据
         */
        private HandlerMetadata handler = new HandlerMetadata();

        /**
         * 依赖元数据
         */
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
    interface Entry extends PluginDefinition {

        /**
         * 获得插件
         */
        Plugin getParent();

        /**
         * 获得条目名称
         */
        String getName();

        /**
         * 获得内容（文件夹为null）
         */
        @Nullable
        Content getContent();

        /**
         * 获得输入流（文件夹为null）
         */
        @Nullable
        @Override
        default InputStream getInputStream() {
            Content content = getContent();
            if (content == null) {
                return null;
            }
            return content.getInputStream();
        }
    }

    /**
     * 插件内容
     */
    interface Content {

        /**
         * 获得输入流
         */
        InputStream getInputStream();
    }
}
