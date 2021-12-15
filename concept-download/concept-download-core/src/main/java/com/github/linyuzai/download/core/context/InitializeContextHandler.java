package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.handler.DownloadHandler;
import com.github.linyuzai.download.core.handler.DownloadHandlerChain;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class InitializeContextHandler implements DownloadHandler {

    @NonNull
    private List<DownloadContextInitializer> initializers;

    @Override
    public void handle(DownloadContext context, DownloadHandlerChain chain) throws IOException {
        for (DownloadContextInitializer initializer : initializers) {
            initializer.initialize(context);
        }
        chain.next(context);
    }

    @Override
    public int getOrder() {
        return ORDER_INITIALIZE_CONTEXT;
    }
}
