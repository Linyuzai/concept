package com.github.linyuzai.plugin.core.extract;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.match.PluginContent;
import com.github.linyuzai.plugin.core.match.PluginProperties;
import com.github.linyuzai.plugin.core.resolve.PluginResolver;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynamicPluginExtractor implements PluginExtractor {

    protected final Map<Method, Map<Integer, Invoker>> methodInvokersMap = new LinkedHashMap<>();

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
                        Invoker invoker = getInvoker(parameters[i]);
                        if (invoker == null) {
                            throw new PluginException("Can not invoke " + parameters[i]);
                        }
                        methodInvokersMap.computeIfAbsent(method, m ->
                                new LinkedHashMap<>()).put(i, invoker);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        if (methodInvokersMap.isEmpty()) {
            throw new PluginException("No method has @OnPluginExtract");
        }
    }

    public Invoker getInvoker(Parameter parameter) {
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            if (hasExplicitAnnotation(annotation)) {
                return getExplicitInvoker(annotation, parameter);
            }
        }
        Invoker pluginContextInvoker = getPluginContextInvoker(parameter);
        if (pluginContextInvoker != null) {
            return pluginContextInvoker;
        }
        Invoker pluginObjectInvoker = getPluginObjectInvoker(parameter);
        if (pluginObjectInvoker != null) {
            return pluginObjectInvoker;
        }
        Invoker propertiesInvoker = getPropertiesInvoker(parameter);
        if (propertiesInvoker != null) {
            return propertiesInvoker;
        }
        Invoker contentInvoker = getContentInvoker(parameter, null);
        if (contentInvoker != null) {
            return contentInvoker;
        }
        return null;
    }

    public boolean hasExplicitAnnotation(Annotation annotation) {
        return annotation.annotationType() == PluginProperties.class ||
                annotation.annotationType() == PluginContent.class;
    }

    public Invoker getExplicitInvoker(Annotation annotation, Parameter parameter) {
        if (annotation.annotationType() == PluginProperties.class) {
            return getPropertiesInvoker(parameter);
        }
        if (annotation.annotationType() == PluginContent.class) {
            String charset = ((PluginContent) annotation).charset();
            return getContentInvoker(parameter, charset.isEmpty() ? null : Charset.forName(charset));
        }
        throw new PluginException(annotation + " has no explicit invoker");
    }

    public Invoker getPluginContextInvoker(Parameter parameter) {
        try {
            return new PluginContextExtractor<PluginContext>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(PluginContext plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    public Invoker getPluginObjectInvoker(Parameter parameter) {
        try {
            return new PluginObjectExtractor<Plugin>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Plugin plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    public Invoker getPropertiesInvoker(Parameter parameter) {
        try {
            return new PropertiesExtractor<Void>() {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Void plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    public Invoker getContentInvoker(Parameter parameter, Charset charset) {
        try {
            return new ContentExtractor<Void>(charset) {

                @Override
                public Type getGenericType() {
                    return parameter.getParameterizedType();
                }

                @Override
                public Annotation[] getAnnotations() {
                    return parameter.getAnnotations();
                }

                @Override
                public void onExtract(Void plugin) {

                }
            }.getInvoker();
        } catch (Throwable e) {
            return null;
        }
    }

    @SneakyThrows
    @Override
    public void extract(PluginContext context) {
        for (Map.Entry<Method, Map<Integer, Invoker>> entry : methodInvokersMap.entrySet()) {
            Map<Integer, Invoker> matcherMap = entry.getValue();
            Object[] values = new Object[matcherMap.size()];
            boolean matched = false;
            for (Map.Entry<Integer, Invoker> methodEntry : matcherMap.entrySet()) {
                Invoker invoker = methodEntry.getValue();
                Object invoke = invoker.invoke(context);
                if (invoke == null) {
                    continue;
                }
                values[methodEntry.getKey()] = invoke;
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
        return methodInvokersMap.values().stream()
                .flatMap(it -> it.values().stream())
                .flatMap(it -> Arrays.stream(it.getMatcher().dependencies()))
                .distinct()
                .toArray(Class[]::new);
    }
}
