package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.options.DownloadOptions;

import java.util.function.Function;

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
    default Object download(DownloadOptions options) {
        return download(configuration -> options);
    }

    /**
     * 执行下载操作。
     * <p>
     * Perform download operation.
     *
     * @param function 基于 {@link DownloadConfiguration} 返回 {@link DownloadOptions}
     *                 <p>
     *                 Return {@link DownloadOptions} based on {@link DownloadConfiguration}
     */
    Object download(Function<DownloadConfiguration, DownloadOptions> function);
}
