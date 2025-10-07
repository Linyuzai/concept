package com.github.linyuzai.plugin.autoconfigure.observable;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.listener.PluginListener;
import com.github.linyuzai.plugin.jar.handle.extract.match.PluginClassAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class RequestMappingPluginObservable extends BeanExtractor<Map<String, ?>>
        implements PluginObservable<String, Class<?>> {

    private final Map<String, Class<?>> requestMappingMap = new ConcurrentHashMap<>();

    @Override
    public Collection<String> keys() {
        return requestMappingMap.keySet();
    }

    @Override
    public Collection<Class<?>> values() {
        return requestMappingMap.values();
    }

    @Override
    public Class<?> get(String key) {
        return requestMappingMap.get(key);
    }

    @Override
    public List<Class<?>> list(String key) {
        Class<?> cls = requestMappingMap.get(key);
        if (cls == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(cls);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Class<?>> action) {
        requestMappingMap.forEach(action);
    }

    @Override
    public void onExtract(@PluginClassAnnotation({Controller.class, RestController.class})
                          Map<String, ?> requestMappings, PluginContext context) {
        List<Runnable> destroyList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : requestMappings.entrySet()) {
            String path = entry.getKey();
            Object requestMapping = entry.getValue();
            Class<?> type = requestMapping.getClass();
            doRegister(type, requestMapping, destroyList);
            requestMappingMap.put(path, type);
        }
        context.getPlugin().addListener(new PluginListener() {
            @Override
            public void onUnload(Plugin p) {
                requestMappingMap.keySet().removeAll(requestMappings.keySet());
                destroyList.forEach(Runnable::run);
            }
        });
    }

    protected abstract void doRegister(Class<?> type, Object requestMapping, List<Runnable> destroyList);
}
