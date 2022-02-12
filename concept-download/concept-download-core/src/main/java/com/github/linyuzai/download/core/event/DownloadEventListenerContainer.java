package com.github.linyuzai.download.core.event;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * {@link DownloadEventListener} 的组合监听器。
 */
public class DownloadEventListenerContainer implements DownloadEventListener {

    /**
     * {@link DownloadEventListener} 列表
     */
    @Getter
    private final List<DownloadEventListener> listeners;

    public DownloadEventListenerContainer(DownloadEventListener... listeners) {
        this(Arrays.asList(listeners));
    }

    public DownloadEventListenerContainer(Collection<DownloadEventListener> listeners) {
        this.listeners = new ArrayList<>(listeners);
    }

    @Override
    public void onEvent(Object event) {
        for (DownloadEventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
