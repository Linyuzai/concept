package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import org.springframework.util.StringUtils;

public abstract class AbstractCacheNameGenerator implements CacheNameGenerator {

    @Override
    public String generate(Downloadable downloadable, DownloadContext context) {
        String name = downloadable.getName();
        if (StringUtils.hasText(name)) {
            return name;
        } else {
            return doGenerate(downloadable, context);
        }
    }

    public abstract String doGenerate(Downloadable downloadable, DownloadContext context);
}
