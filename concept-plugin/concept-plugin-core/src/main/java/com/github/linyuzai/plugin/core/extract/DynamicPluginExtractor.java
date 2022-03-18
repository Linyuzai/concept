package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.match.PluginMatcher;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynamicPluginExtractor implements PluginExtractor {

    protected final Map<Method, Map<Integer, PluginMatcher>> methodPluginMatchersMap = new LinkedHashMap<>();

    protected final Object target;

    public DynamicPluginExtractor(@NonNull Object target) {
        this.target = target;
        Class<?> clazz = this.target.getClass();
        while (clazz != null) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnPluginExtract.class)) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    Parameter[] parameters = method.getParameters();
                    for (int i = 0; i < parameters.length; i++) {
                        PluginMatcher matcher = getMatcher(parameters[i]);
                        if (matcher == null) {
                            throw new PluginException("Can not match " + parameters[i]);
                        }
                        methodPluginMatchersMap.computeIfAbsent(method, m ->
                                new LinkedHashMap<>()).put(i, matcher);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    public PluginMatcher getMatcher(Parameter parameter) {
        PluginMatcher pluginContextMatcher = getPluginContextMatcher(parameter);
        if (pluginContextMatcher != null) {
            return pluginContextMatcher;
        }
        PluginMatcher pluginObjectMatcher = getPluginObjectMatcher(parameter);
        if (pluginObjectMatcher != null) {
            return pluginObjectMatcher;
        }
        return null;
    }

    public PluginMatcher getPluginContextMatcher(Parameter parameter) {
        return new PluginContextExtractor<PluginContext>() {

            @Override
            public void match(Type type) {
                matcher = getMatcher(parameter.getParameterizedType());
            }

            @Override
            public void onExtract(PluginContext plugin) {

            }
        }.getMatcher();
    }

    public PluginMatcher getPluginObjectMatcher(Parameter parameter) {
        return new PluginObjectExtractor<Plugin>() {

            @Override
            public void match(Type type) {
                matcher = getMatcher(parameter.getParameterizedType());
            }

            @Override
            public void onExtract(Plugin plugin) {

            }
        }.getMatcher();
    }

    @SneakyThrows
    @Override
    public void extract(PluginContext context) {
        for (Map.Entry<Method, Map<Integer, PluginMatcher>> entry : methodPluginMatchersMap.entrySet()) {
            Map<Integer, PluginMatcher> matcherMap = entry.getValue();
            Object[] values = new Object[matcherMap.size()];
            boolean matched = false;
            for (Map.Entry<Integer, PluginMatcher> typeEntry : matcherMap.entrySet()) {
                PluginMatcher matcher = typeEntry.getValue();
                Object match = matcher.match(context);
                if (match == null) {
                    continue;
                }
                values[typeEntry.getKey()] = match;
                matched = true;
            }
            if (matched) {
                entry.getKey().invoke(target, values);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends PluginResolver>[] dependencies() {
        return methodPluginMatchersMap.values().stream()
                .flatMap(it -> it.values().stream())
                .flatMap(it -> Arrays.stream(it.dependencies()))
                .distinct()
                .toArray(Class[]::new);
    }
}
