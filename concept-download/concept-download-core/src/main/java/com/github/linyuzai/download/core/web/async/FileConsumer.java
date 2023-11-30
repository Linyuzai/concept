package com.github.linyuzai.download.core.web.async;

import com.github.linyuzai.download.core.compress.Compression;
import com.github.linyuzai.download.core.compress.FileCompression;
import com.github.linyuzai.download.core.compress.NoCompression;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.source.file.FileSource;
import lombok.SneakyThrows;

import java.io.File;
import java.util.function.Consumer;

public interface FileConsumer extends Consumer<DownloadContext> {

    @SneakyThrows
    @Override
    default void accept(DownloadContext context) {
        Compression compression = context.get(Compression.class);
        if (compression instanceof FileCompression) {
            consumer(((FileCompression) compression).getFile());
            return;
        }
        if (compression instanceof NoCompression) {
            Source source = ((NoCompression) compression).getSource();
            if (source instanceof FileSource) {
                consumer(((FileSource) source).getFile());
                return;
            } else {
                if (source.isCacheEnabled() && source.isCacheExisted()) {
                    consumer(new File(source.getCachePath()));
                    return;
                }
            }
        }
        throw new IllegalArgumentException("Can not get file");
    }

    void consumer(File file);
}
