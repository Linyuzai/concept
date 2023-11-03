package com.github.linyuzai.download.core.executor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;

@Getter
@RequiredArgsConstructor
public class SimpleDownloadExecutor implements DownloadExecutor {

    private final Executor executor;

    @Override
    public Executor getExecutor() {
        return executor;
    }
}
