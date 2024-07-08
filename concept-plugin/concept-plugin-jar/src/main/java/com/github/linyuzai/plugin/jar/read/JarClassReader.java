package com.github.linyuzai.plugin.jar.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.DependencyReader;
import com.github.linyuzai.plugin.jar.concept.PluginClassLoader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;

@Deprecated
@Getter
public class JarClassReader extends DependencyReader implements ClassReader {

    private final PluginClassLoader classLoader;

    public JarClassReader(Plugin plugin, PluginClassLoader classLoader) {
        super(plugin);
        this.classLoader = classLoader;
    }

    @Override
    public Class<?> read(Object key, PluginContext context) {
        return (Class<?>) super.read(key, context);
    }

    @SneakyThrows
    @Override
    public Class<?> doRead(Object key) {
        return classLoader.loadClass(String.valueOf(key));
    }

    @Override
    public Class<?> getReadableType() {
        return Class.class;
    }

    @Override
    public void close() throws IOException {
        classLoader.close();
    }
}
