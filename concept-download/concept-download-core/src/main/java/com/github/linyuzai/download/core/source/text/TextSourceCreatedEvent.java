package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;

public class TextSourceCreatedEvent extends AbstractSourceCreatedEvent {

    public TextSourceCreatedEvent(DownloadContext context, TextSource source) {
        super(context, source);
    }
}
