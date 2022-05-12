package com.github.linyuzai.connection.loadbalance.autoconfigure;

import com.github.linyuzai.connection.loadbalance.core.event.EventPublishErrorHandler;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class LoggerEventPublishErrorHandler implements EventPublishErrorHandler {
    @Override
    public void onEventPublishError(Object event, Throwable e) {
        log.error("WebSocket event publish error", e);
    }
}
