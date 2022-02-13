package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.event.DownloadEvent;
import com.github.linyuzai.download.core.load.DefaultSourceLoader;
import com.github.linyuzai.download.core.load.SchedulerSourceLoader;
import com.github.linyuzai.download.core.log.ProgressCalculationLogger;
import com.github.linyuzai.download.core.log.StandardDownloadLogger;
import com.github.linyuzai.download.core.log.TimeSpentCalculationLogger;
import com.github.linyuzai.download.coroutines.loader.CoroutinesSourceLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
public class ConceptDownloadConfig implements DownloadConfigurer {

    @Override
    public void configure(DownloadConfiguration configuration) {
        System.out.println("可以在这里覆盖配置文件的配置！");
        //configuration.getLogger().getTimeSpent().setEnabled(true);
    }

    //@Bean
    public StandardDownloadLogger standardDownloadLogger() {
        return new StandardDownloadLogger();
    }

    //@Bean
    public ProgressCalculationLogger progressCalculationLogger() {
        return new ProgressCalculationLogger();
    }

    //@Bean
    public TimeSpentCalculationLogger timeSpentDownloadLogger() {
        return new TimeSpentCalculationLogger();
    }

    //@Bean
    public SchedulerSourceLoader schedulerSourceLoader() {
        return new SchedulerSourceLoader(Schedulers.fromExecutor(Executors.newFixedThreadPool(2)));
    }

    //@Bean
    public CoroutinesSourceLoader coroutinesSourceLoader() {
        return new CoroutinesSourceLoader();
    }

    @EventListener
    public void onDownloadEvent(DownloadEvent event) {
        //System.out.println(event);
    }
}
