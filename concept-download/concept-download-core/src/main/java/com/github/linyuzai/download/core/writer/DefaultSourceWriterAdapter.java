package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class DefaultSourceWriterAdapter implements SourceWriterAdapter {

    private final List<SourceWriter> writers;

    public DefaultSourceWriterAdapter(List<SourceWriter> writers) {
        this.writers = writers;
        this.writers.sort(Comparator.comparingInt(OrderProvider::getOrder));
    }

    @Override
    public SourceWriter getSourceWriter(Source source, DownloadContext context) {
        for (SourceWriter writer : writers) {
            if (writer.support(source, context)) {
                return writer;
            }
        }
        throw new SourceWriterException("No SourceWriter support: " + source);
    }
}
