package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractSourceCreatedEvent;

public class FileSourceCreatedEvent extends AbstractSourceCreatedEvent {

    public FileSourceCreatedEvent(DownloadContext context, FileSource source) {
        super(context, source);
    }
}
