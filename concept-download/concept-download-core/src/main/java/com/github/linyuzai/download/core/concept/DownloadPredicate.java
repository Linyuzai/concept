package com.github.linyuzai.download.core.concept;

import java.io.IOException;

public interface DownloadPredicate<T> {

    boolean test(T t) throws IOException;
}
