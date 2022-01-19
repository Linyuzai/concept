package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;

public class ClassPathSourceCreatedEvent extends AbstractSourceCreatedEvent {

    public ClassPathSourceCreatedEvent(DownloadContext context, ClassPathSource source) {
        super(context, source);
    }
}
