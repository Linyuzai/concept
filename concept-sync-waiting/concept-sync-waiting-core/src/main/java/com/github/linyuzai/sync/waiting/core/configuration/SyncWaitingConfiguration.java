package com.github.linyuzai.sync.waiting.core.configuration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 同步等待配置。
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SyncWaitingConfiguration {

    /**
     * 等待时间
     */
    private long waitingTime;

    /**
     * 排队时间
     */
    private long queuingTime;

    public static class Builder {

        private long waitingTime = 0L;

        private long queuingTime = 0L;

        public Builder waitingTime(long waitingTime) {
            this.waitingTime = waitingTime;
            return this;
        }

        public Builder queuingTime(long queuingTime) {
            this.queuingTime = queuingTime;
            return this;
        }

        public SyncWaitingConfiguration build() {
            SyncWaitingConfiguration configuration = new SyncWaitingConfiguration();
            configuration.waitingTime = waitingTime;
            configuration.queuingTime = queuingTime;
            return configuration;
        }
    }
}
