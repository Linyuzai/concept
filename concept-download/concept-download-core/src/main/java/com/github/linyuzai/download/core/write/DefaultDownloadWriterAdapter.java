package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.exception.DownloadException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * {@link DownloadWriterAdapter} 的默认实现。
 */
@Getter
@AllArgsConstructor
public class DefaultDownloadWriterAdapter implements DownloadWriterAdapter {

    /**
     * {@link DownloadWriter} 列表
     */
    private final List<DownloadWriter> writers;

    /**
     * 匹配合适的 {@link DownloadWriter}，
     * 如没有可用的 {@link DownloadWriter} 则抛出异常。
     *
     * @param resource {@link Resource}
     * @param context  {@link DownloadContext}
     * @return 匹配到的 {@link DownloadWriter}
     */
    @Override
    public DownloadWriter getWriter(Resource resource, DownloadContext context) {
        for (DownloadWriter writer : writers) {
            if (writer.support(resource, context)) {
                return writer;
            }
        }
        throw new DownloadException("No DownloadWriter support: " + resource);
    }
}
