package com.github.linyuzai.plugin.autoconfigure;

import com.amazonaws.services.s3.AmazonS3;
import com.github.linyuzai.plugin.autoconfigure.preperties.PluginConceptProperties;
import com.github.linyuzai.plugin.core.storage.*;
import com.github.linyuzai.plugin.remote.aws.AmazonS3Storage;
import com.github.linyuzai.plugin.remote.aws.S3ClientStorage;
import com.github.linyuzai.plugin.remote.minio.MinioPluginStorage;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration(proxyBeanMethods = false)
public class PluginStorageConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PluginStorage.Filter pluginStorageFilter(PluginConceptProperties properties) {
        return new SuffixStorageFilter(properties.getStorage().getFilterSuffixes());
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "MEMORY", matchIfMissing = true)
    public static class MemoryConfiguration {

        @Bean(initMethod = "initialize")
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage() {
            return new MemoryPluginStorage();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "LOCAL")
    public static class LocalConfiguration {

        @Bean(initMethod = "initialize")
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties,
                                           PluginStorage.Filter filter) {
            String location = properties.getStorage().getLocation();
            String localLocation = StringUtils.hasText(location) ?
                    location : LocalPluginStorage.DEFAULT_LOCATION;
            return new LocalPluginStorage(localLocation, filter);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "MINIO")
    public static class MinioConfiguration {

        @Bean(initMethod = "initialize")
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties,
                                           PluginStorage.Filter filter,
                                           MinioClient minioClient) {
            String location = properties.getStorage().getLocation();
            String bucket = StringUtils.hasText(location) ?
                    location : RemotePluginStorage.DEFAULT_LOCATION;
            return new MinioPluginStorage(bucket, filter, minioClient);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "AWS_V1")
    public static class AmazonS3Configuration {

        @Bean(initMethod = "initialize")
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties,
                                           PluginStorage.Filter filter,
                                           AmazonS3 amazonS3) {
            String location = properties.getStorage().getLocation();
            String bucket = StringUtils.hasText(location) ?
                    location : RemotePluginStorage.DEFAULT_LOCATION;
            return new AmazonS3Storage(bucket, filter, amazonS3);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = "concept.plugin.storage.type", havingValue = "AWS_V2")
    public static class S3ClientConfiguration {

        @Bean(initMethod = "initialize")
        @ConditionalOnMissingBean
        public PluginStorage pluginStorage(PluginConceptProperties properties,
                                           PluginStorage.Filter filter,
                                           S3Client s3Client) {
            String location = properties.getStorage().getLocation();
            String bucket = StringUtils.hasText(location) ?
                    location : RemotePluginStorage.DEFAULT_LOCATION;
            return new S3ClientStorage(bucket, filter, s3Client);
        }
    }
}
