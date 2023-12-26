package com.github.linyuzai.download.core.event;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link DownloadEvent} 的抽象类。
 */
@Getter
@Setter
public abstract class AbstractDownloadEvent implements DownloadEvent {

    private long timestamp = System.currentTimeMillis();
}
