package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 支持并发的 {@link SourceLoader}。
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ConcurrentSourceLoader implements SourceLoader {

    /**
     * 单个文件的时候串行加载
     */
    @Setter
    private boolean serialOnSingle = true;

    /**
     * 将 {@link Source} 中需要异步加载的部分进行并发加载，
     * 并与同步加载之后的 {@link Source} 合并。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
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

        if (asyncSources.isEmpty()) {
            for (Source syncSource : syncSources) {
                syncSource.load(context);
            }
        } else {
            if (asyncSources.size() == 1 && serialOnSingle) {
                syncSources.add(asyncSources.iterator().next());
                for (Source syncSource : syncSources) {
                    syncSource.load(context);
                }
            } else {
                for (Source syncSource : syncSources) {
                    syncSource.load(context);
                }
                concurrentLoad(asyncSources, context);
            }
        }
    }


    /**
     * 并发加载。
     *
     * @param sources {@link Source} 集合
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    public abstract void concurrentLoad(Collection<Source> sources, DownloadContext context);
}
