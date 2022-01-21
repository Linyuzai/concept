package com.github.linyuzai.download.core.context;

import com.github.linyuzai.download.core.event.DownloadEventPublisher;

public class PublishContextEventInitializer implements DownloadContextInitializer {

    @Override
    public void initialize(DownloadContext context) {
        DownloadEventPublisher publisher = context.get(DownloadEventPublisher.class);
        publisher.publish(new AfterContextInitializedEvent(context));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }
}
