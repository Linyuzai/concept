package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.Getter;

/**
 * {@link Source} 压缩完成后会发布该事件。
 */
@Getter
public class AfterSourceCompressedEvent extends AbstractCompressSourceEvent {

    /**
     * 压缩后的对象
     */
    private final Compression compression;

    public AfterSourceCompressedEvent(DownloadContext context, Source source, Compression compression) {
        super(context, source, null);
        this.compression = compression;
        setMessage("Source compressed " + formatCompressedSize(source, compression));
    }

    /**
     * 处理压缩前后的对比及压缩率。
     *
     * @param source      {@link Source}
     * @param compression {@link Compression}
     * @return 压缩前后的对比及压缩率
     */
    public String formatCompressedSize(Source source, Compression compression) {
        Long sl = source.getLength();
        Long cl = compression.getLength();
        StringBuilder builder = new StringBuilder();
        if (sl == null) {
            builder.append("?");
        } else {
            builder.append(DownloadUtils.format(source.getLength()));
        }
        builder.append(" => ");
        if (cl == null) {
            builder.append("?");
        } else {
            builder.append(DownloadUtils.format(compression.getLength()));
        }

        if (sl != null && cl != null) {
            double r = cl.doubleValue() / sl.doubleValue() * 100.0;
            builder.append(" (").append(String.format("%.2f", r)).append("%)");
        }
        return builder.toString();
    }
}
