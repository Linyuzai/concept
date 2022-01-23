package com.github.linyuzai.download.core.event;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class DownloadContextEvent extends AbstractDownloadEvent {

    @NonNull
    private final DownloadContext context;
}
