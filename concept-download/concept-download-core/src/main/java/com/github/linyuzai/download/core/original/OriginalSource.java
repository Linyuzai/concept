package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.cache.CacheableSource;
import com.github.linyuzai.download.core.loader.LoadableSource;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 下载源
 */
public interface OriginalSource extends CacheableSource, LoadableSource {

    boolean isSingle();

    default Collection<OriginalSource> flatten() {
        return flatten(source -> true);
    }

    default Collection<OriginalSource> flatten(Predicate<OriginalSource> predicate) {
        if (predicate.test(this)) {
            return Collections.singletonList(this);
        } else {
            return Collections.emptyList();
        }
    }
}
