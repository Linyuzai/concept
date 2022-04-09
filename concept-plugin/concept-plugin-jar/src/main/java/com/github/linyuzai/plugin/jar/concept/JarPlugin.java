package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * 基于 jar 的插件
 */
@Getter
public class JarPlugin implements Plugin {

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

    private final PluginConcept pluginConcept;

    private JarURLConnection connection;

    private JarFile file;

    public JarPlugin(URL url, PluginClassLoader classLoader, JarPluginConcept concept) {
        this.url = url;
        this.pluginClassLoader = classLoader;
        this.pluginConcept = concept;
    }

    @Override
    public Object getId() {
        return getUrl();
    }

    /**
     * 加载，通过 {@link JarURLConnection} 来读取 jar 内容
     */
    @SneakyThrows
    @Override
    public void load() {
        this.connection = (JarURLConnection) getUrl().openConnection();
        this.file = getConnection().getJarFile();
    }

    /**
     * 释放资源，关闭资源文件的引用
     */
    @Override
    public void release() {
        if (file != null) {
            try {
                file.close();
            } catch (Throwable ignore) {
            }
        }
        file = null;
        connection = null;
    }

    @Override
    public String toString() {
        return "JarPlugin(" + url + ")";
    }
}
