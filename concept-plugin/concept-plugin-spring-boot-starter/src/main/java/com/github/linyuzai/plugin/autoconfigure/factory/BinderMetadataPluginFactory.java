package com.github.linyuzai.plugin.autoconfigure.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.factory.PluginFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.*;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class BinderMetadataPluginFactory implements PluginFactory, EnvironmentAware {

    private final PluginFactory delegate;

    private Environment environment;

    @Override
    public Plugin create(Object o, PluginContext context) {
        Plugin plugin = delegate.create(o, context);
        if (plugin != null) {
            Plugin.Metadata metadata = new BinderMetadata(plugin.getMetadata());
            plugin.setMetadata(metadata);
        }
        return plugin;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Getter
    public class BinderMetadata implements Plugin.Metadata {

        private final Plugin.Metadata delegate;

        private final Binder binder;

        public BinderMetadata(Plugin.Metadata delegate) {
            this.delegate = delegate;
            this.binder = Binder.get(new MetadataEnvironment(delegate, environment));
        }

        @Override
        public String get(String key) {
            return bind(key, String.class);
        }

        @Override
        public String get(String key, String defaultValue) {
            String value = get(key);
            return value == null ? defaultValue : value;
        }

        @Override
        public Set<String> keys() {
            return delegate.keys();
        }

        @Override
        public <T> T bind(String key, Class<T> type) {
            BindResult<T> bind = binder.bind(key, Bindable.of(type));
            return bind.orElse(null);
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }
    }

    public static class MetadataEnvironment extends AbstractEnvironment {

        public MetadataEnvironment(Plugin.Metadata metadata, Environment environment) {
            getPropertySources().addLast(new MetadataPropertySource("PluginMetadata", metadata));
            if (environment instanceof ConfigurableEnvironment) {
                MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
                sources.stream().forEach(it -> getPropertySources().addLast(it));
            }
        }
    }

    public static class MetadataPropertySource extends EnumerablePropertySource<Plugin.Metadata> {

        public MetadataPropertySource(String name, Plugin.Metadata source) {
            super(name, source);
        }

        @Override
        public String[] getPropertyNames() {
            return source.keys().toArray(new String[0]);
        }

        @Override
        public Object getProperty(String name) {
            return source.get(name);
        }
    }
}
