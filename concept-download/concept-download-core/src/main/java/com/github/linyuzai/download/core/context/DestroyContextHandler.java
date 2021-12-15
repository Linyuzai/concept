package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class DestroyContextHandler implements DownloadHandler {

    @NonNull
    private List<DownloadContextDestroyer> destroyers;

    @Override
    public void handle(DownloadContext context, DownloadHandlerChain chain) throws IOException {
        for (DownloadContextDestroyer destroyer : destroyers) {
            destroyer.destroy(context);
        }
        chain.next(context);
        context.destroy();
    }

    @Override
    public int getOrder() {
        return ORDER_DESTROY_CONTEXT;
    }
}
