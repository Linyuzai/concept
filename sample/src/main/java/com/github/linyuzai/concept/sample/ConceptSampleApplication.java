package com.github.linyuzai.concept.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@Slf4j
@SpringBootApplication(scanBasePackages = "com.github.linyuzai.concept.sample.plugin.v2", exclude = DataSourceAutoConfiguration.class)
public class ConceptSampleApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConceptSampleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }
}
