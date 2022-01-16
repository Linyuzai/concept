package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.context.DownloadContextInitializer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DownloadEventPublisherInitializer implements DownloadContextInitializer {

    private DownloadEventPublisher eventPublisher;

    @Override
    public void initialize(DownloadContext context) {
        DownloadEventListener listener = context.getOptions().getEventListener();
        DownloadEventPublisher publisherToUse = listener == null ? eventPublisher :
                new DownloadEventPublisherDelegate(eventPublisher, listener);
        context.set(DownloadEventPublisher.class, publisherToUse);
    }
}
