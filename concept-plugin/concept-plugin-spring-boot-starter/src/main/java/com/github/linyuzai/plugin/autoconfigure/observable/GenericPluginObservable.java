package com.github.linyuzai.plugin.autoconfigure.observable;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import com.github.linyuzai.plugin.core.type.DefaultNestedType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public abstract class GenericPluginObservable<K, V> extends BeanExtractor<Map<String, V>> implements PluginObservable<K, V> {

    private final Map<K, List<Entry>> plugins = newMap();

    @Override
    public V get(K key) {
        List<Entry> entries = plugins.getOrDefault(key, Collections.emptyList());
        return entries.isEmpty() ? null : entries.get(0).value;
    }

    @Override
    public List<V> list(K key) {
        List<Entry> entries = plugins.getOrDefault(key, Collections.emptyList());
        return entries.stream().map(it -> it.value).collect(Collectors.toList());
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        plugins.forEach((key, value) ->
                value.forEach(entry ->
                        action.accept(key, entry.getValue())));
    }

    @Override
    public Invoker createInvoker(Type type, Annotation[] annotations) {
        return super.createInvoker(new DefaultNestedType(Map.class) {
            {
                children.add(new DefaultNestedType(String.class));
                children.add(new DefaultNestedType(getPluginType()));
            }
        }, annotations);
    }

    @Override
    public void onExtract(Map<String, V> plugin, PluginContext context) {
        plugin.forEach((k, v) -> {
            K key = grouping(v, context);
            plugins.computeIfAbsent(key, func -> newList()).add(new Entry(k, v));
        });
        context.getPlugin().addDestroyListener(p ->
                plugins.values().removeIf(entries -> {
                    entries.removeIf(it -> plugin.containsKey(it.path));
                    return entries.isEmpty();
                }));
    }

    protected Map<K, List<Entry>> newMap() {
        return new ConcurrentHashMap<>();
    }

    protected List<Entry> newList() {
        return new ArrayList<>();
    }

    protected Type getPluginType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            return ((ParameterizedType) superclass).getActualTypeArguments()[1];
        }
        throw new PluginException("Unsupported type: " + superclass);
    }

    public abstract K grouping(V plugin, PluginContext context);

    @Getter
    @RequiredArgsConstructor
    public class Entry {

        private final String path;

        private final V value;
    }
}
