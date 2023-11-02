package com.github.linyuzai.download.core.concept;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link Part} 的抽象类。
 */
public abstract class AbstractPart implements Part {

    /**
     * 当前持有的 {@link InputStream}
     */
    protected InputStream inputStream;

    /**
     * 如果当前没有持有 {@link InputStream} 则新打开一个，
     * 否则直接使用当前的 {@link InputStream}。
     *
     * @return 新打开的 {@link InputStream} 或已经持有的 {@link InputStream}
     */
    @Override
    public InputStream getInputStream() throws IOException {
        if (inputStream == null) {
            inputStream = openInputStream();
        }
        return inputStream;
    }

    /**
     * 打开一个新的 {@link InputStream}。
     *
     * @return {@link InputStream}
     */
    public abstract InputStream openInputStream() throws IOException;

    /**
     * 释放子节点的资源并关闭当前持有的 {@link InputStream}。
     */
    @Override
    public void release() {
        getChildren().forEach(Part::release);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (Throwable ignore) {
            }
            inputStream = null;
        }
    }
}
