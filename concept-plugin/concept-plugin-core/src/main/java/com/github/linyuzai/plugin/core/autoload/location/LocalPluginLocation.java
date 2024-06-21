package com.github.linyuzai.plugin.core.autoload.location;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
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
    public InputStream getLoadedPluginInputStream(String group, String name) throws IOException {
        File file = new File(getLoadedPluginPath(group, name));
        return Files.newInputStream(file.toPath());
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
    public InputStream getUnloadedPluginInputStream(String group, String name) throws IOException {
        File file = new File(getUnloadedPluginPath(group, name));
        return Files.newInputStream(file.toPath());
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
    public InputStream getDeletedPluginInputStream(String group, String name) throws IOException {
        File file = new File(getDeletedPluginPath(group, name));
        return Files.newInputStream(file.toPath());
    }

    @Override
    public long getSize(String path) {
        try {
            return new File(path).length();
        } catch (Throwable e) {
            return -1;
        }
    }

    @Override
    public long getCreationTimestamp(String path) {
        try {
            BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
            return attr.creationTime().toMillis();
        } catch (Throwable e) {
            return -1;
        }
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
        try {
            move(group, name, UNLOADED, DELETED);
        } catch (Throwable e) {
            move(group, name, LOADED, DELETED);
        }
    }

    protected File getExistFile(String group, String name) {
        String loadPath = getLoadedPluginPath(group, name);
        File loadFile = new File(loadPath);
        if (loadFile.exists()) {
            return loadFile;
        }
        String unloadPath = getUnloadedPluginPath(group, name);
        File unloadFile = new File(unloadPath);
        if (unloadFile.exists()) {
            return unloadFile;
        }
        return null;
    }

    @Override
    public boolean exist(String group, String name) {
        return getExistFile(group, name) != null;
    }

    @Override
    public void rename(String group, String name, String rename) {
        if (exist(group, rename)) {
            throw new IllegalArgumentException("File existed");
        }
        String renamePath = getUnloadedPluginPath(group, rename);
        File renameFile = new File(renamePath);
        File from;
        File fromUnloaded = new File(getUnloadedPluginPath(group, name));
        if (fromUnloaded.exists()) {
            from = fromUnloaded;
        } else {
            File fromLoaded = new File(getLoadedPluginPath(group, name));
            if (fromLoaded.exists()) {
                from = fromLoaded;
            } else {
                return;
            }
        }
        boolean renameTo = from.renameTo(renameFile);
    }

    protected boolean move(String group, String name, String from, String to) {
        File fromFile = new File(getPluginPath(group, name, from));
        if (!fromFile.exists()) {
            throw new IllegalArgumentException(name + " not existed");
        }
        String toPath = getPluginPath(group, name, to);
        File toFile = getFileAutoName(new File(toPath));
        return fromFile.renameTo(toFile);
    }

    protected File getGroupDirectory(String group) {
        return check(new File(basePath, group.trim()));
    }

    protected File getPluginDirectory(String group, String type) {
        return check(new File(getGroupDirectory(group), type));
    }

    protected String getPluginPath(String group, String name, String type) {
        return new File(getPluginDirectory(group, type), name.trim()).getAbsolutePath();
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
