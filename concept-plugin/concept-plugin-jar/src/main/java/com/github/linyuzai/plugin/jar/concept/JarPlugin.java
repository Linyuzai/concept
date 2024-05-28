package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * 基于 jar 的插件
 */
@Getter
public class JarPlugin extends AbstractPlugin {

    public static final String JAR_PREFIX = PREFIX + "JAR@";

    public static final String ENTRY = JAR_PREFIX + "ENTRY";

    public static final String CLASS_NAME = JAR_PREFIX + "CLASS_NAME";

    public static final String CLASS = JAR_PREFIX + "CLASS";

    public static final String INSTANCE = JAR_PREFIX + "INSTANCE";

    /**
     * 资源的 URL
     */
    private final URL url;

    /**
     * 插件类加载器
     */
    private final PluginClassLoader pluginClassLoader;

    private JarURLConnection connection;

    private JarFile file;

    public JarPlugin(URL url, PluginClassLoader pluginClassLoader) {
        this.url = url;
        this.pluginClassLoader = pluginClassLoader;
    }

    @Override
    public Object getId() {
        return getUrl();
    }

    /**
     * 准备，通过 {@link JarURLConnection} 来读取 jar 内容
     */
    @SneakyThrows
    @Override
    public void onPrepare(PluginContext context) {
        this.connection = (JarURLConnection) getUrl().openConnection();
        this.file = getConnection().getJarFile();
    }

    /**
     * 释放资源，关闭资源文件的引用
     */
    @Override
    public void onRelease(PluginContext context) {
        if (file != null) {
            try {
                file.close();
            } catch (Throwable ignore) {
            }
            file = null;
            connection = null;
        }
    }

    @Override
    public Collection<Object> collectContent(PluginContext context) {
        return file.stream()
                .map(ZipEntry::getName)
                //测试之后win环境中读取也不会存在\\的分隔符
                //.map(it -> it.replaceAll("\\\\", "/"))
                .filter(it -> !it.endsWith("/"))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "JarPlugin(" + url + ")";
    }
}
