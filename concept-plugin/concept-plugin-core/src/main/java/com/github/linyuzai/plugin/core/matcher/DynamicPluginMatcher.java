package com.github.linyuzai.plugin.core.matcher;

import com.github.linyuzai.plugin.core.context.PluginContext;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DynamicPluginMatcher extends AbstractPluginMatcher<Map<Method, Object[]>> {

    protected final Map<Method, Map<Integer, Type>> map = new LinkedHashMap<>();

    protected final Object target;

    protected final List<GenericTypePluginMatcher<?>> matchers = new ArrayList<>();

    public DynamicPluginMatcher(@NonNull Object target) {
        this.target = target;
        Class<?> clazz = this.target.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnPluginMatched.class)) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    Type[] types = method.getGenericParameterTypes();
                    for (int i = 0; i < types.length; i++) {
                        map.computeIfAbsent(method, m -> new LinkedHashMap<>())
                                .put(i, types[i]);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    @Override
    public boolean tryMatch(PluginContext context) {
        Map<Method, Object[]> valuesMap = new LinkedHashMap<>();
        for (Map.Entry<Method, Map<Integer, Type>> entry : map.entrySet()) {
            Map<Integer, Type> typeMap = entry.getValue();
            Object[] values = new Object[typeMap.size()];
            boolean matched = false;
            for (Map.Entry<Integer, Type> typeEntry : typeMap.entrySet()) {
                Type type = typeEntry.getValue();
                for (GenericTypePluginMatcher<?> matcher : matchers) {
                    if (matcher.tryMatch(context, type)) {
                        Object o = matcher.getMatched(context);
                        values[typeEntry.getKey()] = o;
                        matched = true;
                        break;
                    }
                }
            }
            if (matched) {
                valuesMap.put(entry.getKey(), values);
            }
        }

        if (valuesMap.isEmpty()) {
            return false;
        }
        context.set(this, valuesMap);
        return true;
    }

    @SneakyThrows
    @Override
    public void onMatched(Map<Method, Object[]> plugins) {
        for (Map.Entry<Method, Object[]> entry : plugins.entrySet()) {
            entry.getKey().invoke(target, entry.getValue());
        }
    }
}
