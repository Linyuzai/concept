package com.github.linyuzai.concept.sample.plugin.v2;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.linyuzai.concept.sample.plugin.CustomPlugin;
import com.github.linyuzai.plugin.autoconfigure.EnablePluginConcept;
import com.github.linyuzai.plugin.autoconfigure.observable.GenericPluginObservable;
import com.github.linyuzai.plugin.autoconfigure.observable.PluginObservable;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.filter.EntryFilter;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.endpoints.internal.DefaultS3EndpointProvider;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@EnablePluginConcept
@Configuration
public class PluginConfig {

    public static final String CLASS_NAME_FILTER = "com.example.jarplugin.sample.SampleCustomPlugin";

    public static final String CLASS_NAME_SAMPLE_FILTER = CLASS_NAME_FILTER;

    //@Bean
    public PluginFilter pluginFilter() {
        ClassFilter.modifier(Modifier::isFinal);
        return new EntryFilter("**/**.properties");
    }

    //@Bean
    public AmazonS3 amazonS3() {
       return AmazonS3ClientBuilder.standard()
               .withCredentials(new AWSStaticCredentialsProvider(
                       new BasicAWSCredentials("minioadmin", "minioadmin")))
               .withEndpointConfiguration(new AwsClientBuilder
                       .EndpointConfiguration("http://localhost:9090",
                       Regions.US_EAST_1.getName()))
               .withPathStyleAccessEnabled(true)
               .build();
    }

    //@Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create("http://localhost:9090"))
                .region(Region.AP_EAST_1) // 设置区域，对于MinIO通常可以忽略或设置为任意区域，因为MinIO不支持基于区域的路由。
                .credentialsProvider(StaticCredentialsProvider
                        .create(AwsBasicCredentials.create("minioadmin", "minioadmin"))) // 设置凭证提供者
                .build();
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://localhost:9090")
                .credentials("minioadmin", "minioadmin")
                .build();
    }

    @Bean
    public PluginObservable<String, CustomPlugin> pluginObservable() {
        return new GenericPluginObservable<String, CustomPlugin>() {

            @Override
            public String grouping(CustomPlugin plugin, PluginContext context) {
                return plugin.getClass().getName();
            }
        };
    }
}
