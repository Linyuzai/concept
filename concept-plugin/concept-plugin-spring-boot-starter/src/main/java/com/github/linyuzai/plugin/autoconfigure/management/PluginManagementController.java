package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginLocation;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/concept-plugin/management")
public class PluginManagementController {

    private final Log log = LogFactory.getLog("PluginManagement");

    @Autowired
    private PluginLocation location;

    @Autowired
    private PluginAutoLoader loader;

    @GetMapping("groups")
    public Response groups() {
        return manage(() -> {
            String[] groups = location.getGroups();
            return Arrays.stream(groups)
                    .map(it -> new Group(it, loader.getGroupState(it)))
                    .collect(Collectors.toList());
        }, () -> "获取插件分组失败");
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

    public Response success(Object data) {
        return new Response(true, data);
    }

    public Response failure(String message) {
        return new Response(false, message);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Group {

        private final String name;

        private final boolean state;
    }

    @Data
    @RequiredArgsConstructor
    public static class Response {

        private final boolean success;

        private final Object data;
    }
}
