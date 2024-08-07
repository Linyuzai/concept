package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.PluginUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.Manifest;

/**
 * jar插件类加载器
 */
public class JarPluginClassLoader extends PluginClassLoader {

    static {
        registerAsParallelCapable();
    }

    /**
     * 包缓存
     */
    private final Map<String, Plugin.Content> packages = new ConcurrentHashMap<>();

    /**
     * 类缓存
     */
    private final Map<String, Plugin.Content> classes = new ConcurrentHashMap<>();

    /**
     * Create a new {@link JarPluginClassLoader} instance.
     */
    public JarPluginClassLoader(Plugin plugin,
                                Map<String, Plugin.Content> packages,
                                Map<String, Plugin.Content> classes,
                                ClassLoader parent) {
        super(plugin, new URL[0], parent);
        this.packages.putAll(packages);
        this.classes.putAll(classes);
    }

    /**
     * 通过缓存获取类
     */
    @Override
    public Class<?> findPluginClass(String name) throws ClassNotFoundException {
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

    /**
     * 通过缓存定义包名
     */
    @Override
    protected void definePackage(String className, String packageName) {
        String packageEntryName = packageName.replace('.', '/') + "/";
        String classEntryName = className.replace('.', '/') + ".class";
        if (packages.containsKey(packageEntryName) && classes.containsKey(classEntryName)) {
            Plugin.Content content = packages.get(packageEntryName);
            if (content != null) {
                try {
                    definePackage(packageName, new Manifest(content.getInputStream()), null);
                } catch (IOException ignore) {
                }
            }
        }
    }

    /**
     * 清空包和类缓存
     */
    @Override
    public void close() throws IOException {
        super.close();
        packages.clear();
        classes.clear();
    }
}
