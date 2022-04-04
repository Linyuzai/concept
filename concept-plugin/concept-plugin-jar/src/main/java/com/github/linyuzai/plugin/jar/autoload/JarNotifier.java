package com.github.linyuzai.plugin.jar.autoload;

import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.net.URL;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class JarNotifier implements BiConsumer<String, WatchEvent.Kind<?>> {

    private final JarPluginConcept concept;

    private final Map<String, URL> pathUrlMapping = new ConcurrentHashMap<>();

    public JarNotifier(JarPluginConcept concept) {
        this.concept = concept;
    }

    @Override
    public void accept(String path, WatchEvent.Kind<?> kind) {
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            load(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
            unload(path);
            load(path);
        } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
            unload(path);
        }
    }

    public void load(String path) {
        JarPlugin plugin = (JarPlugin) concept.create(path);
        concept.load(plugin);
        pathUrlMapping.put(path, plugin.getUrl());
    }

    public void unload(String path) {
        URL url = pathUrlMapping.get(path);
        if (url == null) {
            return;
        }
        concept.getPluginClassLoaders().remove(url);
    }
}
