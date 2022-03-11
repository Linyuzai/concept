package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynamicPluginMatcher extends AbstractPluginMatcher<Object> {

    private final Map<Integer, Object> map = new LinkedHashMap<>();

    private final Object target;

    public DynamicPluginMatcher(@NonNull Object target) {
        this.target = target;
        Class<?> clazz = this.target.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnPluginMatched.class)) {
                    Type[] types = method.getGenericParameterTypes();
                    for (int i = 0; i < types.length; i++) {
                        adaptParameterType(types[i]);
                    }

                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    protected void adaptParameterType(Type type) {

    }

    @Override
    public boolean ifMatch(PluginContext context) {
        return false;
    }

    @Override
    public Object getMatchedPlugin(PluginContext context) {
        return null;
    }

    @Override
    public void onMatched(Object plugin) {

    }
}
