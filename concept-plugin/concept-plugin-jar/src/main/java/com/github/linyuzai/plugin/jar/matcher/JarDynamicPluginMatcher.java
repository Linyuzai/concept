package com.github.linyuzai.plugin.jar.matcher;

import com.github.linyuzai.plugin.core.matcher.DynamicPluginMatcher;
import lombok.NonNull;

public class JarDynamicPluginMatcher extends DynamicPluginMatcher {

    public JarDynamicPluginMatcher(@NonNull Object target) {
        super(target);
        matchers.add(new ClassMatcher<Object>() {
            @Override
            public void onMatched(Class<?> plugin) {
            }
        });
        matchers.add(new InstanceMatcher<Object>() {
            @Override
            public void onMatched(Object plugin) {
            }
        });
        matchers.add(new PropertiesMatcher<Object>() {
            @Override
            public void onMatched(Object plugin) {
            }
        });
    }
}
