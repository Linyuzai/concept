package com.github.linyuzai.download.core.configuration;

/**
 * {@link DownloadConfiguration} 的配置器。
 * <p>
 * Configurer of {@link DownloadConfiguration}.
 */
public interface DownloadConfigurer {

    /**
     * 配置。
     * <p>
     * Configure.
     *
     * @param configuration {@link DownloadConfiguration}
     */
    void configure(DownloadConfiguration configuration);
}
