package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.format.PluginFormatter;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolverDependency;
import lombok.AllArgsConstructor;
import lombok.Getter;

public interface PluginExtractor extends PluginResolverDependency {

    void extract(PluginContext context);

    @Getter
    @AllArgsConstructor
    class Invoker {

        private PluginMatcher matcher;

        private PluginFormatter formatter;

        public Object invoke(PluginContext context) {
            Object match = matcher.match(context);
            if (match == null) {
                return null;
            }
            return formatter == null ? match : formatter.format(match);
        }
    }
}
