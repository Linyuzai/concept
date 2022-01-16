package com.github.linyuzai.download.core.source.text;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class TextSourceCreatedEvent extends SourceCreatedEvent {

    public TextSourceCreatedEvent(DownloadContext context, TextSource source) {
        super(context, source);
    }
}
