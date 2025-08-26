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

public abstract class GenericPluginObservable<K, P> implements PluginObservable<K, P>, ApplicationContextAware {

    private final Map<K, P> plugins = new ConcurrentHashMap<>();

    @Override
    public P get(K key) {
        return plugins.get(key);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        NestedTypeFactory nestedTypeFactory = applicationContext.getBean(NestedTypeFactory.class);
        NestedType nestedType = nestedTypeFactory.create(getClass());
        PluginConcept concept = applicationContext.getBean(PluginConcept.class);
        concept.addHandlers(new BeanExtractor<P>() {

            @Override
            protected Invoker createInvoker() {
                return super.createInvoker(nestedType.getChildren().get(1).toType(), new Annotation[]{});
            }

            @Override
            public void onExtract(P plugin, PluginContext context) {
                K key = mappingKey(plugin, context);
                plugins.put(key, plugin);
                context.getPlugin().addDestroyListener(p -> plugins.remove(key));
            }
        });
    }

    public abstract K mappingKey(P plugin, PluginContext context);
}
