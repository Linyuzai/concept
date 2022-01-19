package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import lombok.Getter;

@Getter
public class AfterSourceLoadedEvent extends AbstractSourceLoadedEvent {

    public AfterSourceLoadedEvent(DownloadContext context, Source source) {
        super(context, source, "Source loaded");
    }
}
