package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.net.JarURLConnection;
import java.net.URL;
import java.util.jar.JarFile;

public class JarPlugin implements Plugin {

    public static final String JAR_PREFIX = PREFIX + "JAR@";

    public static final String ENTRIES = JAR_PREFIX + "ENTRIES";

    public static final String FILE_NAMES = JAR_PREFIX + "FILE_NAMES";

    public static final String PROPERTIES_NAMES = JAR_PREFIX + "PROPERTIES_NAMES";

    public static final String CLASS_NAMES = JAR_PREFIX + "CLASS_NAMES";

    public static final String CLASSES = JAR_PREFIX + "CLASSES";

    @Getter
    private final URL url;

    @Getter
    @Setter
    private ClassLoader classLoader;

    private JarURLConnection connection;

    private JarFile file;

    public JarPlugin(URL url) {
        this.url = url;
    }

    @SneakyThrows
    public JarURLConnection getConnection() {
        if (connection == null) {
            connection = (JarURLConnection) getUrl().openConnection();
        }
        return connection;
    }

    @SneakyThrows
    public JarFile getFile() {
        if (file == null) {
            file = getConnection().getJarFile();
        }
        return file;
    }
}
