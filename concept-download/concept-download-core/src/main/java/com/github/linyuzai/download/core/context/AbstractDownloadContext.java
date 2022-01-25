package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;

/**
 * 持有 {@link DownloadOptions}。
 * 应用 {@link DownloadContextInitializer} 初始化 {@link DownloadContext}。
 * 应用 {@link DownloadContextDestroyer} 销毁 {@link DownloadContext}。
 * <p>
 * Hold {@link DownloadOptions}.
 * Apply {@link DownloadContextInitializer} to initialize {@link DownloadContext}.
 * Apply {@link DownloadContextInitializer} to destroy {@link DownloadContext}.
 */
@Getter
public abstract class AbstractDownloadContext implements DownloadContext {

    private final String id;

    private final DownloadOptions options;

    @NonNull
    @Setter
    private Collection<DownloadContextInitializer> initializers = Collections.emptyList();

    @NonNull
    @Setter
    private Collection<DownloadContextDestroyer> destroyers = Collections.emptyList();

    public AbstractDownloadContext(@NonNull String id, @NonNull DownloadOptions options) {
        this.id = id;
        this.options = options;
    }

    /**
     * 遍历 {@link DownloadContextInitializer} 初始化 {@link DownloadContext}。
     * <p>
     * Traverse {@link DownloadContextInitializer} to initialize {@link DownloadContext}.
     */
    @Override
    public void initialize() {
        initializers.forEach(it -> it.initialize(this));
    }

    /**
     * 遍历 {@link DownloadContextDestroyer} 销毁 {@link DownloadContext}。
     * <p>
     * Traverse {@link DownloadContextDestroyer} and destroy {@link DownloadContext}.
     */
    @Override
    public void destroy() {
        destroyers.forEach(it -> it.destroy(this));
    }
}
