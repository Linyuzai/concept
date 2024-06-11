package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.jar.read.JarClassReader;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.zip.ZipInputStream;

public class JarPlugin extends ZipPlugin {

    public JarPlugin(ZipInputStream inputStream, URL url, Entry parent) {
        super(inputStream, url, parent);
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
        JarPluginClassLoader classLoader =
                new JarPluginClassLoader(packages, classes, getClass().getClassLoader());
        addReader(new JarClassReader(this, classLoader));
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

    public interface ClassName {

    }

    public static class Mode {

        public static final String FILE = "FILE";

        public static final String STREAM = "STREAM";
    }
}
