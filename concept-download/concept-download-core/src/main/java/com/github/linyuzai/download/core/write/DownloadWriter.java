package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.Resource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.web.Range;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 具体操作 {@link InputStream} 和 {@link OutputStream} 的写入器。
 */
public interface DownloadWriter extends OrderProvider {

    /**
     * 该写入器是否支持写入。
     *
     * @param resource {@link Resource}
     * @param range                {@link Range}
     * @param context              {@link DownloadContext}
     * @return 如果支持则返回 true
     */
    boolean support(Resource resource, Range range, DownloadContext context);

    /**
     * 执行写入。
     *
     * @param is      {@link InputStream}
     * @param os      {@link OutputStream}
     * @param range   {@link Range}
     * @param charset {@link Charset}
     * @param length  总大小，可能为 null
     */
    default void write(InputStream is, OutputStream os, Range range, Charset charset, Long length) {
        write(is, os, range, charset, length, null);
    }

    /**
     * 执行写入。
     *
     * @param is       {@link InputStream}
     * @param os       {@link OutputStream}
     * @param range    {@link Range}
     * @param charset  {@link Charset}
     * @param length   总大小，可能为 null
     * @param callback 回调当前进度和增长的大小
     */
    void write(InputStream is, OutputStream os, Range range, Charset charset, Long length, Callback callback);

    interface Callback {

        void onWrite(long current, long increase);
    }
}
