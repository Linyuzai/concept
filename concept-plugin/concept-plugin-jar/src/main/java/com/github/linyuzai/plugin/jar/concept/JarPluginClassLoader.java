package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.util.ReadUtils;

import java.io.IOException;
import java.io.InputStream;
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
     * 类缓存
     */
    private final Map<String, Plugin.Content> resources = new ConcurrentHashMap<>();

    /**
     * Create a new {@link JarPluginClassLoader} instance.
     */
    public JarPluginClassLoader(Plugin plugin,
                                ClassLoader parent,
                                Map<String, Plugin.Content> packages,
                                Map<String, Plugin.Content> classes,
                                Map<String, Plugin.Content> resources) {
        super(plugin, new URL[0], parent);
        this.packages.putAll(packages);
        this.classes.putAll(classes);
        this.resources.putAll(resources);
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
        byte[] bytes = ReadUtils.read(content.getInputStream());
        return defineClass(name, bytes, 0, bytes.length);
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

    @Override
    public InputStream getResourceAsStream(String name) {
        Plugin.Content content = resources.getOrDefault(name, classes.get(name));
        return content == null ? null : content.getInputStream();
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
