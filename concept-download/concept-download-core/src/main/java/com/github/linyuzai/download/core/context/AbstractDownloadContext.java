package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;

/**
 * {@link DownloadContext} 的抽象类。
 */
@Getter
public abstract class AbstractDownloadContext implements DownloadContext {

    /**
     * 唯一id
     */
    private final String id;

    /**
     * 操作参数
     */
    private final DownloadOptions options;

    /**
     * 初始化器
     */
    @NonNull
    @Setter
    private Collection<DownloadContextInitializer> initializers = Collections.emptyList();

    /**
     * 销毁器
     */
    @NonNull
    @Setter
    private Collection<DownloadContextDestroyer> destroyers = Collections.emptyList();

    public AbstractDownloadContext(@NonNull String id, @NonNull DownloadOptions options) {
        this.id = id;
        this.options = options;
    }

    /**
     * 遍历 {@link DownloadContextInitializer} 初始化 {@link DownloadContext}。
     */
    @Override
    public void initialize() {
        initializers.forEach(it -> it.initialize(this));
    }

    /**
     * 遍历 {@link DownloadContextDestroyer} 销毁 {@link DownloadContext}。
     */
    @Override
    public void destroy() {
        destroyers.forEach(it -> it.destroy(this));
    }
}
