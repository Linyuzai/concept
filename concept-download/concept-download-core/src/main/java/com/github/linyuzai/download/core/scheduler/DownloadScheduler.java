package com.github.linyuzai.download.core.scheduler;

import reactor.core.scheduler.Scheduler;

public interface DownloadScheduler {

    Scheduler getScheduler();
}
