package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import com.github.linyuzai.download.core.writer.DownloadWriterAdapter;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public abstract class RemoteLoadableSource extends AbstractLoadableSource {

    @SneakyThrows
    @Override
    public Mono<InputStream> doLoad(DownloadContext context) {
        return loadRemote(context).map(it -> {
            if (it instanceof ByteArrayInputStream) {
                return it;
            }
            DownloadWriterAdapter writerAdapter = context.get(DownloadWriterAdapter.class);
            DownloadWriter writer = writerAdapter.getWriter(this, null, context);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writer.write(it, os, null, null, getLength());
            return new ByteArrayInputStream(os.toByteArray());
        });
    }

    public abstract Mono<InputStream> loadRemote(DownloadContext context);

    public static abstract class Builder<T extends RemoteLoadableSource, B extends Builder<T, B>> extends AbstractLoadableSource.Builder<T, B> {

        public Builder() {
            asyncLoad = true;
            cacheEnabled = true;
        }

        @Override
        protected T build(T target) {
            if (cacheEnabled && (cachePath == null || cachePath.isEmpty())) {
                throw new DownloadException("Cache path is null or empty");
            }
            return super.build(target);
        }
    }
}
