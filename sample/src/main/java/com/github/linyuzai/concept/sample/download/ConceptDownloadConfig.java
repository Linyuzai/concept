package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.load.SchedulerSourceLoader;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.log.ProgressCalculationLogger;
import com.github.linyuzai.download.core.log.StandardDownloadLogger;
import com.github.linyuzai.download.core.log.TimeSpentCalculationLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;

@Configuration
public class ConceptDownloadConfig implements DownloadConfigurer {

    @Override
    public void configure(DownloadConfiguration configuration) {
        System.out.println("可以在这里覆盖配置文件的配置！");
    }

    @Bean
    public StandardDownloadLogger standardDownloadLogger() {
        return new StandardDownloadLogger();
    }

    @Bean
    public ProgressCalculationLogger progressCalculationLogger() {
        return new ProgressCalculationLogger();
    }

    @Bean
    public TimeSpentCalculationLogger timeSpentDownloadLogger() {
        return new TimeSpentCalculationLogger();
    }

    @Bean
    public SourceLoader sourceLoader() {
        return new SchedulerSourceLoader(Schedulers.fromExecutor(Executors.newFixedThreadPool(5)));
    }
}
