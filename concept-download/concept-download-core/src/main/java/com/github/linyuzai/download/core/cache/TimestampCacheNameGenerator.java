package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;

public class TimestampCacheNameGenerator extends AbstractCacheNameGenerator {

    @Override
    public String doGenerate(Downloadable downloadable, DownloadContext context) {
        return String.valueOf(System.currentTimeMillis());
    }
}
