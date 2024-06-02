package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoader;
import com.github.linyuzai.plugin.jar.classloader.PluginClassLoaderFactory;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarFile;

/**
 * 基于 jar 的插件
 */
@Getter
public class JarPlugin extends ZipPlugin {

    public static final String JAR_PREFIX = PREFIX + "JAR@";

    public static final String ENTRY = JAR_PREFIX + "ENTRY";

    public static final String CLASS_NAME = JAR_PREFIX + "CLASS_NAME";

    public static final String CLASS = JAR_PREFIX + "CLASS";

    public static final String INSTANCE = JAR_PREFIX + "INSTANCE";

    private final PluginClassLoaderFactory pluginClassLoaderFactory;

    /**
     * 插件类加载器
     */
    private PluginClassLoader pluginClassLoader;

    /**
     * 资源的 URL
     */
    private URL url;

    private JarURLConnection connection;

    public JarPlugin(String path, PluginClassLoaderFactory pluginClassLoaderFactory) {
        super(path);
        this.pluginClassLoaderFactory = pluginClassLoaderFactory;
    }

    /**
     * 准备，通过 {@link JarURLConnection} 来读取 jar 内容
     */
    @SneakyThrows
    @Override
    public void onPrepare(PluginContext context) {
        this.url = parseURL(getPath());
        this.pluginClassLoader = pluginClassLoaderFactory.create(new URL[]{url}, getConcept());
        super.onPrepare(context);
    }

    @Override
    protected JarFile createFile() throws IOException {
        this.connection = (JarURLConnection) getUrl().openConnection();
        return getConnection().getJarFile();
    }

    @Override
    public JarFile getFile() {
        return (JarFile) super.getFile();
    }

    @SneakyThrows
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

    /**
     * 释放资源，关闭资源文件的引用
     */
    @Override
    public void onRelease(PluginContext context) {
        super.onRelease(context);
        connection = null;
    }

    @Override
    public String toString() {
        return "JarPlugin(" + getPath() + ")";
    }
}
