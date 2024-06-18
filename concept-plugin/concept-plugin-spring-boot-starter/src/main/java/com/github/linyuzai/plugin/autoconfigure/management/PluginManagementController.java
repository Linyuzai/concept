package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.core.autoload.*;
import com.github.linyuzai.plugin.core.autoload.location.LocalPluginLocation;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import com.github.linyuzai.plugin.core.executer.PluginExecutor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PluginManagementController {

    protected final Log log = LogFactory.getLog("PluginManagement");

    protected final Set<String> loadingSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    protected final Set<String> unloadingSet = Collections.newSetFromMap(new ConcurrentHashMap<>());

    @Autowired
    protected PluginConcept concept;

    @Autowired
    protected PluginLocation location;

    @Autowired
    protected PluginAutoLoader loader;

    @Autowired
    protected PluginExecutor executor;

    protected final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/group/add")
    public Response addGroup(@RequestParam("group") String group) {
        return manage(() -> {
            loader.addGroup(group);
            return null;
        }, () -> "插件分组添加");
    }

    @GetMapping("/group/list")
    public Response listGroup() {
        return manage(() -> {
            String[] groups = location.getGroups();
            return Arrays.stream(groups)
                    .map(this::group)
                    .collect(Collectors.toList());
        }, () -> "插件分组获取");
    }

    @GetMapping("/plugin/load")
    public Response loadPlugin(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
            String path = location.getLoadedPluginPath(group, name);
            loadingSet.add(path);
            try {
                location.load(group, name);
            } catch (Throwable e) {
                executor.execute(() -> {
                    try {
                        concept.load(path);
                    } catch (Throwable e1) {
                        log.error("Load plugin error: " + path, e1);
                    } finally {
                        loadingSet.remove(path);
                    }
                }, 1000, TimeUnit.MILLISECONDS);
            }
            return null;
        }, () -> "插件加载");
    }

    @GetMapping("/plugin/unload")
    public Response unloadPlugin(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
            String path = location.getLoadedPluginPath(group, name);
            unloadingSet.add(path);
            try {
                location.unload(group, name);
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
            return null;
        }, () -> "插件卸载");
    }

    @GetMapping("/plugin/reload")
    public Response reloadPlugin(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
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
                    concept.load(path);
                } catch (Throwable e) {
                    log.error("Reload plugin error: " + path, e);
                } finally {
                    loadingSet.remove(path);
                }
            }, 1000, TimeUnit.MILLISECONDS);
            return null;
        }, () -> "插件重新加载");
    }

    @GetMapping("/plugin/delete")
    public Response deletePlugin(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
            try {
                location.delete(group, name);
            } catch (Throwable ignore) {
            }
            return null;
        }, () -> "插件删除");
    }

    @GetMapping("/plugin/list")
    public Response listPlugin(@RequestParam("group") String group) {
        return manage(() -> {
            List<ManagedPlugin> list = new ArrayList<>();
            String[] loaded = location.getLoadedPlugins(group);
            for (String load : loaded) {
                list.add(loadedPlugin(group, load));
            }
            String[] unloaded = location.getUnloadedPlugins(group);
            for (String unload : unloaded) {
                list.add(unloadedPlugin(group, unload));
            }
            list.sort((o1, o2) -> Long.compare(o2.sort, o1.sort));
            return list;
        }, () -> "插件列表获取");
    }

    public Response manage(Supplier<Object> success, Supplier<String> message) {
        try {
            return success(message.get() + "成功", success.get());
        } catch (Throwable e) {
            log.error(message.get(), e);
            return failure(message.get() + "失败", e);
        }
    }

    public ManagedGroup group(String group) {
        return new ManagedGroup(group, loader.getGroupState(group));
    }

    public ManagedPlugin loadedPlugin(String group, String plugin) {
        String path = location.getLoadedPluginPath(group, plugin);
        long timestamp = location.getCreationTimestamp(path);
        long size = location.getSize(path);
        String name;
        ManagedPlugin.State state;
        if (loadingSet.contains(path) || concept.isLoading(path)) {
            name = null;
            state = ManagedPlugin.State.LOADING;
        } else {
            Plugin get = concept.getRepository().get(path);
            if (get == null) {
                name = null;
                state = ManagedPlugin.State.LOAD_ERROR;
            } else {
                name = get.getMetadata().get(Plugin.Metadata.PropertyKey.NAME, "");
                state = ManagedPlugin.State.LOADED;
            }
        }
        return new ManagedPlugin(plugin, name, formatSize(size), formatTime(timestamp), state, timestamp);
    }

    public ManagedPlugin unloadedPlugin(String group, String plugin) {
        String unloadPath = location.getUnloadedPluginPath(group, plugin);
        long timestamp = location.getCreationTimestamp(unloadPath);
        long size = location.getSize(unloadPath);
        String name;
        ManagedPlugin.State state;
        String path = location.getLoadedPluginPath(group, plugin);
        if (unloadingSet.contains(path) || concept.isUnloading(path)) {
            name = null;
            state = ManagedPlugin.State.UNLOADING;
        } else {
            Plugin get = concept.getRepository().get(path);
            if (get == null) {
                name = null;
                state = ManagedPlugin.State.UNLOADED;
            } else {
                name = get.getMetadata().get(Plugin.Metadata.PropertyKey.NAME, "");
                state = ManagedPlugin.State.UNLOAD_ERROR;
            }
        }
        return new ManagedPlugin(plugin, name, formatSize(size), formatTime(timestamp), state, timestamp);
    }

    public Response success(String message, Object data) {
        return new Response(true, message, data);
    }

    public Response failure(String message, Throwable e) {
        return new Response(false, message, e);
    }

    public void init() {
        concept.getEventPublisher().register(new PluginAutoLoadListener());
    }

    protected File getFinalFile(String group, String name) {
        String loadedPath = location.getLoadedPluginPath(group, name);
        File file = LocalPluginLocation.getFileAutoName(new File(loadedPath));
        String unloadedPath = location.getUnloadedPluginPath(group, file.getName());
        return LocalPluginLocation.getFileAutoName(new File(unloadedPath));
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
    public static class ManagedGroup {

        private final String name;

        private final Boolean state;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ManagedPlugin {

        private final String plugin;

        private final String name;

        private final String size;

        private final String creationTime;

        private final State state;

        private final long sort;

        public enum State {

            LOADED, LOADING, LOAD_ERROR, UNLOADED, UNLOADING, UNLOAD_ERROR, DELETED
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class Response {

        private final boolean success;

        private final String message;

        private final Object data;
    }
}
