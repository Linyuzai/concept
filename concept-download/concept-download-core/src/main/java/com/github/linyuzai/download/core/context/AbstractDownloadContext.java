package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.options.DownloadOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * 持有下载操作参数的下载上下文 / Context of download holding download options
 */
@Getter
@AllArgsConstructor
public abstract class AbstractDownloadContext implements DownloadContext {

    @NonNull
    private final String id;

    @NonNull
    private final DownloadOptions options;

    @NonNull
    private final Collection<DownloadContextInitializer> initializers;

    @NonNull
    private final Collection<DownloadContextDestroyer> destroyers;
}
