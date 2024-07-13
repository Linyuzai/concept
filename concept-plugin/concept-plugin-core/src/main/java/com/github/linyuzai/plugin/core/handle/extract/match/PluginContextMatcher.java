package com.github.linyuzai.plugin.core.handle.extract.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.*;

/**
 * 插件上下文匹配器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PluginContextMatcher implements PluginMatcher {

    /**
     * 上下文类型
     */
    private Class<?> contextClass = PluginContext.class;

    @Override
    public Object match(PluginContext context) {
        if (contextClass.isInstance(context)) {
            return context;
        }
        return null;
    }
}
