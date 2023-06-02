package $PACKAGE$.module.sample.config;

import $PACKAGE$.domain.sample.SampleRepository;
import $PACKAGE$.domain.sample.SampleService;
import $PACKAGE$.module.sample.domain.sample.*;
import $PACKAGE$.module.sample.infrastructure.sample.mbp.MBPSampleRepository;
import com.github.linyuzai.domain.mbp.MBPDomainIdGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 示例配置
 */
@Configuration
public class DomainSampleConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SampleController sampleController() {
        return new SampleController();
    }

    @Bean
    @ConditionalOnMissingBean
    public SampleService sampleService() {
        return new SampleService();
    }

    @Bean
    @ConditionalOnMissingBean
    public SampleApplicationService sampleApplicationService() {
        return new SampleApplicationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public SampleFacadeAdapter sampleFacadeAdapter() {
        return new SampleFacadeAdapterImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SampleSearcher sampleSearcher() {
        return new SampleSearcherImpl();
    }

    /**
     * MyBatis-Plus 配置
     */
    @Configuration
    public static class MyBatisPlusConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SampleIdGenerator sampleIdGenerator() {
            return MBPDomainIdGenerator.create(SampleIdGenerator.class);
        }

        @Bean
        @ConditionalOnMissingBean
        public SampleRepository sampleRepository() {
            return new MBPSampleRepository();
        }
    }
}
