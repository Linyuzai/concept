package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoadEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginAutoReloadEvent;
import com.github.linyuzai.plugin.core.autoload.PluginAutoUnloadEvent;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.event.PluginEventListener;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    public void init() {
        concept.getEventPublisher().register(new PluginAutoLoadListener());
    }

    @GetMapping("load")
    public Response load(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
            String path = location.getLoadedPluginPath(group, name);
            loadingSet.add(path);
            location.load(group, name);
            return null;
        }, () -> "加载失败");
    }

    @GetMapping("unload")
    public Response unload(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
            String path = location.getLoadedPluginPath(group, name);
            unloadingSet.add(path);
            location.unload(group, name);
            return null;
        }, () -> "卸载失败");
    }

    @GetMapping("delete")
    public Response delete(@RequestParam("group") String group, @RequestParam("name") String name) {
        return manage(() -> {
            location.delete(group, name);
            return null;
        }, () -> "删除失败");
    }

    @GetMapping("groups")
    public Response groups() {
        return manage(() -> {
            String[] groups = location.getGroups();
            return Arrays.stream(groups)
                    .map(this::group)
                    .collect(Collectors.toList());
        }, () -> "获取插件分组失败");
    }

    @GetMapping("plugins")
    public Response plugins(@RequestParam("group") String group) {
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
            return list;
        }, () -> "获取插件列表失败");
    }

    public Response manage(Supplier<Object> success, Supplier<String> failure) {
        try {
            return success(success.get());
        } catch (Throwable e) {
            String message = failure.get();
            log.error(message, e);
            return failure(message);
        }
    }

    public ManagedGroup group(String group) {
        return new ManagedGroup(group, loader.getGroupState(group));
    }

    public ManagedPlugin loadedPlugin(String group, String plugin) {
        String path = location.getLoadedPluginPath(group, plugin);
        Plugin get = concept.getRepository().get(path);
        if (get == null) {
            if (loadingSet.contains(path)) {
                return new ManagedPlugin(plugin, "", "loading");
            }
            return new ManagedPlugin(plugin, "", "error");
        } else {
            String name = get.getMetadata().get(Plugin.Metadata.KEY_NAME, "");
            return new ManagedPlugin(plugin, name, "loaded");
        }
    }

    public ManagedPlugin unloadedPlugin(String group, String plugin) {
        String path = location.getLoadedPluginPath(group, plugin);
        if (unloadingSet.contains(path)) {
            return new ManagedPlugin(plugin, "", "unloading");
        }
        return new ManagedPlugin(plugin, "", "unloaded");
    }

    public ManagedPlugin deletedPlugin(String group, String plugin) {
        return new ManagedPlugin(plugin, "", "deleted");
    }

    public Response success(Object data) {
        return new Response(true, data);
    }

    public Response failure(String message) {
        return new Response(false, message);
    }

    public class PluginAutoLoadListener implements PluginEventListener {

        @Override
        public void onEvent(Object event) {
            if (event instanceof PluginAutoLoadEvent) {
                loadingSet.remove(((PluginAutoLoadEvent) event).getPath());
            } else if (event instanceof PluginAutoReloadEvent) {

            } else if (event instanceof PluginAutoUnloadEvent) {
                unloadingSet.remove(((PluginAutoUnloadEvent) event).getPath());
            } else {

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

        private final String state;
    }

    @Data
    @RequiredArgsConstructor
    public static class Response {

        private final boolean success;

        private final Object data;
    }
}
