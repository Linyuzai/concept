package com.github.linyuzai.plugin.core.autoload;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class WatchServicePluginAutoLoader implements PluginAutoLoader {

    private final PluginConcept pluginConcept;

    private final ExecutorService executor;

    private final PluginPath[] paths;

    private final Map<String, Plugin> pluginMap = new ConcurrentHashMap<>();

    private boolean running = false;

    public WatchServicePluginAutoLoader(PluginConcept pluginConcept, ExecutorService executor, PluginPath... paths) {
        this.pluginConcept = pluginConcept;
        this.executor = executor;
        this.paths = paths;
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        for (PluginPath path : paths) {
            String[] list = new File(path.getPath()).list();
            if (list == null) {
                continue;
            }
            if (path.getFilter() == null) {
                Arrays.stream(list).forEach(pluginConcept::load);
            } else {
                Arrays.stream(list).filter(path.getFilter()).forEach(pluginConcept::load);
            }

        }
        if (executor == null) {
            new Thread(this::listen).start();
        } else {
            executor.execute(this::listen);
        }
    }

    public synchronized void stop() {
        running = false;
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public void listen() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
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
                            final Path p = ((WatchEvent<Path>) watchEvent).context();
                            File file = p.toFile();
                            load(file);
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            final Path p = ((WatchEvent<Path>) watchEvent).context();
                            File file = p.toFile();
                            unload(file);
                            load(file);
                        }
                        if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            final Path p = ((WatchEvent<Path>) watchEvent).context();
                            File file = p.toFile();
                            unload(file);
                        }
                    }
                    key.reset();
                } catch (Throwable ignore) {

                }
            }
        }
    }

    private void load(File file) {
        pluginMap.put(file.getAbsolutePath(), pluginConcept.load(file));
    }

    private void unload(File file) {
        Plugin plugin = pluginMap.remove(file.getAbsolutePath());
        if (plugin != null) {
            pluginConcept.remove(plugin.getId());
        }
    }
}
