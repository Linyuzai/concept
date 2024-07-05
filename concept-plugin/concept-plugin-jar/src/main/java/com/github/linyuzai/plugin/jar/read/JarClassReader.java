package com.github.linyuzai.plugin.jar.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.DependencyReader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.Closeable;
import java.io.IOException;

@Getter
public class JarClassReader extends DependencyReader implements ClassReader {

    private final ClassLoader classLoader;

    public JarClassReader(Plugin plugin, ClassLoader classLoader) {
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
        if (classLoader instanceof Closeable) {
            ((Closeable) classLoader).close();
        }
    }
}
