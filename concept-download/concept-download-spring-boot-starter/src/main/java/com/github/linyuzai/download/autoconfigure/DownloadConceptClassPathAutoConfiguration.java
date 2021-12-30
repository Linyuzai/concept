package com.github.linyuzai.download.autoconfigure;

import com.github.linyuzai.download.classpath.ClassPathPrefixSourceFactory;
import com.github.linyuzai.download.classpath.ClassPathResourceSourceFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * ClassPath的配置 / Configuration of class path
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DownloadConceptAutoConfiguration.class)
@ConditionalOnClass({ClassPathResourceSourceFactory.class, ClassPathPrefixSourceFactory.class})
public class DownloadConceptClassPathAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ClassPathResource.class)
    public ClassPathResourceSourceFactory classPathResourceOriginalSourceFactory() {
        return new ClassPathResourceSourceFactory();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(ClassPathResource.class)
    public ClassPathPrefixSourceFactory classPathPrefixOriginalSourceFactory() {
        return new ClassPathPrefixSourceFactory();
    }
}
