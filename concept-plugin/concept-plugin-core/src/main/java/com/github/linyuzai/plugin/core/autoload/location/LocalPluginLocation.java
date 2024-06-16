package com.github.linyuzai.plugin.core.autoload.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public class LocalPluginLocation implements PluginLocation {

    public static final String LOADED = "_loaded";

    public static final String UNLOADED = "_unloaded";

    public static final String DELETED = "_deleted";

    private final String basePath;

    private final PluginLocation.Filter filter;

    @Override
    public String[] getGroups() {
        File file = check(new File(basePath));
        File[] files = file.listFiles(File::isDirectory);
        if (files == null) {
            return new String[0];
        }
        return Arrays.stream(files)
                .map(File::getName)
                .toArray(String[]::new);
    }

    @Override
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

    @Override
    public String getLoadedPath(String group) {
        return getPluginDirectory(group, LOADED).getAbsolutePath();
    }

    @Override
    public String[] getLoadedPlugins(String group) {
        return getPlugins(group, LOADED);
    }

    @Override
    public String getLoadedPluginPath(String group, String name) {
        return getPluginPath(group, name, LOADED);
    }

    @Override
    public String getUnloadedPath(String group) {
        return getPluginDirectory(group, UNLOADED).getAbsolutePath();
    }

    @Override
    public String[] getUnloadedPlugins(String group) {
        return getPlugins(group, UNLOADED);
    }

    @Override
    public String getUnloadedPluginPath(String group, String name) {
        return getPluginPath(group, name, UNLOADED);
    }

    @Override
    public String getDeletedPath(String group) {
        return getPluginDirectory(group, DELETED).getAbsolutePath();
    }

    @Override
    public String[] getDeletedPlugins(String group) {
        return getPlugins(group, DELETED);
    }

    @Override
    public String getDeletedPluginPath(String group, String name) {
        return getPluginPath(group, name, DELETED);
    }

    @Override
    public void load(String group, String name) {
        move(group, name, UNLOADED, LOADED);
    }

    @Override
    public void unload(String group, String name) {
        move(group, name, LOADED, UNLOADED);
    }

    @Override
    public void delete(String group, String name) {
        move(group, name, UNLOADED, DELETED);
    }

    protected boolean move(String group, String name, String from, String to) {
        String fromPath = getPluginPath(group, name, from);
        if (fromPath == null) {
            throw new IllegalArgumentException(name + " is not a Plugin");
        }
        File fromFile = new File(fromPath);
        if (!fromFile.exists()) {
            throw new IllegalArgumentException(name + " not existed");
        }
        String toPath = getPluginPath(group, name, to);
        File toFile = getFileAutoName(new File(toPath));
        return fromFile.renameTo(toFile);
    }

    protected File getGroupDirectory(String group) {
        return check(new File(basePath, group));
    }

    protected File getPluginDirectory(String group, String type) {
        return check(new File(getGroupDirectory(group), type));
    }

    protected String getPluginPath(String group, String name, String type) {
        if (filter.filter(group, name)) {
            return new File(getPluginDirectory(group, type), name).getAbsolutePath();
        } else {
            return null;
        }
    }

    protected String[] getPlugins(String group, String type) {
        File directory = getPluginDirectory(group, type);
        File[] files = directory.listFiles(pathname -> {
            String name = pathname.getName();
            return filter.filter(group, name);
        });
        if (files == null) {
            return new String[0];
        }
        return Arrays.stream(files)
                .map(File::getName)
                .toArray(String[]::new);
    }

    protected File check(File file) {
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        return file;
    }

    public static File getFileAutoName(File file) {
        int i = 1;
        while (file.exists()) {
            String path = file.getAbsolutePath();
            int index = path.lastIndexOf(".");
            if (index == -1) {
                file = new File(path + i);
            } else {
                file = new File(path.substring(0, index) + "(" + i + ")" + path.substring(index));
            }
            i++;
        }
        return file;
    }
}
