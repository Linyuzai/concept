package com.github.linyuzai.router.core.repository;

import com.github.linyuzai.router.core.concept.Router;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.*;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * 基于本地文件的路由仓库
 */
@Getter
@RequiredArgsConstructor
public abstract class LocalRouterRepository extends InMemoryRouterRepository {

    /**
     * 文件目录
     */
    private volatile String path;

    /**
     * 文件目录提供者，懒加载并且缓存到 path
     * <p>
     * 懒加载是由于当端口配置在配置中心上时，晚一点才能拿到端口
     */
    private final Supplier<String> pathSupplier;

    @SneakyThrows
    @Override
    public void initialize() {
        try (FileInputStream fis = new FileInputStream(getFile())) {
            Collection<? extends Router> routers = read(fis);
            add(routers);
        }
    }

    @SneakyThrows
    @Override
    public void add(Collection<? extends Router> routes) {
        super.add(routes);
        overwrite();
    }

    @SneakyThrows
    @Override
    public void update(Collection<? extends Router> routes) {
        super.update(routes);
        overwrite();
    }

    @SneakyThrows
    @Override
    public void remove(Collection<? extends String> ids) {
        super.remove(ids);
        overwrite();
    }

    /**
     * 覆盖文件
     */
    @SneakyThrows
    protected void overwrite() {
        try (FileOutputStream fos = new FileOutputStream(getFile())) {
            write(fos, all());
        }
    }

    @SneakyThrows
    public File getFile() {
        File parent = new File(getPath());
        if (!parent.exists()) {
            boolean mkdirs = parent.mkdirs();
        }
        File file = new File(parent, "routers.json");
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
        }
        return file;
    }

    public String getPath() {
        if (path == null) {
            synchronized (this) {
                if (path == null) {
                    path = pathSupplier.get();
                }
            }
        }
        return path;
    }

    public abstract Collection<? extends Router> read(InputStream is);

    public abstract void write(OutputStream os, Collection<? extends Router> routers);
}
