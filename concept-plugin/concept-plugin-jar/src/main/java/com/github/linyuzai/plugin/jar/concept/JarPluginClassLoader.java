package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;

public class JarPluginClassLoader extends AbstractPluginClassLoader {

    static {
        registerAsParallelCapable();
    }

    private final Map<String, Plugin.Content> packages = new ConcurrentHashMap<>();

    private final Map<String, Plugin.Content> classes = new ConcurrentHashMap<>();

    /**
     * Create a new {@link JarPluginClassLoader} instance.
     *
     * @param parent the parent class loader for delegation
     * @since 2.3.1
     */
    public JarPluginClassLoader(Map<String, Plugin.Content> packages,
                                Map<String, Plugin.Content> classes,
                                ClassLoader parent) {
        super(new URL[0], parent);
        this.packages.putAll(packages);
        this.classes.putAll(classes);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        Plugin.Content content = classes.get(path);
        if (content == null) {
            throw new ClassNotFoundException(name);
        }
        try {
            byte[] bytes = PluginUtils.read(content.getInputStream());
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    @Override
    protected void definePackage(String className, String packageName) {
        String packageEntryName = packageName.replace('.', '/') + "/";
        String classEntryName = className.replace('.', '/') + ".class";
        if (packages.containsKey(packageEntryName) && classes.containsKey(classEntryName)) {
            Plugin.Content content = packages.get(packageEntryName);
            if (content != null) {
                try {
                    definePackage(packageName, new Manifest(content.getInputStream()), null);
                } catch (IOException e) {
                }
            }
        }
    }
}
