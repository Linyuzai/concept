package com.github.linyuzai.download.core.scheduler;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ImmediateDownloadScheduler implements DownloadScheduler {

    @Override
    public Scheduler getScheduler() {
        return Schedulers.immediate();
    }
}
