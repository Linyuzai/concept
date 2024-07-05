package com.github.linyuzai.plugin.autoconfigure.factory;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.factory.JarPluginFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.*;

import java.io.File;
import java.util.Set;

@Getter
@Setter
public class BinderMetadataJarPluginFactory extends JarPluginFactory implements EnvironmentAware {

    private Class<? extends Plugin.StandardMetadata> standardMetadataType = JarPlugin.StandardMetadata.class;

    private Environment environment;

    @Override
    protected PluginMetadata createMetadata(File file, PluginContext context) {
        PluginMetadata metadata = super.createMetadata(file, context);
        if (metadata == null) {
            return null;
        }
        return new BinderMetadata(metadata);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Getter
    public class BinderMetadata implements PluginMetadata {

        private final PluginMetadata delegate;

        private final Binder binder;

        private Plugin.StandardMetadata standard;

        public BinderMetadata(PluginMetadata delegate) {
            this.delegate = delegate;
            this.binder = Binder.get(new MetadataEnvironment(delegate, environment));
            bind();
        }

        @Override
        public String get(String name) {
            return get(name, null);
        }

        @Override
        public String get(String name, String defaultValue) {
            return binder.bind(name, String.class).orElse(defaultValue);
        }

        @Override
        public Set<String> names() {
            return delegate.names();
        }

        @Override
        public <T> T bind(String name, Class<T> type) {
            return binder.bindOrCreate(name, Bindable.of(type));
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Plugin.StandardMetadata> T standard() {
            return (T) standard;
        }

        @Override
        public void set(String name, String value) {
            delegate.set(name, value);
            if (name != null && name.startsWith(PluginMetadata.PREFIX)) {
                bind();
            }
        }

        protected void bind() {
            this.standard = bind(PluginMetadata.PREFIX, standardMetadataType);
        }
    }

    public static class MetadataEnvironment extends AbstractEnvironment {

        public MetadataEnvironment(PluginMetadata metadata, Environment environment) {
            getPropertySources().addLast(new MetadataPropertySource(PluginMetadata.class.getSimpleName(), metadata));
            if (environment instanceof ConfigurableEnvironment) {
                MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
                sources.stream().forEach(it -> getPropertySources().addLast(it));
            }
        }
    }

    public static class MetadataPropertySource extends EnumerablePropertySource<PluginMetadata> {

        public MetadataPropertySource(String name, PluginMetadata source) {
            super(name, source);
        }

        @Override
        public String[] getPropertyNames() {
            return source.names().toArray(new String[0]);
        }

        @Override
        public Object getProperty(String name) {
            return source.get(name);
        }
    }
}
