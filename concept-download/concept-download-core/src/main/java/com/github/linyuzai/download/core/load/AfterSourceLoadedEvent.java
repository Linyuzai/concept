package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

/**
 * {@link Source} 加载完成后会发布该事件。
 */
@Getter
public class AfterSourceLoadedEvent extends AbstractLoadSourceEvent {

    public AfterSourceLoadedEvent(DownloadContext context, Source source) {
        super(context, source, "Source loaded");
    }
}
