package com.github.linyuzai.download.core.configuration;

import java.util.Collection;

/**
 * {@link DownloadConfigurer} 调用器。
 */
public class DownloadConfigurerInvoker {

    public DownloadConfigurerInvoker(DownloadConfiguration configuration, Collection<DownloadConfigurer> configurers) {
        for (DownloadConfigurer it : configurers) {
            it.configure(configuration);
        }
    }
}
