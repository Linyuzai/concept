package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
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
import java.util.function.Consumer;

/**
 * 基于 {@link WatchService} 的插件文件自动加载
 */
@Getter
public class WatchServicePluginAutoLoader implements PluginAutoLoader {

    /**
     * {@link PluginConcept}
     */
    private final PluginConcept pluginConcept;

    /**
     * 执行线程池
     */
    private final ExecutorService executor;

    /**
     * 插件位置
     */
    private final PluginLocation[] locations;

    /**
     * 异常处理
     */
    private final Consumer<Throwable> errorConsumer;

    /**
     * 在 {@link #start()} 时是否触发一次加载
     */
    private final boolean loadOnStart;

    /**
     * {@link WatchService}
     */
    private WatchService watchService;


    private final Set<String> notifyCreate = new HashSet<>();

    private final Set<String> notifyModify = new HashSet<>();

    private final Set<String> notifyDelete = new HashSet<>();

    private boolean running = false;

    private WatchServicePluginAutoLoader(PluginConcept concept, ExecutorService executor,
                                         Consumer<Throwable> consumer, boolean loadOnStart,
                                         PluginLocation... locations) {
        this.pluginConcept = concept;
        this.executor = executor;
        this.locations = locations;
        this.errorConsumer = consumer;
        this.loadOnStart = loadOnStart;
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

    @Override
    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        if (loadOnStart) {
            for (PluginLocation location : locations) {
                File[] list = new File(location.getPath()).listFiles();
                if (list == null) {
                    continue;
                }
                if (location.getFilter() == null) {
                    Arrays.stream(list)
                            .map(File::getAbsolutePath)
                            .forEach(pluginConcept::load);
                } else {
                    Arrays.stream(list)
                            .map(File::getAbsolutePath)
                            .filter(location.getFilter()).forEach(pluginConcept::load);
                }
            }
        }
        if (executor == null) {
            new Thread(this::listen).start();
        } else {
            executor.execute(this::listen);
        }
    }

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

    public void onFileCreated(WatchEvent<Path> watchEvent) {
        final Path path = watchEvent.context();
        File file = path.toFile();
        if (notifyCreate.contains(file.getAbsolutePath())) {
            load(file);
        }
    }

    public void onFileModified(WatchEvent<Path> watchEvent) {
        //请自行实现
    }

    public void onFileDeleted(WatchEvent<Path> watchEvent) {
        //请自行实现
    }

    public void load(File file) {
        pluginConcept.load(file);
    }

    public void onError(Throwable e) {
        if (errorConsumer != null) {
            errorConsumer.accept(e);
        }
    }

    public static class Builder {

        private PluginConcept concept;

        private ExecutorService executor;

        private PluginLocation[] locations;

        private Consumer<Throwable> consumer;

        private boolean loadOnStart = true;

        public Builder pluginConcept(PluginConcept concept) {
            this.concept = concept;
            return this;
        }

        public Builder executorService(ExecutorService executor) {
            this.executor = executor;
            return this;
        }

        public Builder locations(PluginLocation... locations) {
            this.locations = locations;
            return this;
        }

        public Builder errorConsumer(Consumer<Throwable> consumer) {
            this.consumer = consumer;
            return this;
        }

        public Builder loadOnStart(boolean loadOnStart) {
            this.loadOnStart = loadOnStart;
            return this;
        }

        public WatchServicePluginAutoLoader build() {
            if (concept == null) {
                throw new PluginException("PluginConcept is null");
            }
            if (locations == null || locations.length == 0) {
                throw new PluginException("No path watched");
            }
            return new WatchServicePluginAutoLoader(concept, executor, consumer, loadOnStart, locations);
        }
    }
}
