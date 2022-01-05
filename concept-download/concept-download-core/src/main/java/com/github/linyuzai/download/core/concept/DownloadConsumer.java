package com.github.linyuzai.download.core.concept;

import java.io.IOException;

public interface DownloadConsumer<T> {

    void apply(T t) throws IOException;
}
