package com.github.linyuzai.download.core.event;

import org.springframework.context.event.EventListener;

/**
 * 下载事件。
 * 下载过程中会发布各种事件，
 * 可以用 {@link DownloadEventListener} 或者 {@link EventListener} 监听事件。
 * <p>
 * Download events.
 * Various events will be published during downloading.
 * You can listen for events with {@link DownloadEventListener} or {@link EventListener}.
 */
public interface DownloadEvent {
}
