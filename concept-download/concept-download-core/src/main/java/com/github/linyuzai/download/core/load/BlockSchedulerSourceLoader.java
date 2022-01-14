package com.github.linyuzai.download.core.load;

import com.github.linyuzai.download.core.context.DownloadContext;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import reactor.core.scheduler.Scheduler;

@AllArgsConstructor
public class BlockSchedulerSourceLoader extends BlockSourceLoader{

    @NonNull
    private Scheduler scheduler;

    @Override
    public Scheduler getScheduler(DownloadContext context) {
        return scheduler;
    }
}
