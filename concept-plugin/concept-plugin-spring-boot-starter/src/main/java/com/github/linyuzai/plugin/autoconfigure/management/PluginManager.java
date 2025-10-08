package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.autoload.*;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.event.*;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import com.github.linyuzai.plugin.core.metadata.PluginMetadata;
import com.github.linyuzai.plugin.core.storage.PluginStorage;
import com.github.linyuzai.plugin.core.sync.SyncSupport;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PluginManager extends SyncSupport {

    protected final Log log = LogFactory.getLog(PluginManager.class);

    protected final Set<String> loadingSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    protected final Set<String> unloadingSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    protected final Set<String> updatingSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    protected final Map<String, PluginTree> treeMap = new ConcurrentHashMap<>();

    protected final Map<String, Throwable> errorMap = new ConcurrentHashMap<>();

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

    public void addGroup(String group) {
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

    public void loadPlugin(String group, String name) {
        PluginDefinition definition = storage.getPluginDefinition(PluginStorage.LOADED, group, name);
        String path = definition.getPath();
        resetTreeAndError(path);
        syncWrite(() -> {
            loadingSet.add(path);
            try {
                storage.loadPlugin(group, name);
            } catch (Throwable e) {
                execute(() -> {
                    try {
                        concept.load(definition);
                    } catch (Throwable e1) {
                        log.error("Load plugin error: " + path, e1);
                    } finally {
                        loadingSet.remove(path);
                    }
                });
            }
        });
    }

    public void unloadPlugin(String group, String name) {
        PluginDefinition definition = storage.getPluginDefinition(PluginStorage.LOADED, group, name);
        String path = definition.getPath();
        resetTreeAndError(path);
        syncWrite(() -> {
            unloadingSet.add(path);
            try {
                storage.unloadPlugin(group, name);
            } catch (Throwable e) {
                execute(() -> {
                    try {
                        concept.unload(definition);
                    } catch (Throwable e1) {
                        log.error("Unload plugin error: " + path, e1);
                    } finally {
                        unloadingSet.remove(path);
                    }
                });
            }
        });
    }

    public void reloadPlugin(String group, String name) {
        PluginDefinition definition = storage.getPluginDefinition(PluginStorage.LOADED, group, name);
        String path = definition.getPath();
        resetTreeAndError(path);
        syncWrite(() -> {
            loadingSet.add(path);
            try {
                concept.unload(definition);
            } catch (Throwable e) {
                loadingSet.remove(path);
                throw e;
            }
            execute(() -> {
                try {
                    concept.load(definition);
                } catch (Throwable e) {
                    log.error("Reload plugin error: " + path, e);
                } finally {
                    loadingSet.remove(path);
                }
            });
        });
    }

    public void renamePlugin(String group, String name, String rename) {
        storage.renamePlugin(group, name, rename);
        resetTreeAndError(storage.getPluginDefinition(PluginStorage.LOADED, group, name).getPath());
        resetTreeAndError(storage.getPluginDefinition(PluginStorage.UNLOADED, group, name).getPath());
    }

    public void deletePlugin(String group, String name) {
        storage.deletePlugin(group, name);
        resetTreeAndError(storage.getPluginDefinition(PluginStorage.LOADED, group, name).getPath());
        resetTreeAndError(storage.getPluginDefinition(PluginStorage.UNLOADED, group, name).getPath());
    }

    public PluginMetadata getMetadata(String group, String name) {
        PluginDefinition definition = storage.getPluginDefinition(PluginStorage.LOADED, group, name);
        Plugin plugin = concept.getRepository().get(definition);
        if (plugin == null) {
            PluginDefinition unload = storage.getPluginDefinition(PluginStorage.UNLOADED, group, name);
            try {
                return concept.createMetadata(unload, concept.createContext());
            } catch (Throwable e) {
                PluginDefinition loaded = storage.getPluginDefinition(PluginStorage.LOADED, group, name);
                return concept.createMetadata(loaded, concept.createContext());
            }
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

    public void updatePlugin(String group, String original, String upload) {
        String newPath = storage.getPluginDefinition(PluginStorage.LOADED, group, upload).getPath();
        String oldPath = storage.getPluginDefinition(PluginStorage.LOADED, group, original).getPath();
        resetTreeAndError(oldPath);
        syncWrite(() -> {
            loadingSet.add(newPath);
            updatingSet.add(oldPath);
            PluginEventListener listener = new PluginEventListener() {

                @Override
                public void onEvent(Object event) {
                    if (event instanceof PluginAutoEvent) {
                        String path = ((PluginAutoEvent) event).getDefinition().getPath();
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
        });
    }

    public String uploadPlugin(String group, String name, InputStream is, long length) {
        return storage.uploadPlugin(group, name, is, length);
    }

    public InputStream downloadPlugin(String group, String name) {
        try {
            return storage.getPluginDefinition(PluginStorage.LOADED, group, name).getInputStream();
        } catch (Throwable e) {
            return storage.getPluginDefinition(PluginStorage.UNLOADED, group, name).getInputStream();
        }
    }

    public InputStream downloadDeletedPlugin(String group, String name) {
        return storage.getPluginDefinition(PluginStorage.DELETED, group, name).getInputStream();
    }

    public void clearDeleted(String group) {
        storage.clearDeleted(group);
    }

    protected List<PluginSummary> getLoadedPluginSummaries(String group) {
        return syncRead(() -> storage.getPluginDefinitions(PluginStorage.LOADED, group).map(definition -> {
            String name = definition.getName();
            String path = definition.getPath();
            long timestamp = definition.getCreateTime();
            long size = definition.getSize();
            PluginState state;
            if (loadingSet.contains(path) || concept.isLoading(definition)) {
                state = PluginState.LOADING;
            } else {
                Plugin get = concept.getRepository().get(definition);
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
            return new PluginSummary(name, formatSize(size),
                    formatTime(timestamp), state, timestamp);
        }).collect(Collectors.toList()));
    }

    protected List<PluginSummary> getUnloadedPluginSummaries(String group) {
        return syncRead(() -> storage.getPluginDefinitions(PluginStorage.UNLOADED, group).map(definition -> {
            String name = definition.getName();
            long timestamp = definition.getCreateTime();
            long size = definition.getSize();
            PluginState state;
            PluginDefinition def = storage.getPluginDefinition(PluginStorage.LOADED, group, name);
            String path = def.getPath();
            if (unloadingSet.contains(path) || concept.isUnloading(def)) {
                state = PluginState.UNLOADING;
            } else {
                Plugin get = concept.getRepository().get(def);
                if (get == null) {
                    state = PluginState.UNLOADED;
                } else {
                    state = PluginState.UNLOAD_ERROR;
                }
            }
            return new PluginSummary(name, formatSize(size),
                    formatTime(timestamp), state, timestamp);
        }).collect(Collectors.toList()));
    }

    protected List<PluginSummary> getDeletedPluginSummaries(String group) {
        return storage.getPluginDefinitions(PluginStorage.DELETED, group).map(definition -> {
            String name = definition.getName();
            long timestamp = definition.getCreateTime();
            long size = definition.getSize();
            PluginState state = PluginState.DELETED;
            return new PluginSummary(name, formatSize(size),
                    formatTime(timestamp), state, timestamp);
        }).collect(Collectors.toList());
    }

    public void initialize() {
        concept.getEventPublisher().register(new PluginAutoLoadListener());
    }

    protected void resetTreeAndError(String path) {
        treeMap.remove(path);
        errorMap.remove(path);
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
        executor.execute(runnable, 1000, TimeUnit.MILLISECONDS);
    }

    public class PluginAutoLoadListener implements PluginEventListener {

        @Override
        public void onEvent(Object event) {
            if (event instanceof PluginLoadedEvent ||
                    event instanceof PluginLoadErrorEvent) {
                PluginContext context = ((PluginContextEvent) event).getContext();
                PluginTree tree = context.get(PluginTree.class);
                if (tree != null) {
                    treeMap.put(context.getPlugin().getDefinition().getPath(), tree);
                }
            }
            if (event instanceof PluginAutoEvent) {
                String path = ((PluginAutoEvent) event).getDefinition().getPath();
                if (event instanceof PluginErrorEvent) {
                    Throwable error = ((PluginErrorEvent) event).getError();
                    if (error != null) {
                        errorMap.put(path, error);
                    }
                }
                if (event instanceof PluginAutoLoadEvent ||
                        event instanceof PluginAutoLoadErrorEvent) {
                    syncWrite(() -> loadingSet.remove(path));
                } else if (event instanceof PluginAutoUnloadEvent ||
                        event instanceof PluginAutoUnloadErrorEvent) {
                    syncWrite(() -> unloadingSet.remove(path));
                }
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class GroupSummary {

        private final Integer index;

        private final String name;
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
