package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.autoload.*;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PluginManager {

    protected final Log log = LogFactory.getLog(PluginManager.class);

    protected final Set<String> loadingSet = new ConcurrentSkipListSet<>();

    protected final Set<String> unloadingSet = new ConcurrentSkipListSet<>();

    protected final Set<String> updatingSet = new ConcurrentSkipListSet<>();

    protected final PluginConceptProperties properties;

    protected final PluginConcept concept;

    protected final PluginStorage storage;

    protected final PluginExecutor executor;

    @Getter
    @Setter
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<String> getGroups() {
        return storage.getGroups();
    }

    public synchronized void addGroup(String group) {
        storage.addGroup(group);
    }

    public List<PluginSummary> getPlugins(String group) {
        List<PluginSummary> list = new ArrayList<>();
        if (group.isEmpty()) {
            return list;
        }
        list.addAll(getLoadedPluginSummaries(group));
        list.addAll(getUnloadedPluginSummaries(group));
        list.sort((o1, o2) -> Long.compare(o2.sort, o1.sort));
        return list;
    }

    public List<PluginSummary> getDeletedPlugins(String group) {
        List<PluginSummary> list = new ArrayList<>();
        if (group.isEmpty()) {
            return list;
        }

        list.addAll(getDeletedPluginSummaries(group));

        list.sort((o1, o2) -> Long.compare(o2.sort, o1.sort));
        return list;
    }

    public boolean existPlugin(String group, String name) {
        return storage.existPlugin(group, name);
    }

    public synchronized void loadPlugin(String group, String name) {
        String path = storage.getLoadedPluginPath(group, name);
        loadingSet.add(path);
        try {
            storage.loadPlugin(group, name);
        } catch (Throwable e) {
            log.error("Load plugin error: " + path, e);
            execute(() -> {
                try {
                    concept.load(path);
                } catch (Throwable e1) {
                    log.error("Load plugin error: " + path, e1);
                } finally {
                    loadingSet.remove(path);
                }
            });
        }
    }

    public synchronized void unloadPlugin(String group, String name) {
        String path = storage.getLoadedPluginPath(group, name);
        unloadingSet.add(path);
        try {
            storage.unloadPlugin(group, name);
        } catch (Throwable e) {
            log.error("Unload plugin error: " + path, e);
            execute(() -> {
                try {
                    concept.unload(path);
                } catch (Throwable e1) {
                    log.error("Unload plugin error: " + path, e1);
                } finally {
                    unloadingSet.remove(path);
                }
            });
        }
    }

    public synchronized void reloadPlugin(String group, String name) {
        String path = storage.getLoadedPluginPath(group, name);
        loadingSet.add(path);
        try {
            concept.unload(path);
        } catch (Throwable e) {
            loadingSet.remove(path);
            throw e;
        }
        execute(() -> {
            try {
                concept.load(path);
            } catch (Throwable e) {
                log.error("Reload plugin error: " + path, e);
            } finally {
                loadingSet.remove(path);
            }
        });
    }

    public synchronized void renamePlugin(String group, String name, String rename) {
        storage.renamePlugin(group, name, rename);
    }

    public synchronized void deletePlugin(String group, String name) {
        storage.deletePlugin(group, name);
    }

    public PluginMetadata getMetadata(String group, String name) {
        String path = storage.getLoadedPluginPath(group, name);
        Plugin plugin = concept.getRepository().get(path);
        if (plugin == null) {
            PluginDefinition definition = storage.getPluginDefinition(storage.getUnloadedPluginPath(group, name));
            return concept.createMetadata(definition, concept.createContext());
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
        String newPath = storage.getLoadedPluginPath(group, upload);
        loadingSet.add(newPath);
        String oldPath = storage.getLoadedPluginPath(group, original);
        updatingSet.add(oldPath);
        PluginEventListener listener = new PluginEventListener() {

            @Override
            public void onEvent(Object event) {
                if (event instanceof PluginAutoEvent) {
                    String path = ((PluginAutoEvent) event).getPath();
                    if (Objects.equals(newPath, path)) {
                        updatingSet.remove(oldPath);
                        if (event instanceof PluginAutoLoadEvent) {
                            storage.deletePlugin(group, original);
                            concept.getEventPublisher().unregister(this);
                        }
                    }
                }
            }
        };
        concept.getEventPublisher().register(listener);
        try {
            storage.loadPlugin(group, upload);
        } catch (Throwable e) {
            log.error("Update plugin error: " + newPath, e);
            loadingSet.remove(newPath);
            updatingSet.remove(oldPath);
            concept.getEventPublisher().unregister(listener);
        }
    }

    public synchronized String uploadPlugin(String group, String name, InputStream is, long length) throws IOException {
        return storage.uploadPlugin(group, name, is, length);
    }

    public InputStream downloadPlugin(String group, String name) throws IOException {
        try {
            return storage.getLoadedPluginInputStream(group, name);
        } catch (Throwable e) {
            return storage.getUnloadedPluginInputStream(group, name);
        }
    }

    public InputStream downloadDeletedPlugin(String group, String name) throws IOException {
        return storage.getDeletedPluginInputStream(group, name);
    }

    protected List<PluginSummary> getLoadedPluginSummaries(String group) {
        Map<String, String> pathNames = storage.getLoadedPlugins(group)
                .stream()
                .collect(Collectors.toMap(name ->
                        storage.getLoadedPluginPath(group, name), Function.identity()));
        return storage.getPluginDefinitions(pathNames.keySet())
                .stream()
                .map(definition -> {
                    String path = definition.getPath();
                    long timestamp = definition.getCreateTime();
                    long size = definition.getSize();
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
                    return new PluginSummary(pathNames.get(path), formatSize(size),
                            formatTime(timestamp), state, timestamp);
                })
                .collect(Collectors.toList());
    }

    protected List<PluginSummary> getUnloadedPluginSummaries(String group) {
        Map<String, String> pathNames = storage.getUnloadedPlugins(group)
                .stream()
                .collect(Collectors.toMap(name ->
                        storage.getUnloadedPluginPath(group, name), Function.identity()));
        return storage.getPluginDefinitions(pathNames.keySet()).stream()
                .map(definition -> {
                    long timestamp = definition.getCreateTime();
                    long size = definition.getSize();
                    PluginState state;
                    String name = pathNames.get(definition.getPath());
                    String path = storage.getLoadedPluginPath(group, name);
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
                    return new PluginSummary(name, formatSize(size),
                            formatTime(timestamp), state, timestamp);
                })
                .collect(Collectors.toList());
    }

    protected List<PluginSummary> getDeletedPluginSummaries(String group) {
        Map<String, String> pathNames = storage.getDeletedPlugins(group)
                .stream()
                .collect(Collectors.toMap(name ->
                        storage.getDeletedPluginPath(group, name), Function.identity()));
        return storage.getPluginDefinitions(pathNames.keySet()).stream()
                .map(definition -> {
                    String path = definition.getPath();
                    long timestamp = definition.getCreateTime();
                    long size = definition.getSize();
                    PluginState state = PluginState.DELETED;
                    return new PluginSummary(pathNames.get(path), formatSize(size),
                            formatTime(timestamp), state, timestamp);
                })
                .collect(Collectors.toList());
    }

    public void initialize() {
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

    protected void execute(Runnable runnable) {
        executor.execute(runnable, 5000, TimeUnit.MILLISECONDS);
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

        private final String createTime;

        private final PluginState state;

        private final long sort;
    }

    public enum PluginState {

        LOADED, LOADING, LOAD_ERROR, UNLOADED, UNLOADING, UNLOAD_ERROR, DELETED, UPDATING
    }
}
