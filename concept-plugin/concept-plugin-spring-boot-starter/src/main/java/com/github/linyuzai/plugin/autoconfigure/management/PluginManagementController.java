package com.github.linyuzai.plugin.autoconfigure.management;

import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.function.Supplier;

public class PluginManagementController {

    protected final Log log = LogFactory.getLog(PluginManagementController.class);

    @Autowired
    protected PluginConceptProperties properties;

    @Autowired
    protected PluginManagementAuthorizer authorizer;

    @Autowired
    protected PluginManager manager;

    @GetMapping("/setting")
    public Response setting() {
        return response(() -> new Setting(
                properties.getManagement().getGithubCorner(),
                properties.getManagement().getHeader(),
                properties.getManagement().getFooter()), () -> "获取配置");
    }

    @PostMapping("/auth/unlock")
    public Response unlock(@RequestBody Map<String, String> body/*@RequestParam("password") String password*/) {
        String password = body.get("password");
        return response(() -> authorizer.unlock(password), () -> null);
    }

    @PostMapping("/group/add")
    public Response addGroup(@RequestBody Map<String, String> body/*@RequestParam("group") String group*/) {
        String group = body.get("group");
        return response(() -> manager.addGroup(group), () -> "插件分组添加");
    }

    @GetMapping("/group/list")
    public Response listGroup() {
        return response(() -> manager.getGroups(), () -> "插件分组获取");
    }

    @PostMapping("/plugin/load")
    public Response loadPlugin(@RequestBody Map<String, String> body/*@RequestParam("group") String group,
                               @RequestParam("name") String name*/) {
        String group = body.get("group");
        String name = body.get("name");
        return response(() -> manager.loadPlugin(group, name), () -> "插件加载");
    }

    @PostMapping("/plugin/unload")
    public Response unloadPlugin(@RequestBody Map<String, String> body/*@RequestParam("group") String group,
                                 @RequestParam("name") String name*/) {
        String group = body.get("group");
        String name = body.get("name");
        return response(() -> manager.unloadPlugin(group, name), () -> "插件卸载");
    }

    @PostMapping("/plugin/reload")
    public Response reloadPlugin(@RequestBody Map<String, String> body/*@RequestParam("group") String group,
                                 @RequestParam("name") String name*/) {
        String group = body.get("group");
        String name = body.get("name");
        return response(() -> manager.reloadPlugin(group, name), () -> "插件重新加载");
    }

    @GetMapping("/plugin/exist")
    public Response existPlugin(@RequestParam("group") String group,
                                @RequestParam("name") String name) {
        return response(() -> manager.existPlugin(group, name), () -> "插件包重名判断");
    }

    @PostMapping("/plugin/rename")
    public Response renamePlugin(@RequestBody Map<String, String> body/*@RequestParam("group") String group,
                                 @RequestParam("name") String name,
                                 @RequestParam("rename") String rename*/) {
        String group = body.get("group");
        String name = body.get("name");
        String rename = body.get("rename");
        return response(() -> manager.renamePlugin(group, name, rename), () -> "插件包重命名");
    }

    @PostMapping("/plugin/delete")
    public Response deletePlugin(@RequestBody Map<String, String> body/*@RequestParam("group") String group,
                                 @RequestParam("name") String name*/) {
        String group = body.get("group");
        String name = body.get("name");
        return response(() -> manager.deletePlugin(group, name), () -> "插件删除");
    }

    @GetMapping("/plugin/properties")
    public Response getProperties(@RequestParam("group") String group,
                                  @RequestParam("name") String name) {
        return response(() -> manager.getMetadataSummaries(group, name), () -> "查询配置");
    }

    @GetMapping("/plugin/list")
    public Response listPlugin(@RequestParam("group") String group,
                               @RequestParam("deleted") Boolean deleted) {
        return response(() -> {
            if (deleted == Boolean.TRUE) {
                return manager.getDeletedPlugins(group);
            } else {
                return manager.getPlugins(group);
            }
        }, () -> "插件列表获取");
    }

    @PostMapping("/plugin/clear")
    public Response clearDeleted(@RequestBody Map<String, String> body) {
        String group = body.get("group");
        return response(() -> manager.clearDeleted(group), () -> "插件清空");
    }

    protected void updatePlugin(String group, String original, String upload) {
        manager.updatePlugin(group, original, upload);
    }

    protected String uploadPlugin(String group, String name, InputStream is, long length) {
        return manager.uploadPlugin(group, name, is, length);
    }

    protected InputStream downloadPlugin(String group, String name, Boolean deleted) {
        if (deleted == Boolean.TRUE) {
            return manager.downloadDeletedPlugin(group, name);
        } else {
            return manager.downloadPlugin(group, name);
        }
    }

    protected void setDownloadHeaders(HttpHeaders headers, String name) throws UnsupportedEncodingException {
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(name, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    }

    protected Response response(Runnable success, Supplier<String> message) {
        return response(() -> {
            success.run();
            return null;
        }, message);
    }

    protected Response response(Supplier<Object> success, Supplier<String> message) {
        String msg = message.get();
        try {
            Object object = success.get();
            if (object instanceof Response) {
                return (Response) object;
            }
            return success(msg == null ? null : msg + "成功", object);
        } catch (Throwable e) {
            log.error(message.get(), e);
            return failure(msg == null ? null : msg + "失败", e);
        }
    }

    protected Response success(String message, Object data) {
        return new Response(true, message, data);
    }

    protected Response failure(String message, Throwable e) {
        return new Response(false, message, e);
    }

    @Getter
    @RequiredArgsConstructor
    public static class Setting {

        private final PluginConceptProperties.ManagementProperties.GithubCornerProperties githubCorner;

        private final PluginConceptProperties.ManagementProperties.HeaderProperties header;

        private final PluginConceptProperties.ManagementProperties.FooterProperties footer;
    }

    @Data
    @RequiredArgsConstructor
    public static class Response {

        private final boolean success;

        private final String message;

        private final Object data;
    }
}
