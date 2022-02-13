package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.DownloadableResource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import com.github.linyuzai.download.core.web.Range;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * 默认的写入器的适配器 / Adapter of write by default
 */
@Getter
@AllArgsConstructor
public class DefaultDownloadWriterAdapter implements DownloadWriterAdapter {

    private final List<DownloadWriter> writers;

    @Override
    public DownloadWriter getWriter(DownloadableResource downloadableResource, Range range, DownloadContext context) {
        for (DownloadWriter writer : writers) {
            if (writer.support(downloadableResource, range, context)) {
                return writer;
            }
        }
        throw new DownloadException("No DownloadWriter support: " + downloadableResource);
    }
}
