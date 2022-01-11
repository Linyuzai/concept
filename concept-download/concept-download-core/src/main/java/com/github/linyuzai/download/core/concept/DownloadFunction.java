package com.github.linyuzai.download.core.concept;

import java.io.IOException;

@FunctionalInterface
public interface DownloadFunction<T, R> {

    R apply(T t) throws IOException;
}
