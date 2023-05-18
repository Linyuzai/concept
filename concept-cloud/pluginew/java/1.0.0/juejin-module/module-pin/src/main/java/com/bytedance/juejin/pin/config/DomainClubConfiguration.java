package com.bytedance.juejin.pin.config;

import com.bytedance.juejin.domain.club.ClubRepository;
import com.bytedance.juejin.domain.club.ClubService;
import com.bytedance.juejin.pin.domain.club.*;
import com.bytedance.juejin.pin.infrastructure.club.mbp.MBPClubIdGenerator;
import com.bytedance.juejin.pin.infrastructure.club.mbp.MBPClubRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 圈子配置
 */
@Configuration
public class DomainClubConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClubController pinClubController() {
        return new ClubController();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClubService pinClubService() {
        return new ClubService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClubApplicationService clubApplicationService() {
        return new ClubApplicationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClubFacadeAdapter pinClubFacadeAdapter() {
        return new ClubFacadeAdapterImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClubSearcher pinClubSearcher() {
        return new ClubSearcherImpl();
    }

    /**
     * MyBatis-Plus 配置
     */
    @Configuration
    public static class MyBatisPlusConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ClubIdGenerator pinClubIdGenerator() {
            return new MBPClubIdGenerator();
        }

        @Bean
        @ConditionalOnMissingBean
        public ClubRepository pinClubRepository() {
            return new MBPClubRepository();
        }
    }
}
