package com.github.linyuzai.download.core.web.async;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.function.Consumer;

public interface InputStreamConsumer extends Consumer<DownloadContext> {

    @SneakyThrows
    @Override
    default void accept(DownloadContext context) {
        Compression compression = context.get(Compression.class);
        for (Part part : compression.getParts()) {
            consume(part.getInputStream());
        }
    }

    default void consume(InputStream is) {
        consumer(is);
    }

    @Deprecated
    default void consumer(InputStream is) {

    }
}
