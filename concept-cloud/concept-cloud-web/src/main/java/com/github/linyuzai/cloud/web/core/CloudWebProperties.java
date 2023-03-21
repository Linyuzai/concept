package com.github.linyuzai.cloud.web.core;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * 配置属性
 */
@Data
@ConfigurationProperties(prefix = "concept.cloud.web")
public class CloudWebProperties {

    /**
     * 拦截配置
     */
    private InterceptProperties intercept = new InterceptProperties();

    /**
     * 拦截配置类
     */
    @Data
    public static class InterceptProperties {

        /**
         * 是否启用拦截
         */
        private boolean enabled = true;

        /**
         * 请求拦截配置
         */
        private RequestProperties request = new RequestProperties();

        /**
         * 响应拦截配置
         */
        private ResponseProperties response = new ResponseProperties();

        /**
         * 断言拦截配置
         */
        private PredicateProperties predicate = new PredicateProperties();

        /**
         * 请求拦截配置类
         */
        @Data
        public static class RequestProperties {

            /**
             * 是否启用请求拦截
             */
            private boolean enabled = true;

            /**
             * 请求断言拦截配置
             */
            private PredicateProperties predicate = new PredicateProperties();
        }

        /**
         * 响应拦截配置类
         */
        @Data
        public static class ResponseProperties {

            /**
             * 是否启用响应式拦截
             */
            private boolean enabled = true;

            /**
             * 响应断言拦截配置
             */
            private PredicateProperties predicate = new PredicateProperties();
        }

        /**
         * 断言拦截配置类
         */
        @Data
        public static class PredicateProperties {

            /**
             * 请求路径配置
             */
            private List<RequestPathProperties> requestPath = Collections.emptyList();
        }

        /**
         * 请求路径配置类
         */
        @Data
        public static class RequestPathProperties {

            /**
             * 路径匹配
             */
            private List<String> patterns;

            /**
             * 是否使用原始响应体作为返回值
             */
            private boolean useResponseBodyAsWebResult = true;

            /**
             * 取反
             */
            private boolean negate = false;

            /**
             * 排序值
             */
            private int order = WebInterceptor.Orders.PREDICATE;
        }
    }
}
