package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * jar文件插件
 */
@Getter
@Setter
public class JarFilePlugin extends ZipFilePlugin implements JarPlugin {

    private PluginClassLoader pluginClassLoader;

    public JarFilePlugin(JarFile jarFile, URL url) {
        super(jarFile, url);
    }

    @Override
    public JarFile getZipFile() {
        return (JarFile) super.getZipFile();
    }

    @Override
    protected JarPluginEntry createPluginEntry(URL url, String name) {
        return new JarFilePluginEntry(getZipFile(), url, name, this);
    }

    /**
     * 缓存包和类的内容创建插件类加载器
     */
    @Override
    public void onPrepare(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        if (tree == null) {
            return;
        }
        if (pluginClassLoader == null) {
            Map<String, Content> classes = new HashMap<>();
            Map<String, Content> packages = new HashMap<>();
            Map<String, Content> resources = new HashMap<>();
            collectPackageAndClassContents(tree.getRoot(), classes, packages, resources);
            ClassLoader parent = getClass().getClassLoader();
            this.pluginClassLoader = new JarPluginClassLoader(this, parent,
                    packages, classes, resources);
        }
    }

    /**
     * 获取包和类的内容
     */
    protected void collectPackageAndClassContents(PluginTree.Node node,
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
    protected Content findManifestContent(PluginTree.Node node) {
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

    /**
     * 关闭插件类加载器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            pluginClassLoader.close();
        } catch (Throwable ignore) {
        }
    }
}
