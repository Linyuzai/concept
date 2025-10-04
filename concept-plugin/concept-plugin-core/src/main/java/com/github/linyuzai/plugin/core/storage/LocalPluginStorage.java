package com.github.linyuzai.plugin.core.storage;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import lombok.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 本地插件位置
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalPluginStorage extends AbstractPluginStorage {

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
        getPluginDirectory(LOADED, group);
    }

    @Override
    public PluginDefinition getPluginDefinition(String type, String group, String name) {
        return new LocalPluginDefinition(getPluginPath(type, group, name), name);
    }

    @Override
    public Stream<PluginDefinition> getPluginDefinitions(String type, String group) {
        return getPlugins(type, group).stream().map(it -> getPluginDefinition(type, group, it));
    }

    @SneakyThrows
    @Override
    public String uploadPlugin(String group, String name, InputStream is, long length) {
        String loadedPath = getPluginDefinition(LOADED, group, name).getPath();
        File file = new File(generateName(loadedPath));
        String unloadedPath = getPluginDefinition(UNLOADED, group, file.getName()).getPath();
        File generate = new File(generateName(unloadedPath));
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
        move(group, name, UNLOADED, LOADED, false);
    }

    /**
     * 将插件文件从需要加载的目录移动到不需要加载的目录触发插件卸载
     */
    @Override
    public void unloadPlugin(String group, String name) {
        move(group, name, LOADED, UNLOADED, false);
    }

    /**
     * 将插件文件移动到删除的文件目录
     */
    @Override
    public void deletePlugin(String group, String name) {
        try {
            move(group, name, UNLOADED, DELETED, true);
        } catch (Throwable e) {
            move(group, name, LOADED, DELETED, true);
        }
    }

    protected File getExistFile(String group, String name) {
        String loadPath = getPluginDefinition(LOADED, group, name).getPath();
        File loadFile = new File(loadPath);
        if (loadFile.exists()) {
            return loadFile;
        }
        String unloadPath = getPluginDefinition(UNLOADED, group, name).getPath();
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
            throw new IllegalArgumentException("Name existed");
        }
        String renamePath = getPluginDefinition(UNLOADED, group, rename).getPath();
        File renameFile = new File(renamePath);
        File from;
        File fromUnloaded = new File(getPluginDefinition(UNLOADED, group, name).getPath());
        if (fromUnloaded.exists()) {
            from = fromUnloaded;
        } else {
            File fromLoaded = new File(getPluginDefinition(LOADED, group, name).getPath());
            if (fromLoaded.exists()) {
                from = fromLoaded;
            } else {
                return;
            }
        }
        boolean renameTo = from.renameTo(renameFile);
    }

    @Override
    public void clearDeleted(String group) {
        File directory = getGroupDirectory(group);
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.exists()) {
                boolean delete = file.delete();
            }
        }
    }

    @SneakyThrows
    protected void move(String group, String name, String from, String to, boolean deleted) {
        File fromFile = new File(getPluginPath(from, group, name));
        if (!fromFile.exists()) {
            throw new IllegalArgumentException(name + " not existed");
        }
        String toPath = getPluginPath(to, group, name);
        File toFile = new File(deleted ? generateDeletedName(toPath) : generateName(toPath));
        //return fromFile.renameTo(toFile);
        // 移动失败会抛出异常
        Files.move(fromFile.toPath(), toFile.toPath());
    }

    protected File getGroupDirectory(String group) {
        return check(new File(location, group));
    }

    protected File getPluginDirectory(String type, String group) {
        return check(new File(getGroupDirectory(group), type));
    }

    protected String getPluginPath(String type, String group, String name) {
        return new File(getPluginDirectory(type, group), name).getAbsolutePath();
    }

    protected String generateName(String path) {
        return generateName(path, p -> new File(p).exists());
    }

    protected String generateDeletedName(String path) {
        return generateDeletedName(path, p -> new File(p).exists());
    }

    protected List<String> getPlugins(String type, String group) {
        File directory = getPluginDirectory(type, group);
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

    @Getter
    @RequiredArgsConstructor
    public static class LocalPluginDefinition implements PluginDefinition {

        private final String path;

        private final String name;

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

        @SneakyThrows
        @Override
        public InputStream getInputStream() {
            return Files.newInputStream(Paths.get(path));
        }
    }
}
