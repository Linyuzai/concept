package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.utils.DownloadUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 内存压缩。
 */
@Getter
@RequiredArgsConstructor
public class MemoryCompression extends AbstractCompression {

    /**
     * 存储在字节数组中
     */
    protected final byte @NonNull [] bytes;

    /**
     * 获得一个 {@link ByteArrayInputStream}。
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
