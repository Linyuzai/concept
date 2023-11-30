package com.github.linyuzai.download.core.handler.impl;

import com.github.linyuzai.download.core.concept.DownloadMode;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.event.DownloadLifecycleListener;
import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import com.github.linyuzai.download.core.options.ConfigurableDownloadOptions;
import com.github.linyuzai.download.core.options.DownloadOptions;

public class AsyncConsumeHandler implements DownloadHandler, DownloadLifecycleListener {

    @Override
    public boolean support(DownloadContext context) {
        return DownloadMode.getMode(context) == DownloadMode.ASYNC;
    }

    @Override
    public Object handle(DownloadContext context, DownloadHandlerChain chain) {
        DownloadOptions options = DownloadOptions.get(context);
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
