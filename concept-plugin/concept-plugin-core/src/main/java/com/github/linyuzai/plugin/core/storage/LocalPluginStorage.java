package com.github.linyuzai.plugin.core.storage;

import lombok.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 本地插件位置
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalPluginStorage implements PluginStorage {

    /**
     * 默认的缓存路径 {user.home}/concept/plugin
     */
    public static final String DEFAULT_LOCATION = new File(System.getProperty("user.home"), "concept/plugin").getAbsolutePath();

    /**
     * 基础目录
     */
    private String location = DEFAULT_LOCATION;

    private PluginStorage.Filter filter;

    /**
     * 基础路径下的子目录为分组
     */
    @Override
    public List<String> getGroups() {
        File file = check(new File(location));
        File[] files = file.listFiles(File::isDirectory);
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    @Override
    public void addGroup(String group) {
        getPluginDirectory(group, LOADED);
    }

    @Override
    public List<String> getLoadedPlugins(String group) {
        return getPlugins(group, LOADED);
    }

    /**
     * {basePath}/{group}/_loaded/{name}
     */
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
    public List<String> getUnloadedPlugins(String group) {
        return getPlugins(group, UNLOADED);
    }

    /**
     * {basePath}/{group}/_unloaded/{name}
     */
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
    public List<String> getDeletedPlugins(String group) {
        return getPlugins(group, DELETED);
    }

    /**
     * {basePath}/{group}/_deleted/{name}
     */
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
    public PluginDefinition getPluginDefinition(String path) {
        return new LocalPluginDefinition(path);
    }

    @Override
    public List<PluginDefinition> getPluginDefinitions(Collection<? extends String> paths) {
        return paths.stream().map(this::getPluginDefinition).collect(Collectors.toList());
    }

    @Override
    public String uploadPlugin(String group, String name, InputStream is, long length) throws IOException {
        String loadedPath = getLoadedPluginPath(group, name);
        File file = new File(generateFileName(loadedPath));
        String unloadedPath = getUnloadedPluginPath(group, file.getName());
        File generate = new File(generateFileName(unloadedPath));
        try (FileOutputStream out = new FileOutputStream(generate)) {
            int bytesRead;
            for (byte[] buffer = new byte[4096]; (bytesRead = is.read(buffer)) != -1; ) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
        return generate.getName();
    }

    /**
     * 将插件文件从不需要加载的目录移动到需要加载的目录触发插件加载
     */
    @Override
    public void loadPlugin(String group, String name) {
        move(group, name, UNLOADED, LOADED);
    }

    /**
     * 将插件文件从需要加载的目录移动到不需要加载的目录触发插件卸载
     */
    @Override
    public void unloadPlugin(String group, String name) {
        move(group, name, LOADED, UNLOADED);
    }

    /**
     * 将插件文件移动到删除的文件目录
     */
    @Override
    public void deletePlugin(String group, String name) {
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
    public boolean existPlugin(String group, String name) {
        return getExistFile(group, name) != null;
    }

    @Override
    public void renamePlugin(String group, String name, String rename) {
        if (existPlugin(group, rename)) {
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
        File toFile = new File(generateFileName(toPath));
        return fromFile.renameTo(toFile);
    }

    protected File getGroupDirectory(String group) {
        return check(new File(location, group.trim()));
    }

    protected File getPluginDirectory(String group, String type) {
        return check(new File(getGroupDirectory(group), type));
    }

    protected String getPluginPath(String group, String name, String type) {
        return new File(getPluginDirectory(group, type), name.trim()).getAbsolutePath();
    }

    protected List<String> getPlugins(String group, String type) {
        File directory = getPluginDirectory(group, type);
        File[] files = directory.listFiles(pathname -> {
            String name = pathname.getName();
            return filter == null || filter.filter(group, name);
        });
        if (files == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toList());
    }

    protected File check(File file) {
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
        }
        return file;
    }

    /**
     * 如果文件存在则顺序添加后缀
     */
    public static String generateFileName(String path) {
        int i = 1;
        String tryPath = path;
        while (new File(tryPath).exists()) {
            int index = tryPath.lastIndexOf(".");
            if (index == -1) {
                tryPath = tryPath + i;
            } else {
                tryPath = tryPath.substring(0, index) + "(" + i + ")" + tryPath.substring(index);
            }
            i++;
        }
        return tryPath;
    }

    @Getter
    @RequiredArgsConstructor
    public static class LocalPluginDefinition implements PluginDefinition {

        private final String path;

        @Override
        public long getSize() {
            return new File(path).length();
        }

        @Override
        public long getCreateTime() {
            try {
                BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
                return attr.creationTime().toMillis();
            } catch (Throwable e) {
                return -1;
            }
        }

        @Override
        public Object getVersion() {
            return new File(path).lastModified();
        }

        @Override
        public Object getSource() {
            return path;
        }
    }
}
