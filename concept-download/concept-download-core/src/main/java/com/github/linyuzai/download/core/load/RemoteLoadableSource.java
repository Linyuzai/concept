package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadEventPublisher;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.write.DownloadWriter;
import com.github.linyuzai.download.core.write.DownloadWriterAdapter;
import com.github.linyuzai.download.core.write.Progress;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 支持网络加载的 {@link Source}。
 */
public abstract class RemoteLoadableSource extends AbstractLoadableSource {

    /**
     * 将 {@link InputStream} 写到 {@link OutputStream}，更新并发布加载进度。
     *
     * @param os      {@link OutputStream}
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    @Override
    public Mono<Source> doLoad(OutputStream os, DownloadContext context) {
        DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
        DownloadWriter writer = writerAdapter.getWriter(this, null, context);
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        return loadRemote(context).map(it -> {
            Progress progress = new Progress(length);
            writer.write(it, os, null, null, length, (current, increase) -> {
                progress.update(increase);
                publisher.publish(new SourceLoadingProgressEvent(context, this, progress.freeze()));
            });
            return this;
        });
    }

    /**
     * 远程加载。
     *
     * @param context {@link DownloadContext}
     * @return 加载后的 {@link Source}
     */
    public abstract Mono<InputStream> loadRemote(DownloadContext context);

    public static abstract class Builder<T extends RemoteLoadableSource, B extends Builder<T, B>> extends AbstractLoadableSource.Builder<T, B> {

        public Builder() {
            asyncLoad = true;
            cacheEnabled = true;
        }

        @Override
        protected T build(T target) {
            if (cacheEnabled && !StringUtils.hasText(cachePath)) {
                throw new DownloadException("Cache path is null or empty");
            }
            return super.build(target);
        }
    }
}
