package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public abstract class DownloadContextEvent extends AbstractDownloadEvent {

    @NonNull
    private final DownloadContext context;
}
