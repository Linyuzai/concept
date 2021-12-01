package com.github.linyuzai.download.core.original;

import com.github.linyuzai.download.core.source.Source;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 下载源
 */
public interface OriginalSource extends Source {

    void load() throws IOException;

    boolean isAsyncLoad();

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
