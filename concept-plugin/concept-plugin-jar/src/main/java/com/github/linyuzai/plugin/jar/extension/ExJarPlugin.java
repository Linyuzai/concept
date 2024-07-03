package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.read.JarClassReader;
import lombok.Getter;
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
public class ExJarPlugin extends AbstractPlugin implements JarPlugin {

    private final ExJarFile jarFile;

    private final Object source;

    private final URL url;

    @SneakyThrows
    public ExJarPlugin(ExJarFile jarFile, Object source) {
        this.jarFile = jarFile;
        this.source = source;
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
    public void collectEntries(PluginContext context, Consumer<Entry> consumer) {
        jarFile.stream()
                .map(ExJarEntry.class::cast)
                .map(it -> new ExJarPluginEntry(this, jarFile, it))
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
        List<URL> urls = new ArrayList<>();
        tree.getRoot().forEach(it -> {
            if (it.isPluginNode() && it.getId() instanceof URL) {
                URL url = (URL) it.getId();
                urls.add(url);
            }
        });
        if (urls.isEmpty()) {
            return;
        }
        ExJarPluginClassLoader classLoader =
                new ExJarPluginClassLoader(urls.toArray(new URL[0]), getClass().getClassLoader());
        addReader(new JarClassReader(this, classLoader));
    }

    @Override
    public void onRelease(PluginContext context) {
        try {
            jarFile.close();
        } catch (Throwable ignore) {
        }
    }
}
