package com.bytedance.juejin.pin.config;

import com.bytedance.juejin.domain.comment.CommentRepository;
import com.bytedance.juejin.domain.comment.CommentService;
import com.bytedance.juejin.pin.domain.comment.*;
import com.bytedance.juejin.pin.infrastructure.comment.mbp.MBPCommentIdGenerator;
import com.bytedance.juejin.pin.infrastructure.comment.mbp.MBPCommentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 评论配置
 */
@Configuration
public class DomainCommentConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CommentController commentController() {
        return new CommentController();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommentService commentService() {
        return new CommentService();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommentApplicationService commentApplicationService() {
        return new CommentApplicationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommentFacadeAdapter pinCommentFacadeAdapter() {
        return new CommentFacadeAdapterImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommentSearcher pinCommentSearcher() {
        return new CommentSearcherImpl();
    }

    /**
     * MyBatis-Plus 配置
     */
    @Configuration
    public static class MyBatisPlusConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public CommentIdGenerator pinCommentIdGenerator() {
            return new MBPCommentIdGenerator();
        }

        @Bean
        @ConditionalOnMissingBean
        public CommentRepository pinCommentRepository() {
            return new MBPCommentRepository();
        }
    }
}
