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

    public static final String ENTRY = JAR_PREFIX + "ENTRY";

    public static final String CLASS_NAME = JAR_PREFIX + "CLASS_NAME";

    public static final String CLASS = JAR_PREFIX + "CLASS";

    public static final String INSTANCE = JAR_PREFIX + "INSTANCE";

    private final URL url;

    private final PluginConcept pluginConcept;

    private final JarPluginClassLoader classLoader;

    private JarURLConnection connection;

    private JarFile file;

    public JarPlugin(URL url, ClassLoader parent, JarPluginConcept concept) {
        this.url = url;
        this.pluginConcept = concept;
        this.classLoader = new JarPluginClassLoader(url, parent, concept);
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
