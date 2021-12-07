package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.source.AbstractSource;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCacheableSource extends AbstractSource implements CacheableSource {

    private boolean cacheEnabled;

    private String cachePath;
}
