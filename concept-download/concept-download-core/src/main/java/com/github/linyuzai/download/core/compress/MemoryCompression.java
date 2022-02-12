package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 内存压缩。
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    @NonNull
    protected final byte[] bytes;

    /**
     * 获得一个 {@link ByteArrayInputStream}
     *
     * @return {@link ByteArrayInputStream}
     */
    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 获得字节数组的长度。
     *
     * @return 字节数组的长度
     */
    @Override
    public Long getLength() {
        return (long) bytes.length;
    }

    /**
     * 内存压缩描述。
     *
     * @return MemoryCompression(数据大小)
     */
    @Override
    public String getDescription() {
        return "MemoryCompression(" + DownloadUtils.format(getLength()) + ")";
    }
}
