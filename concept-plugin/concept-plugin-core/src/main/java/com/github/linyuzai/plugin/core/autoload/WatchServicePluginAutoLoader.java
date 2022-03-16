package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.PluginConcept;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

@Getter
public class WatchServicePluginAutoLoader implements PluginAutoLoader {

    private final PluginConcept pluginConcept;

    private final ExecutorService executor;

    private final PluginPath[] paths;

    private final Consumer<Throwable> errorConsumer;

    private final boolean loadOnStart;

    private WatchService watchService;

    private final Set<String> notifyCreate = new HashSet<>();

    private final Set<String> notifyModify = new HashSet<>();

    private final Set<String> notifyDelete = new HashSet<>();

    private final Set<String> files = new HashSet<>();

    private boolean running = false;

    private WatchServicePluginAutoLoader(PluginConcept concept, ExecutorService executor,
                                         Consumer<Throwable> consumer, boolean loadOnStart,
                                         PluginPath... paths) {
        this.pluginConcept = concept;
        this.executor = executor;
        this.paths = paths;
        this.errorConsumer = consumer;
        this.loadOnStart = loadOnStart;
        for (PluginPath path : this.paths) {
            if (path.isNotifyCreate()) {
                notifyCreate.add(path.getPath());
            }
            if (path.isNotifyModify()) {
                notifyModify.add(path.getPath());
            }
            if (path.isNotifyDelete()) {
                notifyDelete.add(path.getPath());
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
            for (PluginPath path : paths) {
                File[] list = new File(path.getPath()).listFiles();
                if (list == null) {
                    continue;
                }
                if (path.getFilter() == null) {
                    Arrays.stream(list)
                            .map(File::getAbsolutePath)
                            .forEach(pluginConcept::load);
                } else {
                    Arrays.stream(list)
                            .map(File::getAbsolutePath)
                            .filter(path.getFilter()).forEach(pluginConcept::load);
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
            for (PluginPath pluginPath : paths) {
                final Path path = Paths.get(pluginPath.getPath());
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
        final Path path = watchEvent.context();
        File file = path.toFile();
        if (notifyModify.contains(file.getAbsolutePath())) {
            unload(file);
            load(file);
        }
    }

    public void onFileDeleted(WatchEvent<Path> watchEvent) {
        final Path path = watchEvent.context();
        File file = path.toFile();
        if (notifyDelete.contains(file.getAbsolutePath())) {
            unload(file);
        }
    }

    public void load(File file) {
        pluginConcept.load(file);
        files.add(file.getAbsolutePath());
    }

    public void unload(File file) {
        files.remove(file.getAbsolutePath());
    }

    public void onError(Throwable e) {
        if (errorConsumer != null) {
            errorConsumer.accept(e);
        }
    }

    public static class Builder {

        private PluginConcept concept;

        private ExecutorService executor;

        private PluginPath[] paths;

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

        public Builder paths(PluginPath... paths) {
            this.paths = paths;
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
            if (paths == null || paths.length == 0) {
                throw new PluginException("No path watched");
            }
            return new WatchServicePluginAutoLoader(concept, executor, consumer, loadOnStart, paths);
        }
    }
}
