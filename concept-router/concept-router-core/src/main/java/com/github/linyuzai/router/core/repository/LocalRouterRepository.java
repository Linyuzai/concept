package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.PathPatternRouter;
import com.github.linyuzai.router.core.concept.Router;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Collection;

@Getter
public abstract class LocalRouterRepository extends InMemoryRouterRepository {

    private final String path;

    public LocalRouterRepository() {
        this(new File(System.getProperty("user.home"), "concept/router").getAbsolutePath());
    }

    public LocalRouterRepository(String path) {
        this.path = path;
    }

    @SneakyThrows
    public void initialize() {
        try (FileInputStream fis = new FileInputStream(getFile())) {
            Collection<? extends Router> routers = read(fis);
            for (Router router : routers) {
                PathPatternRouter ppr = (PathPatternRouter) router;
                routerMap.put(ppr.getId(), ppr);
                pathPatternMap.put(ppr.getPathPattern(), ppr);
            }
        }
    }

    @SneakyThrows
    @Override
    public void add(Collection<? extends Router> routes) {
        super.add(routes);
        try (FileOutputStream fos = new FileOutputStream(getFile())) {
            write(fos, routerMap.values());
        }
    }


    @SneakyThrows
    @Override
    public void update(Collection<? extends Router> routes) {
        super.update(routes);
        try (FileOutputStream fos = new FileOutputStream(getFile())) {
            write(fos, routerMap.values());
        }
    }

    @SneakyThrows
    @Override
    public void remove(Collection<? extends String> ids) {
        super.remove(ids);
        try (FileOutputStream fos = new FileOutputStream(getFile())) {
            write(fos, routerMap.values());
        }
    }

    @SneakyThrows
    public File getFile() {
        File parent = new File(path);
        if (!parent.exists()) {
            boolean mkdirs = parent.mkdirs();
        }
        File file = new File(parent, "routers.json");
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
        }
        return file;
    }

    public abstract Collection<? extends Router> read(InputStream is);

    public abstract void write(OutputStream os, Collection<? extends Router> routers);
}
