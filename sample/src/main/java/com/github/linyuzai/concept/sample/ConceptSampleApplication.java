package com.github.linyuzai.concept.sample;

import com.github.linyuzai.concept.sample.domain.DomainTest;
import com.github.linyuzai.concept.sample.inherit.bug6.Bug6Sample;
import com.github.linyuzai.concept.sample.mapqueue.MapQueueBugSample;
import com.github.linyuzai.concept.sample.mapqueue.MapQueueSample;
import com.github.linyuzai.concept.sample.throwsdemo.ThrowsDemo;
import com.github.linyuzai.connection.loadbalance.netty.sample.NettySampleServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.Serializable;
import java.nio.channels.Selector;
import java.util.*;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableSwagger2
@Slf4j
@SpringBootApplication(scanBasePackages = "com.github.linyuzai.concept.sample.connection.loadbalance.sse"
        , exclude = DataSourceAutoConfiguration.class)
public class ConceptSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConceptSampleApplication.class, args);
        //new MapQueueSample().start();
        //new ThrowsDemo().demo4throws();
        //new Bug6Sample().bug(null);
        //new MapQueueBugSample().start();
        //new DomainTest().test();
        //Selector.open();
        //new NettySampleServer().start();
    }
}
