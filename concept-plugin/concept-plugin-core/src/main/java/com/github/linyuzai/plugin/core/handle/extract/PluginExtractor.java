package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.format.PluginFormatter;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    @Getter
    @RequiredArgsConstructor
    class Invoker {

        /**
         * 插件匹配器
         */
        private final PluginMatcher matcher;

        /**
         * 插件转换器
         */
        private final PluginConvertor convertor;

        /**
         * 插件格式器
         */
        private final PluginFormatter formatter;

        /**
         * 执行插件提取。
         * 包括匹配，转换，格式化三个步骤。
         *
         * @param context 上下文 {@link PluginContext}
         * @return 插件对象
         */
        public Object invoke(PluginContext context) {
            //匹配插件
            Object matched = matcher.match(context);
            if (matched == null) {
                return null;
            }
            //转换插件
            Object converted = convertor == null ? matched : convertor.convert(matched, context);
            if (converted == null) {
                return null;
            }
            //格式化插件
            return formatter == null ? converted : formatter.format(converted, context);
        }
    }
}
