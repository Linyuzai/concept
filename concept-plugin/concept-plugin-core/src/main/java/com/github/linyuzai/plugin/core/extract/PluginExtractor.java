package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolverDependency;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 插件提取器
 */
public interface PluginExtractor extends PluginResolverDependency {

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
    @AllArgsConstructor
    class Invoker {

        /**
         * 插件匹配器
         */
        private PluginMatcher matcher;

        /**
         * 插件转换器
         */
        private PluginConvertor convertor;

        /**
         * 插件格式器
         */
        private PluginFormatter formatter;

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
