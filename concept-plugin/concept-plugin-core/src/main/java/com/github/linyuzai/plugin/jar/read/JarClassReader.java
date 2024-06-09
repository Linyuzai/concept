package com.github.linyuzai.plugin.jar.read;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.DependencyReader;
import com.github.linyuzai.plugin.jar.concept.JarPluginClassLoader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Getter
public class JarClassReader extends DependencyReader implements ClassReader {

    private final List<URL> urls;

    private final JarPluginClassLoader jarClassLoader;

    public JarClassReader(Plugin plugin, List<URL> urls) throws IOException {
        super(plugin);
        this.urls = urls;
        this.jarClassLoader = new JarPluginClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
    }

    @Override
    public Class<?> read(Object key, PluginContext context) {
        return (Class<?>) super.read(key, context);
    }

    //TODO 自动创建新的类加载器重新加载
    @SneakyThrows
    @Override
    public Class<?> doRead(Object key) {
        return jarClassLoader.loadClass(String.valueOf(key));
    }

    @Override
    public Class<?> getReadableType() {
        return Class.class;
    }

    @Override
    public void close() throws IOException {
        jarClassLoader.close();
    }
}
