package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 基于 {@link WatchService} 的插件文件自动加载器
 */
@Getter
public class WatchServicePluginAutoLoader implements PluginAutoLoader {

    /**
     * 执行线程池
     */
    private final ExecutorService executor;

    /**
     * 插件位置
     */
    private final PluginLocation[] locations;

    private final Consumer<String> createConsumer;

    private final Consumer<String> modifyConsumer;

    private final Consumer<String> deleteConsumer;

    /**
     * 异常处理
     */
    private final Consumer<Throwable> errorConsumer;

    /**
     * 在 {@link #start()} 时是否触发一次加载
     */
    private final WatchEvent.Kind<?> notifyOnStart;

    private WatchService watchService;

    /**
     * 需要创建回调的路径
     */
    private final Set<String> notifyCreate = new HashSet<>();

    /**
     * 需要修改回调的路径
     */
    private final Set<String> notifyModify = new HashSet<>();

    /**
     * 需要删除回调的路径
     */
    private final Set<String> notifyDelete = new HashSet<>();

    private boolean running = false;

    private WatchServicePluginAutoLoader(ExecutorService executor,
                                         Consumer<String> createConsumer,
                                         Consumer<String> modifyConsumer,
                                         Consumer<String> deleteConsumer,
                                         Consumer<Throwable> errorConsumer,
                                         WatchEvent.Kind<?> notifyOnStart,
                                         PluginLocation... locations) {
        this.executor = executor;
        this.locations = locations;
        this.createConsumer = createConsumer;
        this.modifyConsumer = modifyConsumer;
        this.deleteConsumer = deleteConsumer;
        this.errorConsumer = errorConsumer;
        this.notifyOnStart = notifyOnStart;
        for (PluginLocation location : this.locations) {
            if (location.isNotifyCreate()) {
                notifyCreate.add(location.getPath());
            }
            if (location.isNotifyModify()) {
                notifyModify.add(location.getPath());
            }
            if (location.isNotifyDelete()) {
                notifyDelete.add(location.getPath());
            }
        }
    }

    /**
     * 开始监听
     */
    @Override
    public synchronized void start() {
        //如果已经开始，直接忽略
        if (running) {
            return;
        }
        running = true;
        //开始就执行一次回调
        if (notifyOnStart == StandardWatchEventKinds.ENTRY_CREATE) {
            notifyOnStart0(createConsumer);
        } else if (notifyOnStart == StandardWatchEventKinds.ENTRY_MODIFY) {
            notifyOnStart0(modifyConsumer);
        } else if (notifyOnStart == StandardWatchEventKinds.ENTRY_DELETE) {
            notifyOnStart0(deleteConsumer);
        }

        //如果没有指定执行器，直接新建一个线程
        if (executor == null) {
            new Thread(this::listen).start();
        } else {
            executor.execute(this::listen);
        }
    }

    private void notifyOnStart0(Consumer<String> consumer) {
        if (consumer == null) {
            return;
        }
        for (PluginLocation location : locations) {
            File[] list = new File(location.getPath()).listFiles();
            if (list == null) {
                continue;
            }
            if (location.getFilter() == null) {
                Arrays.stream(list)
                        .map(File::getAbsolutePath)
                        .forEach(consumer);
            } else {
                Arrays.stream(list)
                        .map(File::getAbsolutePath)
                        .filter(location.getFilter())
                        .forEach(consumer);
            }
        }
    }

    /**
     * 停止监听
     */
    @Override
    public synchronized void stop() {
        running = false;
        if (watchService != null) {
            try {
                watchService.close();
            } catch (IOException ignore) {
            }
        }
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /**
     * 开始监听。
     * 通过 {@link WatchService} 监听目录，当触发文件新增，修改，删除时进行回调。
     */
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void listen() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            this.watchService = watchService;
            for (PluginLocation location : locations) {
                final Path path = Paths.get(location.getPath());
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE);
            }
            while (running) {
                try {
                    final WatchKey key = watchService.take();
                    for (WatchEvent<?> watchEvent : key.pollEvents()) {
                        final WatchEvent.Kind<?> kind = watchEvent.kind();
                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            continue;
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            onFileCreated((WatchEvent<Path>) watchEvent);
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            onFileModified((WatchEvent<Path>) watchEvent);
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            onFileDeleted((WatchEvent<Path>) watchEvent);
                        }
                    }
                    key.reset();
                } catch (Throwable e) {
                    onError(e);
                }
            }
        }
    }

    /**
     * 文件创建
     *
     * @param watchEvent 监听到的事件
     */
    public void onFileCreated(WatchEvent<Path> watchEvent) {
        final Path path = watchEvent.context();
        File file = path.toFile();
        String p = file.getAbsolutePath();
        //如果该路径需要回调
        if (notifyCreate.contains(p)) {
            onCreateNotify(p);
        }
    }

    public void onCreateNotify(String path) {
        if (createConsumer != null) {
            createConsumer.accept(path);
        }
    }

    /**
     * 文件修改
     *
     * @param watchEvent 监听到的事件
     */
    public void onFileModified(WatchEvent<Path> watchEvent) {
        final Path path = watchEvent.context();
        File file = path.toFile();
        String p = file.getAbsolutePath();
        //如果该路径需要回调
        if (notifyModify.contains(p)) {
            onModifyNotify(p);
        }
    }

    public void onModifyNotify(String path) {
        if (modifyConsumer != null) {
            modifyConsumer.accept(path);
        }
    }

    /**
     * 文件删除
     *
     * @param watchEvent 监听到的事件
     */
    public void onFileDeleted(WatchEvent<Path> watchEvent) {
        final Path path = watchEvent.context();
        File file = path.toFile();
        String p = file.getAbsolutePath();
        //如果该路径需要回调
        if (notifyDelete.contains(p)) {
            onDeleteNotify(p);
        }
    }

    public void onDeleteNotify(String path) {
        if (deleteConsumer != null) {
            deleteConsumer.accept(path);
        }
    }

    public void onError(Throwable e) {
        if (errorConsumer != null) {
            errorConsumer.accept(e);
        }
    }

    public static class Builder {

        private ExecutorService executor;

        private PluginLocation[] locations;

        private Consumer<String> createConsumer;

        private Consumer<String> modifyConsumer;

        private Consumer<String> deleteConsumer;

        private Consumer<Throwable> errorConsumer;

        private WatchEvent.Kind<?> notifyOnStart = StandardWatchEventKinds.ENTRY_CREATE;

        public Builder executor(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        public Builder locations(PluginLocation... locations) {
            this.locations = locations;
            return this;
        }

        public Builder onCreate(Consumer<String> consumer) {
            this.createConsumer = consumer;
            return this;
        }

        public Builder onCreate(BiConsumer<String, WatchEvent.Kind<?>> consumer) {
            this.createConsumer = path -> consumer.accept(path, StandardWatchEventKinds.ENTRY_CREATE);
            return this;
        }

        public Builder onModify(Consumer<String> consumer) {
            this.modifyConsumer = consumer;
            return this;
        }

        public Builder onModify(BiConsumer<String, WatchEvent.Kind<?>> consumer) {
            this.modifyConsumer = path -> consumer.accept(path, StandardWatchEventKinds.ENTRY_MODIFY);
            return this;
        }

        public Builder onDelete(Consumer<String> consumer) {
            this.deleteConsumer = consumer;
            return this;
        }

        public Builder onDelete(BiConsumer<String, WatchEvent.Kind<?>> consumer) {
            this.deleteConsumer = path -> consumer.accept(path, StandardWatchEventKinds.ENTRY_DELETE);
            return this;
        }

        public Builder onNotify(BiConsumer<String, WatchEvent.Kind<?>> consumer) {
            this.createConsumer = path -> consumer.accept(path, StandardWatchEventKinds.ENTRY_CREATE);
            this.modifyConsumer = path -> consumer.accept(path, StandardWatchEventKinds.ENTRY_MODIFY);
            this.deleteConsumer = path -> consumer.accept(path, StandardWatchEventKinds.ENTRY_DELETE);
            return this;
        }

        public Builder onError(Consumer<Throwable> consumer) {
            this.errorConsumer = consumer;
            return this;
        }

        public Builder notifyOnStart(WatchEvent.Kind<?> notifyOnStart) {
            this.notifyOnStart = notifyOnStart;
            return this;
        }

        public WatchServicePluginAutoLoader build() {
            if (locations == null || locations.length == 0) {
                throw new PluginException("No path watched");
            }
            return new WatchServicePluginAutoLoader(executor,
                    createConsumer, modifyConsumer, deleteConsumer,
                    errorConsumer, notifyOnStart, locations);
        }
    }
}
