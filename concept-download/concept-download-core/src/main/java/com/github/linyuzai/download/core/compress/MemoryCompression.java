package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 内存压缩。
 * <p>
 * Memory compression.
 */
@AllArgsConstructor
public class MemoryCompression extends AbstractCompression {

    @NonNull
    protected final byte[] bytes;

    /**
     * 获得一个 {@link ByteArrayInputStream}
     * <p>
     * Get a {@link ByteArrayInputStream}
     *
     * @return {@link ByteArrayInputStream}
     */
    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 获得字节数组的长度。
     * <p>
     * Gets the length of the byte array.
     *
     * @return 字节数组的长度
     * <p>
     * Length of the byte array
     */
    @Override
    public Long getLength() {
        return (long) bytes.length;
    }

    /**
     * 内存压缩描述。
     * <p>
     * Memory compression description.
     *
     * @return MemoryCompression(数据大小)
     * <p>
     * MemoryCompression(data size)
     */
    @Override
    public String getDescription() {
        return "MemoryCompression(" + DownloadUtils.format(getLength()) + ")";
    }
}
