package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.ConfigurableDownloadOptions;
import com.github.linyuzai.download.core.options.DownloadOptions;

public class AsyncConsumeHandler implements DownloadHandler, DownloadLifecycleListener {

    @Override
    public boolean support(DownloadContext context) {
        DownloadOptions options = context.get(DownloadOptions.class);
        return options.getAsyncConsumer() != null;
    }

    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        DownloadOptions options = context.get(DownloadOptions.class);
        options.getAsyncConsumer().accept(context);
        return null;
    }

    @Override
    public void onStart(DownloadContext context) {
        if (support(context)) {
            ConfigurableDownloadOptions options = context.get(DownloadOptions.class);
            options.setRequest(null);
            options.setResponse(null);
        }
    }
}
