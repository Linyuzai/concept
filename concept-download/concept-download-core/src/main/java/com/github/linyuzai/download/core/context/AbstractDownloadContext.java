package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;

/**
 * 持有下载操作参数的下载上下文 / Context of download holding download options
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

    @Override
    public void initialize() {
        initializers.forEach(it -> it.initialize(this));
    }

    @Override
    public void destroy() {
        destroyers.forEach(it -> it.destroy(this));
    }
}
