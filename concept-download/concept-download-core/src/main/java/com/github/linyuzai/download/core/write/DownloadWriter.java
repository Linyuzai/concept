package com.github.linyuzai.download.core.write;

import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.order.OrderProvider;
import com.github.linyuzai.download.core.web.Range;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 具体操作字节或字符的写入器 / Writer to write bytes or chars
 */
public interface DownloadWriter extends OrderProvider {

    /**
     * @param downloadable 可下载的资源 / Resource can be downloaded
     * @param range        写入的范围 / Range of writing
     * @param context      下载上下文 / Context of download
     * @return 是否支持 / Is it supported
     */
    boolean support(Downloadable downloadable, Range range, DownloadContext context);

    default void write(InputStream is, OutputStream os, Range range, Charset charset, Long length) {
        write(is, os, range, charset, length, null);
    }

    /**
     * 执行写入 / Do write
     *
     * @param is      输入流 / Input stream
     * @param os      输出流 / Output stream
     * @param range   写入的范围 / Range of writing
     * @param charset 编码 / Charset
     * @param length  总字节数，可能为null / Total bytes count, may be null
     */
    void write(InputStream is, OutputStream os, Range range, Charset charset, Long length, Callback callback);

    interface Callback {

        void onWrite(long current, long increase);
    }
}
