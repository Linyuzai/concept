package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;

public abstract class AbstractCacheNameGenerator implements CacheNameGenerator {

    @Override
    public String generate(Downloadable downloadable, DownloadContext context) {
        String name = downloadable.getName();
        if (name == null || name.isEmpty()) {
            return doGenerate(downloadable, context);
        } else {
            return name;
        }
    }

    public abstract String doGenerate(Downloadable downloadable, DownloadContext context);
}
