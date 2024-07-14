package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Getter;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 插件类加载器
 */
@Getter
public abstract class PluginClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private final Plugin plugin;

    public PluginClassLoader(Plugin plugin, URL[] urls, ClassLoader parent) {
        super(urls, parent);
        this.plugin = plugin;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            definePackageIfNecessary(name);
        } catch (IllegalArgumentException ex) {
            // Tolerate race condition due to being parallel capable
            if (getPackage(name) == null) {
                // This should never happen as the IllegalArgumentException indicates
                // that the package has already been defined and, therefore,
                // getPackage(name) should not return null.
                throw new AssertionError("Package " + name + " has already been defined but it could not be found");
            }
        }
        return super.loadClass(name, resolve);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findPluginClass(name, new LinkedHashSet<>());
        /*Collection<Plugin> plugins = new LinkedHashSet<>();
        collectDependencyPlugins(plugin, plugins);
        for (Plugin p : plugins) {
            if (p instanceof JarPlugin) {
                try {
                    return ((JarPlugin) p).getPluginClassLoader().findPluginClass(name);
                } catch (ClassNotFoundException ignore) {
                }
            }
        }
        throw new ClassNotFoundException(name);*/
    }

    /**
     * 基于依赖的插件获得类
     */
    private Class<?> findPluginClass(String name, Collection<Plugin> plugins) throws ClassNotFoundException {
        if (plugins.contains(plugin)) {
            throw new ClassNotFoundException(name);
        }
        //缓存已经找过的插件防止重复查找
        plugins.add(plugin);
        try {
            //查找当前插件的类
            return findPluginClass(name);
        } catch (ClassNotFoundException e) {
            Plugin.StandardMetadata metadata = plugin.getMetadata().asStandard();
            Set<String> names = metadata.getDependency().getNames();
            //没有依赖的插件
            if (names == null || names.isEmpty()) {
                throw e;
            }
            List<Plugin> list = plugin.getConcept()
                    .getRepository()
                    .stream()
                    .collect(Collectors.toList());
            for (String n : names) {
                for (Plugin p : list) {
                    //匹配到依赖的jar尝试获取对应的类
                    if (p instanceof JarPlugin && Objects.equals(n, p.getMetadata().asStandard().getName())) {
                        try {
                            return ((JarPlugin) p).getPluginClassLoader().findPluginClass(name, plugins);
                        } catch (ClassNotFoundException ignore) {
                        }
                    }
                }
            }
            throw e;
        }
    }

    /**
     * 获得插件中的类
     */
    public Class<?> findPluginClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    @Deprecated
    protected void collectDependencyPlugins(Plugin plugin, Collection<Plugin> plugins) {
        if (plugins.contains(plugin)) {
            return;
        }
        plugins.add(plugin);
        Plugin.StandardMetadata metadata = plugin.getMetadata().asStandard();
        Set<String> names = metadata.getDependency().getNames();
        if (names == null || names.isEmpty()) {
            return;
        }
        List<Plugin> list = plugin.getConcept()
                .getRepository()
                .stream()
                .collect(Collectors.toList());
        for (String name : names) {
            for (Plugin p : list) {
                if (Objects.equals(name, p.getMetadata().asStandard().getName())) {
                    collectDependencyPlugins(p, plugins);
                }
            }
        }
    }

    /**
     * Define a package before a {@code findClass} call is made. This is necessary to
     * ensure that the appropriate manifest for nested JARs is associated with the
     * package.
     *
     * @param className the class name being found
     */
    private void definePackageIfNecessary(String className) {
        int lastDot = className.lastIndexOf('.');
        if (lastDot >= 0) {
            String packageName = className.substring(0, lastDot);
            if (getPackage(packageName) == null) {
                try {
                    definePackage(className, packageName);
                } catch (IllegalArgumentException ex) {
                    // Tolerate race condition due to being parallel capable
                    if (getPackage(packageName) == null) {
                        // This should never happen as the IllegalArgumentException
                        // indicates that the package has already been defined and,
                        // therefore, getPackage(name) should not have returned null.
                        throw new AssertionError(
                                "Package " + packageName + " has already been defined but it could not be found");
                    }
                }
            }
        }
    }

    /**
     * 定义包名
     */
    protected abstract void definePackage(String className, String packageName);
}
