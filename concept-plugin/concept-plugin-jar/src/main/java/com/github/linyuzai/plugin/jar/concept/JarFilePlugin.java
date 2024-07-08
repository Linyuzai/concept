package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.jar.read.JarClassReader;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

@Getter
public class JarFilePlugin extends ZipFilePlugin implements JarPlugin {

    private PluginClassLoader pluginClassLoader;

    public JarFilePlugin(String path, URL url) {
        super(path, url);
    }

    @Override
    protected JarFile createZipFile() throws IOException {
        return new JarFile(path);
    }

    @Override
    protected JarPluginEntry createZipPluginEntry(URL url, String name) {
        return new JarFilePluginEntry(url, name, this, path);
    }

    @Override
    public void onPrepare(PluginContext context) {
        PluginTree tree = context.get(PluginTree.class);
        if (tree == null) {
            return;
        }
        Map<String, Content> classes = new HashMap<>();
        Map<String, Content> packages = new HashMap<>();
        collectClassContents(tree.getRoot(), classes, packages);
        this.pluginClassLoader = new JarPluginClassLoader(this, packages, classes, getClass().getClassLoader());
        //addReader(new JarClassReader(this, classLoader));
    }

    @Override
    public void onRelease(PluginContext context) {
        try {
            pluginClassLoader.close();
        } catch (Throwable ignore) {
        }
    }

    protected void collectClassContents(PluginTree.Node node,
                                        Map<String, Content> classes,
                                        Map<String, Content> packages) {
        node.forEach(it -> {
            Object value = it.getValue();
            if (value instanceof Entry) {
                Entry entry = (Entry) value;
                if (entry.getName().endsWith("/")) {
                    packages.computeIfAbsent(entry.getName(), name -> findManifestContent(it));
                } else if (entry.getName().endsWith(".class")) {
                    classes.putIfAbsent(entry.getName(), entry.getContent());
                }
            }
        });
    }

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
}
