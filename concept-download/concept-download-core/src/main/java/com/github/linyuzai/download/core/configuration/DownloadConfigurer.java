package com.github.linyuzai.download.core.configuration;

/**
 * {@link DownloadConfiguration} 的配置器。
 * 可以在代码中修改全局默认配置。
 */
public interface DownloadConfigurer {

    /**
     * 配置。
     *
     * @param configuration {@link DownloadConfiguration}
     */
    void configure(DownloadConfiguration configuration);
}
