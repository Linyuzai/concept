package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.options.DownloadOptions;

/**
 * 下载处理的入口。
 * <p>
 * Entry for download processing.
 */
public interface DownloadConcept {

    /**
     * 执行下载操作。
     * <p>
     * Perform download operation.
     *
     * @param options {@link DownloadOptions}
     */
    Object download(DownloadOptions options);
}
