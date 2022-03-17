package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.util.TypeMetadata;
import lombok.AllArgsConstructor;

import java.lang.reflect.Type;

@AllArgsConstructor
public class ClassObjectMatcher extends ClassMatcher<Object> {

    private final Class<?> clazz;

    /**
     * Class
     *
     * @param metadata
     * @param type
     * @return
     */
    @Override
    public boolean support(TypeMetadata metadata, Type type) {
        if (metadata.isObject()) {
            Type t = metadata.getType();
            if (t instanceof Class) {

            }
        }
        return false;
    }

    @Override
    public boolean isMatched(PluginContext context) {
        return false;
    }
}
