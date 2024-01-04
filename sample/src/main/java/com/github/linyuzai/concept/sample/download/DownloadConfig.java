package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.executor.DownloadExecutor;
import com.github.linyuzai.download.core.executor.SimpleDownloadExecutor;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.coroutines.loader.CoroutinesSourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
public class DownloadConfig {

    //@Bean
    public SourceLoader sourceLoader() {
        System.out.println("Use coroutines source loader");
        return new CoroutinesSourceLoader();
    }

    //@Bean
    public DownloadExecutor downloadExecutor() {
        return new SimpleDownloadExecutor(Executors.newCachedThreadPool());
    }
}
