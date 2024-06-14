package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PluginManagementController {

    protected final Log log = LogFactory.getLog("PluginManagement");

    @Autowired
    protected PluginLocation location;

    @Autowired
    protected PluginAutoLoader loader;

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
        Plugin get = loader.getPlugin(group, plugin);
        if (get == null) {
            return new ManagedPlugin(plugin, "", "error");
        } else {
            String name = get.getMetadata().get(Plugin.Metadata.KEY_NAME, "");
            return new ManagedPlugin(plugin, name, "loaded");
        }
    }

    public ManagedPlugin unloadedPlugin(String group, String plugin) {
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
