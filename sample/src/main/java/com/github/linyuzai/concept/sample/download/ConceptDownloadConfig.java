package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.log.StandardDownloadLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /*@Bean
    public SourceLoader sourceLoader() {
        return new BlockSchedulerSourceLoader(Schedulers.fromExecutor(Executors.newFixedThreadPool(5)));
    }*/
}
