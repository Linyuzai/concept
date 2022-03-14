package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
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

    public static final String CLASS_NAMES = JAR_PREFIX + "CLASS_NAMES";

    public static final String CLASSES = JAR_PREFIX + "CLASSES";

    public static final String INSTANCES = JAR_PREFIX + "INSTANCES";

    private final URL url;

    private final PluginConcept pluginConcept;

    private final JarPluginClassLoader classLoader;

    private JarURLConnection connection;

    private JarFile file;

    public JarPlugin(URL url, ClassLoader parent, JarPluginConcept pluginConcept) {
        this.url = url;
        this.pluginConcept = pluginConcept;
        this.classLoader = new JarPluginClassLoader(url, parent, pluginConcept);
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
    }
}
