package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;

public interface CacheNameGenerator {

    String generate(Downloadable downloadable, DownloadContext context);
}
