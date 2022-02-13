package com.github.linyuzai.download.core.source.file;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.AbstractCreateSourceEvent;

/**
 * {@link FileSource} 创建完成后会发布该事件。
 */
public class FileSourceCreatedEvent extends AbstractCreateSourceEvent {

    public FileSourceCreatedEvent(DownloadContext context, FileSource source) {
        super(context, source);
    }
}
