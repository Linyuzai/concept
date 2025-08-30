package com.github.linyuzai.concept.sample.plugin.v2;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.github.linyuzai.plugin.autoconfigure.EnablePluginConcept;
import com.github.linyuzai.plugin.core.handle.filter.EntryFilter;
import com.github.linyuzai.plugin.core.handle.filter.PluginFilter;
import com.github.linyuzai.plugin.jar.handle.filter.ClassFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Modifier;

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

    @Bean
    public AmazonS3 amazonS3() {
       return AmazonS3ClientBuilder.standard()
               .withCredentials(new AWSStaticCredentialsProvider(
                       new BasicAWSCredentials("minioadmin", "minioadmin")))
               .withEndpointConfiguration(new AwsClientBuilder
                       .EndpointConfiguration("http://localhost:9000",
                       ""))
               .withPathStyleAccessEnabled(true)
               .build();
    }

}
