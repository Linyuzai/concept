package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * jar插件
 */
public interface JarPlugin extends ZipPlugin {

    String SUFFIX_JAR = ".jar";

    /**
     * 获得插件类加载器
     */
    PluginClassLoader getPluginClassLoader();

    void setPluginClassLoader(PluginClassLoader classLoader);

    default void initClassLoader(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        if (tree == null) {
            return;
        }
        if (getPluginClassLoader() == null) {
            Map<String, Content> classes = new HashMap<>();
            Map<String, Content> packages = new HashMap<>();
            Map<String, Content> resources = new HashMap<>();
            collectContents(tree.getRoot(), classes, packages, resources);
            ClassLoader parent = getClass().getClassLoader();
            setPluginClassLoader(new JarPluginClassLoader(this, parent,
                    packages, classes, resources));
        }
    }

    /**
     * 获取包和类的内容
     */
    default void collectContents(PluginTree.Node node,
                                 Map<String, Content> classes,
                                 Map<String, Content> packages,
                                 Map<String, Content> resources) {
        node.forEach(it -> {
            Object value = it.getValue();
            if (value instanceof Entry) {
                Entry entry = (Entry) value;
                if (entry.getName().startsWith("META-INF/")) {
                    return;
                }
                if (entry.getName().endsWith("/")) {
                    packages.computeIfAbsent(entry.getName(), name -> findManifestContent(it));
                } else if (entry.getName().endsWith(".class")) {
                    classes.putIfAbsent(entry.getName(), entry.getContent());
                } else {
                    resources.putIfAbsent(entry.getName(), entry.getContent());
                }
            }
        });
    }

    /**
     * 获得 Manifest 内容
     */
    default Content findManifestContent(PluginTree.Node node) {
        PluginTree.Node parent = node.getParent();
        for (PluginTree.Node child : parent.getChildren()) {
            Object childValue = child.getValue();
            if (childValue instanceof Entry) {
                Entry childEntry = (Entry) childValue;
                if (JarFile.MANIFEST_NAME.equals(child.getName())) {
                    return childEntry.getContent();
                }
            }
        }
        return null;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends ZipPlugin.StandardMetadata {

    }
}
