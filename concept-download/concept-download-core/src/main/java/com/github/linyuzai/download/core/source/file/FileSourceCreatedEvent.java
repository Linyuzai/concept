package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.SourceCreatedEvent;

public class FileSourceCreatedEvent extends SourceCreatedEvent {

    public FileSourceCreatedEvent(DownloadContext context, FileSource source) {
        super(context, source);
    }
}
