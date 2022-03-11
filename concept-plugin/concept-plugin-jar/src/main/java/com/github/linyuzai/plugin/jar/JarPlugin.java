package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.classloader.DynamicParentClassLoader;
import com.github.linyuzai.plugin.jar.classloader.JarPluginClassLoader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarFile;

@Getter
public class JarPlugin implements Plugin {

    public static final String JAR_PREFIX = PREFIX + "JAR@";

    public static final String ENTRIES = JAR_PREFIX + "ENTRIES";

    public static final String FILE_NAMES = JAR_PREFIX + "FILE_NAMES";

    public static final String PROPERTIES_NAMES = JAR_PREFIX + "PROPERTIES_NAMES";

    public static final String PROPERTIES = JAR_PREFIX + "PROPERTIES";

    public static final String CLASS_NAMES = JAR_PREFIX + "CLASS_NAMES";

    public static final String CLASSES = JAR_PREFIX + "CLASSES";

    public static final String INSTANCES = JAR_PREFIX + "INSTANCES";

    private final String id;

    private final URL url;

    private final ClassLoader classLoader;

    private JarURLConnection connection;

    private JarFile file;

    public JarPlugin(URL url, JarPluginClassLoader classLoader) {
        this.id = url.toString();
        this.url = url;
        this.classLoader = new DynamicParentClassLoader(url, classLoader);
    }

    @SneakyThrows
    @Override
    public void initialize() {
        connection = (JarURLConnection) getUrl().openConnection();
        file = getConnection().getJarFile();
    }

    @Override
    public void destroy() {
        if (file != null) {
            try {
                file.close();
            } catch (Throwable ignore) {
            }
        }
        file = null;
        connection = null;
    }
}
