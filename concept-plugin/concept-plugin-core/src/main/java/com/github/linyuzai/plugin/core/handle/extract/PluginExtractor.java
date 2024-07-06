package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandler;

/**
 * 插件提取器
 */
public interface PluginExtractor extends PluginHandler, PluginHandler.Dependency {

    @Override
    default void handle(PluginContext context) {
        extract(context);
    }

    /**
     * 提取插件
     *
     * @param context 上下文 {@link PluginContext}
     */
    void extract(PluginContext context);

    /**
     * 插件提取执行器
     */
    interface Invoker extends Dependency {

        /**
         * 执行插件提取。
         * 包括匹配，转换，格式化三个步骤。
         *
         * @param context 上下文 {@link PluginContext}
         * @return 插件对象
         */
        Object invoke(PluginContext context);
    }
}
