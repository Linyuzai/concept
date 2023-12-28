package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.coroutines.loader.CoroutinesSourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DownloadConfig {

    //@Bean
    public SourceLoader sourceLoader() {
        System.out.println("Use coroutines source loader");
        return new CoroutinesSourceLoader();
    }
}
