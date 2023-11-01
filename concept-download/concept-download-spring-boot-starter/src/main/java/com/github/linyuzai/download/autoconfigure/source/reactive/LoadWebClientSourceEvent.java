package com.github.linyuzai.download.autoconfigure.source.reactive;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.load.AbstractLoadSourceEvent;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 使用 {@link WebClient} 加载时会发布该事件。
 */
public class LoadWebClientSourceEvent extends AbstractLoadSourceEvent {

    public LoadWebClientSourceEvent(DownloadContext context, WebClientSource source) {
        super(context, source, "Load " + source.getDescription() + " using WebClient(webflux)");
    }
}
