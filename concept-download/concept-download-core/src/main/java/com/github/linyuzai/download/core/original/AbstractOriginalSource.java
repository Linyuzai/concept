package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.cache.AbstractCacheableSource;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

@Getter
@Setter
public abstract class AbstractOriginalSource extends AbstractCacheableSource implements OriginalSource {

    private boolean asyncLoad;
}
