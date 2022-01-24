package com.github.linyuzai.download.core.concept;

import java.io.InputStream;

/**
 * 实现了资源释放的 {@link Part}。
 * <p>
 * {@link Part} with method implementation of resources release
 */
public abstract class AbstractPart implements Part {

    /**
     * 当前持有的 {@link InputStream}。
     * <p>
     * Currently held {@link InputStream}
     */
    protected InputStream inputStream;

    /**
     * 如果当前没有持有 {@link InputStream} 则新打开一个，
     * 否则直接使用当前的 {@link InputStream}。
     * <p>
     * If {@link InputStream} is not currently held, a new one will be opened.
     * Otherwise, the current {@link InputStream} will be used directly.
     *
     * @return 新打开的 {@link InputStream} 或已经持有的 {@link InputStream}
     * <p>
     * Newly opened {@link InputStream} or already held {@link InputStream}
     */
    @Override
    public InputStream getInputStream() {
        if (inputStream == null) {
            inputStream = openInputStream();
        }
        return inputStream;
    }

    /**
     * 打开一个新的 {@link InputStream}。
     * <p>
     * Open a new {@link InputStream}.
     *
     * @return {@link InputStream}
     */
    public abstract InputStream openInputStream();

    /**
     * 释放子节点的资源并关闭当前持有的 {@link InputStream}。
     * <p>
     * Release the resources of sub parts and close the {@link InputStream} currently held.
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
