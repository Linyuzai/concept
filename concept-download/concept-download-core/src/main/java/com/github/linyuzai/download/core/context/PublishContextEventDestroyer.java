package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadEventPublisher;

public class PublishContextEventDestroyer implements DownloadContextDestroyer {

    @Override
    public void destroy(DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new AfterContextDestroyedEvent(context));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
