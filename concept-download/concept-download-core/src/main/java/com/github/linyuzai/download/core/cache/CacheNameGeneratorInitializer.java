package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CacheNameGeneratorInitializer implements DownloadContextInitializer {

    private CacheNameGenerator cacheNameGenerator;

    @Override
    public void initialize(DownloadContext context) {
        context.set(CacheNameGenerator.class, cacheNameGenerator);
    }
}
