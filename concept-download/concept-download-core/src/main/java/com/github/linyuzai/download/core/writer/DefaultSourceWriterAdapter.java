package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;

@Getter
public class DefaultSourceWriterAdapter implements SourceWriterAdapter, DownloadContextInitializer {

    private final List<SourceWriter> writers;

    public DefaultSourceWriterAdapter(List<SourceWriter> writers) {
        this.writers = writers;
        this.writers.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    @Override
    public SourceWriter getSourceWriter(Source source, Range range, Charset charset, DownloadContext context) {
        for (SourceWriter writer : writers) {
            if (writer.support(source, range, charset, context)) {
                return writer;
            }
        }
        throw new SourceWriterException("No SourceWriter support: " + source);
    }

    @Override
    public void initialize(DownloadContext context) {
        context.set(SourceWriterAdapter.class, this);
    }
}
