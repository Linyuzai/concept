package com.github.linyuzai.download.core.loader;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.concept.Downloadable;

import java.io.IOException;

public interface Loadable extends Downloadable {

    default void load(DownloadContext context) {

    }

    boolean isAsyncLoad();
}
