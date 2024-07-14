package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.PluginClassLoader;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 基于 jar 的插件
 */
@Getter
@Setter
public class ExJarPlugin extends AbstractPlugin implements JarPlugin {

    private final ExJarFile jarFile;

    private final URL url;

    private PluginClassLoader pluginClassLoader;

    @SneakyThrows
    public ExJarPlugin(ExJarFile jarFile) {
        this.jarFile = jarFile;
        this.url = jarFile.getURL();
    }

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public void forEachEntry(PluginContext context, Consumer<Entry> consumer) {
        jarFile.stream()
                .map(ExJarEntry.class::cast)
                .map(it -> new ExJarPluginEntry(jarFile, it, this))
                .forEach(consumer);
    }

    /**
     * 准备，通过 {@link JarURLConnection} 来读取 jar 内容
     */
    @Override
    public void onPrepare(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        if (tree == null) {
            return;
        }
        List<URL> urlList = new ArrayList<>();
        tree.getRoot().forEach(it -> {
            if (it.isPluginNode() && it.getId() instanceof URL) {
                URL url = (URL) it.getId();
                urlList.add(url);
            }
        });
        if (urlList.isEmpty()) {
            return;
        }
        URL[] urls = urlList.toArray(new URL[0]);
        ClassLoader parent = getClass().getClassLoader();
        this.pluginClassLoader = new ExJarPluginClassLoader(this, urls, parent);
    }

    @Override
    public void onDestroy() {
        try {
            jarFile.close();
        } catch (Throwable ignore) {
        }
        try {
            pluginClassLoader.close();
        } catch (Throwable ignore) {
        }
    }
}
