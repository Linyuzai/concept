package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.options.DownloadOptions;

/**
 * 下载处理的统一入口。
 */
public interface DownloadConcept {

    /**
     * 执行下载操作。
     *
     * @param options {@link DownloadOptions}
     */
    Object download(DownloadOptions options);
}
