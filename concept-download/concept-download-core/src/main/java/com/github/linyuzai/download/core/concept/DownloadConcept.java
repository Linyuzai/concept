package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.options.DownloadOptions;

import java.io.IOException;
import java.util.function.Function;

/**
 * 执行下载的统一对外接口 / Unified external interface for downloading
 */
public interface DownloadConcept {

    /**
     * 执行下载操作 / Perform download operation
     *
     * @param options 下载参数 / Options of download
     */
    default Object download(DownloadOptions options) {
        return download(downloadConfiguration -> options);
    }

    /**
     * 执行下载操作 / Perform download operation
     *
     * @param function 可以通过下载配置来返回一个下载参数 / return an options from the configuration
     */
    Object download(Function<DownloadConfiguration, DownloadOptions> function);
}
