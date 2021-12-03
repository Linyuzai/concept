package com.github.linyuzai.download.core.exception;

import com.github.linyuzai.download.core.context.DownloadContext;

import java.util.Collection;

@Deprecated
public interface DownloadExceptionHandler {

    void handle(Collection<Throwable> e, DownloadContext context);
}
