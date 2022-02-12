package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import reactor.core.publisher.Mono;

/**
 * {@link Source} 加载器。
 *
 * @see DefaultSourceLoader
 * @see SchedulerSourceLoader
 */
public interface SourceLoader {

    /**
     * 执行加载。
     *
     * @param source  {@link Source}
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    Mono<Source> load(Source source, DownloadContext context);
}
