package com.github.linyuzai.download.core.configuration;

/**
 * 下载配置器 / Configurer of download configuration
 */
public interface DownloadConfigurer {

    /**
     * 修改配置 / Modify configuration
     *
     * @param configuration 默认配置 / Default configuration
     */
    void configure(DownloadConfiguration configuration);
}
