package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginEntry;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.read.metadata.PropertiesMetadataReader;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.jar.extension.NestedJarEntry;
import com.github.linyuzai.plugin.jar.extension.NestedJarFile;
import com.github.linyuzai.plugin.jar.read.JarClassReader;
import com.github.linyuzai.plugin.jar.read.JarContentReader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 jar 的插件
 */
@Getter
public class JarPlugin extends AbstractPlugin {

    public static final String JAR_PREFIX = Plugin.PREFIX + "JAR@";

    public static final String ENTRY = JAR_PREFIX + "ENTRY";

    public static final String CLASS_NAME = JAR_PREFIX + "CLASS_NAME";

    public static final String CLASS = JAR_PREFIX + "CLASS";

    public static final String INSTANCE = JAR_PREFIX + "INSTANCE";

    private final URL url;

    private final NestedJarFile jarFile;

    @SneakyThrows
    public JarPlugin(NestedJarFile jarFile) {
        this.jarFile = jarFile;
        this.url = jarFile.getURL();
    }

    @Override
    public Object getId() {
        return url;
    }

    @Override
    public Collection<PluginEntry> collectEntries(PluginContext context) {
        return jarFile.stream()
                .map(NestedJarEntry.class::cast)
                .map(it -> new JarPluginEntry(this, jarFile, it))
                .collect(Collectors.toList());
    }

    /**
     * 准备，通过 {@link JarURLConnection} 来读取 jar 内容
     */
    @Override
    public void onPrepare(PluginContext context) {
        prepareContentReader(context);
        prepareMetadataReader(context);
        prepareClassReader(context);
    }

    protected void prepareContentReader(PluginContext context) {
        addReader(new JarContentReader(jarFile));
    }

    @SneakyThrows
    protected void prepareMetadataReader(PluginContext context) {
        //只处理主插件
        PluginTree.Node node = context.get(PluginTree.Node.class);
        if (node == null || node.getParent() == null) {
            addReader(new PropertiesMetadataReader(this));
        }
    }

    @SneakyThrows
    protected void prepareClassReader(PluginContext context) {
        PluginTree.Node node = context.get(PluginTree.Node.class);
        if (node != null && node.getParent() == null) {
            List<URL> urls = new ArrayList<>();
            node.forEach(node1 -> {
                Object value = node1.getValue();
                if (value instanceof JarPlugin) {
                    try {
                        urls.add(((JarPlugin) value).jarFile.getURL());
                    } catch (MalformedURLException e) {
                        //TODO
                        //context.publish();
                    }
                }
            });
            addReader(new JarClassReader(this, urls));
        }
    }

    @Override
    public void onRelease(PluginContext context) {
        try {
            jarFile.close();
        } catch (IOException e) {
            //TODO
        }
    }

    @SneakyThrows
    @Deprecated
    public URL parseURL(String jarPath) {
        //"jar".equals(((URL) o).getProtocol()
        String url;
        if (jarPath.startsWith("http")) {
            if (jarPath.endsWith("/")) {
                jarPath = jarPath.substring(0, jarPath.length() - 1);
            }
            url = "jar:" + jarPath + "!/";
        } else {
            if (jarPath.startsWith("/")) {
                jarPath = jarPath.substring(1);
            }
            url = "jar:file:/" + jarPath + "!/";
        }
        return new URL(url);
    }

    @Override
    public String toString() {
        return "JarPlugin(" + url + ")";
    }
}
