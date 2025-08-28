package com.github.linyuzai.plugin.autoconfigure.observable;

import com.github.linyuzai.plugin.autoconfigure.bean.BeanExtractor;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.type.NestedType;
import com.github.linyuzai.plugin.core.type.NestedTypeFactory;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public abstract class GenericPluginObservable<K, V> implements PluginObservable<K, V>, ApplicationContextAware {

    private final Map<K, V> plugins = createMap();

    @Override
    public V get(K key) {
        return plugins.get(key);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        plugins.forEach(action);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        NestedTypeFactory nestedTypeFactory = applicationContext.getBean(NestedTypeFactory.class);
        NestedType nestedType = nestedTypeFactory.create(getClass());
        PluginConcept concept = applicationContext.getBean(PluginConcept.class);
        concept.addHandlers(new BeanExtractor<V>() {

            @Override
            protected Invoker createInvoker() {
                return super.createInvoker(nestedType.getChildren().get(1).toType(), new Annotation[]{});
            }

            @Override
            public void onExtract(V plugin, PluginContext context) {
                K key = mappingKey(plugin, context);
                context.getPlugin().addDestroyListener(p -> plugins.remove(key));
                plugins.compute(key, (k, v) -> {
                    if (v == null) {
                        return plugin;
                    } else {
                        return mergingValue(k, v, plugin, context);
                    }
                });
            }
        });
    }

    protected Map<K, V> createMap() {
        return new ConcurrentHashMap<>();
    }

    public abstract K mappingKey(V plugin, PluginContext context);

    public abstract V mergingValue(K key, V exist, V loaded, PluginContext context);
}
