package com.github.linyuzai.download.core.cache;

import com.github.linyuzai.download.core.source.Source;

public interface CacheableSource extends Source {

    void deleteCache();
}
