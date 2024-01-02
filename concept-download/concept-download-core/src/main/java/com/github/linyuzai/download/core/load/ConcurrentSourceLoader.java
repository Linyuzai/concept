package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 支持并发的 {@link SourceLoader}。
 */
public abstract class ConcurrentSourceLoader implements SourceLoader {

    /**
     * 将 {@link Source} 中需要异步加载的部分进行并发加载，
     * 并与同步加载之后的 {@link Source} 合并。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     */
    @Override
    public void load(Source source, DownloadContext context) {
        Collection<Source> syncSources = new ArrayList<>();
        Collection<Source> asyncSources = new ArrayList<>();
        Collection<Source> sources = source.list();
        for (Source s : sources) {
            if (s.isAsyncLoad()) {
                asyncSources.add(s);
            } else {
                syncSources.add(s);
            }
        }

        for (Source syncSource : syncSources) {
            syncSource.load(context);
        }

        if (!asyncSources.isEmpty()) {
            concurrentLoad(asyncSources, context);
        }
    }


    /**
     * 并发加载。
     *
     * @param sources {@link Source} 集合
     * @param context {@link DownloadContext}
     */
    public abstract void concurrentLoad(Collection<Source> sources, DownloadContext context);
}
