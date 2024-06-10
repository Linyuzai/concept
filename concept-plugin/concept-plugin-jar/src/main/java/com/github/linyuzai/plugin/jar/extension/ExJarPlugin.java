package com.github.linyuzai.plugin.jar.extension;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
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
public class ExJarPlugin extends AbstractPlugin {

    private final URL url;

    private final ExJarFile jarFile;

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
    public void onOpen(PluginContext context) {
        PluginTree.Node node = context.get(PluginTree.Node.class);
        if (node == null || node.getParent() != null) {
            return;
        }
        List<URL> urls = new ArrayList<>();
        node.forEach(it -> {
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
    public void onClose(PluginContext context) {
        try {
            jarFile.close();
        } catch (Throwable e) {
            //TODO
        }
    }

    @Override
    public String toString() {
        return "ExJarPlugin(" + url + ")";
    }

    public static class Mode {

        public static final String FILE = "FILE";

        public static final String STREAM = "STREAM";
    }
}
