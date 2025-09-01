package com.github.linyuzai.plugin.autoconfigure.metadata;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.metadata.PluginMetadataFactory;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.*;

import java.util.Set;

/**
 * 可绑定配置文件的元数据Finder
 */
@Getter
@Setter
public class BinderPluginMetadataFactory implements PluginMetadataFactory, EnvironmentAware {

    private PluginMetadataFactory metadataFactory;

    private Class<? extends Plugin.StandardMetadata> standardMetadataType = JarPlugin.StandardMetadata.class;

    private Environment environment;

    @Override
    public PluginMetadata create(PluginDefinition definition, PluginContext context) {
        PluginMetadata metadata = metadataFactory.create(definition, context);
        if (metadata == null) {
            return null;
        }
        return new BinderMetadata(metadata);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 可绑定的插件配置
     */
    @Getter
    public class BinderMetadata implements PluginMetadata {

        private final PluginMetadata delegate;

        private final Binder binder;

        private Plugin.StandardMetadata standard;

        public BinderMetadata(PluginMetadata delegate) {
            this.delegate = delegate;
            this.binder = Binder.get(new MetadataEnvironment(delegate, environment));
            this.bindStandard();
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
        public <T extends Plugin.StandardMetadata> T asStandard() {
            return (T) standard;
        }

        @Override
        public void set(String name, String value) {
            delegate.set(name, value);
        }

        @Override
        public void refresh() {
            bindStandard();
        }

        protected void bindStandard() {
            this.standard = bind(PluginMetadata.PREFIX, standardMetadataType);
        }
    }

    /**
     * 插件配置环境
     */
    public static class MetadataEnvironment extends AbstractEnvironment {

        public MetadataEnvironment(PluginMetadata metadata, Environment environment) {
            getPropertySources().addLast(new MetadataPropertySource(PluginMetadata.class.getSimpleName(), metadata));
            if (environment instanceof ConfigurableEnvironment) {
                MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();
                sources.stream().forEach(it -> {
                    if (!"configurationProperties".equals(it.getName())) {
                        getPropertySources().addLast(it);
                    }
                });
            }
        }
    }

    /**
     * 插件配置源
     */
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
