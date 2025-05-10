package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.autoload.*;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PluginManager {

    protected final Log log = LogFactory.getLog(PluginManager.class);

    protected final Set<String> loadingSet = new HashSet<>();

    protected final Set<String> unloadingSet = new HashSet<>();

    protected final Set<String> updatingSet = new HashSet<>();

    @Autowired
    protected PluginConceptProperties properties;

    @Autowired
    protected PluginConcept concept;

    @Autowired
    protected PluginLocation location;

    @Autowired
    protected PluginExecutor executor;

    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<String> getGroups() {
        return location.getGroups();
    }

    public synchronized void addGroup(String group) {
        location.addGroup(group);
    }

    public List<PluginSummary> getPlugins(String group) {
        List<PluginSummary> list = new ArrayList<>();
        if (group.isEmpty()) {
            return list;
        }
        List<String> loaded = location.getLoadedPlugins(group);
        for (String load : loaded) {
            list.add(getLoadedPluginSummary(group, load));
        }
        List<String> unloaded = location.getUnloadedPlugins(group);
        for (String unload : unloaded) {
            list.add(getUnloadedPluginSummary(group, unload));
        }
        list.sort((o1, o2) -> Long.compare(o2.sort, o1.sort));
        return list;
    }

    public List<PluginSummary> getDeletedPlugins(String group) {
        List<PluginSummary> list = new ArrayList<>();
        if (group.isEmpty()) {
            return list;
        }

        List<String> plugins = location.getDeletedPlugins(group);
        for (String plugin : plugins) {
            list.add(getDeletedPluginSummary(group, plugin));
        }

        list.sort((o1, o2) -> Long.compare(o2.sort, o1.sort));
        return list;
    }

    public boolean existPlugin(String group, String name) {
        return location.existPlugin(group, name);
    }

    public synchronized void loadPlugin(String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        loadingSet.add(path);
        try {
            location.loadPlugin(group, name);
        } catch (Throwable e) {
            executor.execute(() -> {
                try {
                    Object source = location.getPluginSource(path);
                    concept.load(source);
                } catch (Throwable e1) {
                    log.error("Load plugin error: " + path, e1);
                } finally {
                    loadingSet.remove(path);
                }
            }, 1000, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void unloadPlugin(String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        unloadingSet.add(path);
        try {
            location.unloadPlugin(group, name);
        } catch (Throwable e) {
            executor.execute(() -> {
                try {
                    concept.unload(path);
                } catch (Throwable e1) {
                    log.error("Unload plugin error: " + path, e1);
                } finally {
                    unloadingSet.remove(path);
                }
            }, 1000, TimeUnit.MILLISECONDS);
        }
    }

    public synchronized void reloadPlugin(String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        loadingSet.add(path);
        try {
            concept.unload(path);
        } catch (Throwable e) {
            loadingSet.remove(path);
            throw e;
        }
        executor.execute(() -> {
            try {
                Object source = location.getPluginSource(path);
                concept.load(source);
            } catch (Throwable e) {
                log.error("Reload plugin error: " + path, e);
            } finally {
                loadingSet.remove(path);
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    public synchronized void renamePlugin(String group, String name, String rename) {
        location.renamePlugin(group, name, rename);
    }

    public synchronized void deletePlugin(String group, String name) {
        location.deletePlugin(group, name);
    }

    public PluginMetadata getMetadata(String group, String name) {
        String path = location.getLoadedPluginPath(group, name);
        Plugin plugin = concept.getRepository().get(path);
        if (plugin == null) {
            return concept.createMetadata(location.getUnloadedPluginPath(group, name),
                    concept.createContext());
        } else {
            return plugin.getMetadata();
        }
    }

    public List<MetadataSummary> getMetadataSummaries(String group, String name) {
        PluginMetadata metadata = getMetadata(group, name);
        List<MetadataSummary> metadataSummaries = new ArrayList<>();
        if (metadata == null) {
            return metadataSummaries;
        }
        Set<String> names = metadata.names();
        for (String n : names) {
            String v = metadata.get(n);
            metadataSummaries.add(new MetadataSummary(n, v));
        }
        return metadataSummaries;
    }

    public synchronized void updatePlugin(String group, String original, String upload) throws IOException {
        String newPath = location.getLoadedPluginPath(group, upload);
        loadingSet.add(newPath);
        String oldPath = location.getLoadedPluginPath(group, original);
        updatingSet.add(oldPath);
        PluginEventListener listener = new PluginEventListener() {

            @Override
            public void onEvent(Object event) {
                if (event instanceof PluginAutoEvent) {
                    String path = ((PluginAutoEvent) event).getPath();
                    if (Objects.equals(newPath, path)) {
                        updatingSet.remove(oldPath);
                        if (event instanceof PluginAutoLoadEvent) {
                            location.deletePlugin(group, original);
                            concept.getEventPublisher().unregister(this);
                        }
                    }
                }
            }
        };
        concept.getEventPublisher().register(listener);
        try {
            location.loadPlugin(group, upload);
        } catch (Throwable e) {
            log.error("Load plugin error: " + newPath, e);
            loadingSet.remove(newPath);
            updatingSet.remove(oldPath);
            concept.getEventPublisher().unregister(listener);
        }
    }

    public synchronized String uploadPlugin(String group, String name, InputStream is, long length) throws IOException {
        return location.uploadPlugin(group, name, is, length);
    }

    public InputStream downloadPlugin(String group, String name) throws IOException {
        try {
            return location.getLoadedPluginInputStream(group, name);
        } catch (Throwable e) {
            return location.getUnloadedPluginInputStream(group, name);
        }
    }

    public InputStream downloadDeletedPlugin(String group, String name) throws IOException {
        return location.getDeletedPluginInputStream(group, name);
    }

    protected PluginSummary getLoadedPluginSummary(String group, String plugin) {
        String path = location.getLoadedPluginPath(group, plugin);
        long timestamp = location.getPluginCreateTime(path);
        long size = location.getPluginSize(path);
        PluginState state;
        if (loadingSet.contains(path) || concept.isLoading(path)) {
            state = PluginState.LOADING;
        } else {
            Plugin get = concept.getRepository().get(path);
            if (get == null) {
                state = PluginState.LOAD_ERROR;
            } else {
                if (updatingSet.contains(path)) {
                    state = PluginState.UPDATING;
                } else {
                    state = PluginState.LOADED;
                }
            }
        }
        return new PluginSummary(plugin, formatSize(size), formatTime(timestamp), state, timestamp);
    }

    protected PluginSummary getUnloadedPluginSummary(String group, String plugin) {
        String unloadPath = location.getUnloadedPluginPath(group, plugin);
        long timestamp = location.getPluginCreateTime(unloadPath);
        long size = location.getPluginSize(unloadPath);
        PluginState state;
        String path = location.getLoadedPluginPath(group, plugin);
        if (unloadingSet.contains(path) || concept.isUnloading(path)) {
            state = PluginState.UNLOADING;
        } else {
            Plugin get = concept.getRepository().get(path);
            if (get == null) {
                state = PluginState.UNLOADED;
            } else {
                state = PluginState.UNLOAD_ERROR;
            }
        }
        return new PluginSummary(plugin, formatSize(size), formatTime(timestamp), state, timestamp);
    }

    protected PluginSummary getDeletedPluginSummary(String group, String plugin) {
        String deletePath = location.getDeletedPluginPath(group, plugin);
        long timestamp = location.getPluginCreateTime(deletePath);
        long size = location.getPluginSize(deletePath);
        PluginState state = PluginState.DELETED;
        return new PluginSummary(plugin, formatSize(size), formatTime(timestamp), state, timestamp);
    }

    public void init() {
        concept.getEventPublisher().register(new PluginAutoLoadListener());
    }

    protected String formatSize(long size) {
        if (size < 0) {
            return null;
        }
        if (size >= 1024) {
            double k = size / 1024.0;
            if (k >= 1024) {
                double m = k / 1024;
                return String.format("%.2f", m) + "M";
            } else {
                return String.format("%.2f", k) + "K";
            }
        } else {
            return size + "B";
        }
    }

    protected String formatTime(long time) {
        if (time < 0) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(dateTime);
    }

    public class PluginAutoLoadListener implements PluginEventListener {

        @Override
        public void onEvent(Object event) {
            if (event instanceof PluginAutoEvent) {
                String path = ((PluginAutoEvent) event).getPath();
                if (event instanceof PluginAutoLoadEvent ||
                        event instanceof PluginAutoLoadErrorEvent) {
                    loadingSet.remove(path);
                } else if (event instanceof PluginAutoUnloadEvent ||
                        event instanceof PluginAutoUnloadErrorEvent) {
                    unloadingSet.remove(path);
                }
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class MetadataSummary {

        private final String name;

        private final String value;
    }

    @Getter
    @RequiredArgsConstructor
    public static class PluginSummary {

        private final String plugin;

        private final String size;

        private final String creationTime;

        private final PluginState state;

        private final long sort;
    }

    public enum PluginState {

        LOADED, LOADING, LOAD_ERROR, UNLOADED, UNLOADING, UNLOAD_ERROR, DELETED, UPDATING
    }
}
