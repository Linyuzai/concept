package com.github.linyuzai.plugin.core.handle;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.property.BooleanValueMetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;

public interface PluginHandler {

    void handle(PluginContext context);

    interface PropertyKeys {

        MetadataProperty<Boolean> ENABLED = new BooleanValueMetadataProperty("handler.enabled");
    }

    interface Dependency {

        /**
         * 获得依赖的处理器类。
         *
         * @return 依赖的处理器类
         */
        @SuppressWarnings("unchecked")
        default Class<? extends PluginHandler>[] getDependencies() {
            HandlerDependency annotation = ReflectionUtils.findAnnotation(getClass(), HandlerDependency.class);
            if (annotation == null) {
                return new Class[0];
            }
            return annotation.value();
        }
    }
}
