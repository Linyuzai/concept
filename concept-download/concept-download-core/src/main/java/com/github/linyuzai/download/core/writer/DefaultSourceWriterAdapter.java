package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DefaultSourceWriterAdapter implements SourceWriterAdapter {

    private final List<SourceWriter> writers;

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
