package com.github.linyuzai.plugin.core.autoload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class PluginLocation {

    /**
     * 默认的缓存路径。
     */
    public static String DEFAULT_BASE_PATH = new File(System.getProperty("user.home"), "concept/plugin").getAbsolutePath();

    private final String basePath;

    private final Filter filter;

    public String[] getGroups() {
        File file = new File(basePath);
        File[] files = file.listFiles(File::isDirectory);
        if (files == null) {
            return new String[0];
        }
        return Arrays.stream(files)
                .map(File::getName)
                .toArray(String[]::new);
    }

    public String getGroupPath(String group) {
        return new File(basePath, group).getAbsolutePath();
    }

    public String getGroup(String path) {
        if (path.startsWith(basePath)) {
            File file = new File(path);
            String parent;
            while ((parent = file.getParent()) != null) {
                if (parent.equals(basePath)) {
                    return file.getName();
                }
                file = file.getParentFile();
            }
        }
        return null;
    }

    public String[] getPlugins(String group) {
        File file = new File(basePath, group);
        File[] files = file.listFiles(pathname -> {
            String name = pathname.getName();
            if (pathname.isDirectory() && name.startsWith("_")) {
                return false;
            }
            return filter.filter(group, name);
        });
        if (files == null) {
            return new String[0];
        }
        return Arrays.stream(files)
                .map(File::getName)
                .toArray(String[]::new);
    }

    public String getPluginPath(String group, String name) {
        if (filter.filter(group, name)) {
            return new File(getGroupPath(group), name).getAbsolutePath();
        } else {
            return null;
        }
    }

    public interface Filter {

        boolean filter(String group, String name);
    }
}
