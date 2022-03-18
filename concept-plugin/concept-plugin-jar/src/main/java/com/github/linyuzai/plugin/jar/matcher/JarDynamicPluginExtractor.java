package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.extractor.DynamicPluginExtractor;
import com.github.linyuzai.plugin.core.matcher.PluginMatcher;
import com.github.linyuzai.plugin.jar.extractor.ClassExtractor;
import lombok.NonNull;

import java.lang.reflect.Parameter;
import java.lang.reflect.Type;

public class JarDynamicPluginExtractor extends DynamicPluginExtractor {

    public JarDynamicPluginExtractor(@NonNull Object target) {
        super(target);
    }

    @Override
    public PluginMatcher getMatcher(Parameter parameter) {
        PluginMatcher matcher = super.getMatcher(parameter);
        if (matcher != null) {
            return matcher;
        }
        PluginMatcher classMatcher = getClassMatcher(parameter);
        if (classMatcher != null) {
            return classMatcher;
        }
        return null;
    }

    public PluginMatcher getClassMatcher(Parameter parameter) {
        return new ClassExtractor<Object>() {

            @Override
            public void match(Type type) {
                matcher = getMatcher(parameter.getParameterizedType());
            }

            @Override
            public void onExtract(Object plugin) {

            }
        }.getMatcher();
    }
}
