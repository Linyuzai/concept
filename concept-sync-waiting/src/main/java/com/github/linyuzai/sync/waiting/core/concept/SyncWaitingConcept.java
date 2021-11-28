package com.github.linyuzai.sync.waiting.core.concept;

import com.github.linyuzai.sync.waiting.core.caller.SyncCaller;
import com.github.linyuzai.sync.waiting.core.configuration.SyncWaitingConfiguration;

public interface SyncWaitingConcept {

    default <T> T waitSync(Object key, SyncCaller caller) {
        return waitSync(key, caller, 0);
    }

    default <T> T waitSync(Object key, SyncCaller caller, long waitingTime) {
        return waitSync(key, caller, waitingTime, 0);
    }

    default <T> T waitSync(Object key, SyncCaller caller, long waitingTime, long queuingTime) {
        SyncWaitingConfiguration configuration = new SyncWaitingConfiguration.Builder()
                .waitingTime(waitingTime)
                .queuingTime(queuingTime)
                .build();
        return waitSync(key, caller, configuration);
    }

    <T> T waitSync(Object key, SyncCaller caller, SyncWaitingConfiguration configuration);

    void notifyAsync(Object key, Object value);

    boolean isWaiting(Object key);
}
