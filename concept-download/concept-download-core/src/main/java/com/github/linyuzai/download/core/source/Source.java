package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.load.Loadable;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

/**
 * 下载源
 */
public interface Source extends Downloadable, Loadable {

    boolean isSingle();

    default Collection<Source> flatten() {
        return flatten(source -> true);
    }

    default Collection<Source> flatten(Predicate<Source> predicate) {
        if (predicate.test(this)) {
            return Collections.singletonList(this);
        } else {
            return Collections.emptyList();
        }
    }
}
