package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.classpath.ClassPathPrefixOriginalSourceFactory;
import com.github.linyuzai.download.classpath.ClassPathResourceOriginalSourceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass({ClassPathResourceOriginalSourceFactory.class, ClassPathPrefixOriginalSourceFactory.class})
public class DownloadConceptClassPathAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ClassPathResource.class)
    public ClassPathResourceOriginalSourceFactory classPathResourceOriginalSourceFactory() {
        return new ClassPathResourceOriginalSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ClassPathResource.class)
    public ClassPathPrefixOriginalSourceFactory classPathPrefixOriginalSourceFactory() {
        return new ClassPathPrefixOriginalSourceFactory();
    }
}
