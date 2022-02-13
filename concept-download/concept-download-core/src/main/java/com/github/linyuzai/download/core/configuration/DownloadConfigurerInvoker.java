package com.github.linyuzai.download.core.configuration;

import lombok.Getter;

import java.util.Collection;

/**
 * {@link DownloadConfigurer} 调用器。
 */
public class DownloadConfigurerInvoker {

    @Getter
    private final DownloadConfiguration configuration;

    public DownloadConfigurerInvoker(DownloadConfiguration configuration, Collection<DownloadConfigurer> configurers) {
        this.configuration = configuration;
        for (DownloadConfigurer it : configurers) {
            it.configure(configuration);
        }
    }
}
