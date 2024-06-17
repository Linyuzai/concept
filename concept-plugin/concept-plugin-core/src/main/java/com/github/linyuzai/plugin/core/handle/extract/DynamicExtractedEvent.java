package com.github.linyuzai.plugin.core.handle.extract;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.Getter;

import java.lang.reflect.Method;

/**
 * 动态插件提取事件
 */
@Getter
public class DynamicExtractedEvent extends PluginExtractedEvent {

    /**
     * 回调方法
     */
    private final Method method;

    /**
     * 方法执行对象
     */
    private final Object object;

    public DynamicExtractedEvent(PluginContext context,
                                 PluginExtractor extractor,
                                 Object extracted,
                                 Method method,
                                 Object object) {
        super(context, extractor, extracted);
        this.method = method;
        this.object = object;
    }
}
