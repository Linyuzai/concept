package com.github.linyuzai.download.core.source.classpath;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class ClassPathSourceCreatedEvent extends SourceCreatedEvent {

    public ClassPathSourceCreatedEvent(DownloadContext context, ClassPathSource source) {
        super(context, source);
    }
}
