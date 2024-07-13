package com.github.linyuzai.plugin.core.autoload.location;

import lombok.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

/**
 * 本地插件位置
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocalPluginLocation implements PluginLocation {

    /**
     * 默认的缓存路径 {user.home}/concept/plugin
     */
    public static final String DEFAULT_BASE_PATH = new File(System.getProperty("user.home"), "concept/plugin").getAbsolutePath();

    /**
     * 需要加载的插件目录名
     */
    public static final String LOADED = "_loaded";

    /**
     * 不需要加载的插件目录名
     */
    public static final String UNLOADED = "_unloaded";

    /**
     * 删除的插件目录名
     */
    public static final String DELETED = "_deleted";

    /**
     * 基础目录
     */
    private String basePath = DEFAULT_BASE_PATH;

    private PluginLocation.Filter filter;

    /**
     * 基础路径下的子目录为分组
     */
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

    /**
     * 当前目录的上级目录是基础目录时，当前目录为分组
     */
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

    /**
     * {basePath}/{group}/_loaded
     */
    @Override
    public String getLoadedBasePath(String group) {
        return getPluginDirectory(group, LOADED).getAbsolutePath();
    }

    @Override
    public String[] getLoadedPlugins(String group) {
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

    /**
     * {basePath}/{group}/_unloaded
     */
    @Override
    public String getUnloadedBasePath(String group) {
        return getPluginDirectory(group, UNLOADED).getAbsolutePath();
    }

    @Override
    public String[] getUnloadedPlugins(String group) {
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

    /**
     * {basePath}/{group}/_deleted
     */
    @Override
    public String getDeletedBasePath(String group) {
        return getPluginDirectory(group, DELETED).getAbsolutePath();
    }

    @Override
    public String[] getDeletedPlugins(String group) {
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

    /**
     * 文件大小
     */
    @Override
    public long getSize(String path) {
        try {
            return new File(path).length();
        } catch (Throwable e) {
            return -1;
        }
    }

    /**
     * 文件创建时间
     */
    @Override
    public long getCreationTimestamp(String path) {
        try {
            BasicFileAttributes attr = Files.readAttributes(Paths.get(path), BasicFileAttributes.class);
            return attr.creationTime().toMillis();
        } catch (Throwable e) {
            return -1;
        }
    }

    /**
     * 将插件文件从不需要加载的目录移动到需要加载的目录触发插件加载
     */
    @Override
    public void load(String group, String name) {
        move(group, name, UNLOADED, LOADED);
    }

    /**
     * 将插件文件从需要加载的目录移动到不需要加载的目录触发插件卸载
     */
    @Override
    public void unload(String group, String name) {
        move(group, name, LOADED, UNLOADED);
    }

    /**
     * 将插件文件移动到删除的文件目录
     */
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
            return filter == null || filter.filter(group, name);
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

    /**
     * 如果文件存在则顺序添加后缀
     */
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
