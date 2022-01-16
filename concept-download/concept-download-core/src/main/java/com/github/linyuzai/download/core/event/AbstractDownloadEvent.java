package com.github.linyuzai.download.core.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDownloadEvent implements DownloadEvent {

    private String message;
}
