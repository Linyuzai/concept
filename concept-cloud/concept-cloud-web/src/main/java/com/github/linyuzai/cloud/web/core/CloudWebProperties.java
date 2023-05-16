package com.github.linyuzai.cloud.web.core;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * 配置属性
 */
@Data
@ConfigurationProperties(prefix = "concept.cloud.web")
public class CloudWebProperties {

    /**
     * i18n 配置
     */
    private I18nProperties i18n = new I18nProperties();

    /**
     * i18n 配置类
     */
    @Data
    public static class I18nProperties {

        /**
         * 是否启用 i18n
         */
        private boolean enabled = true;

        /**
         * Comma-separated list of basenames (essentially a fully-qualified classpath
         * location), each following the ResourceBundle convention with relaxed support for
         * slash based locations. If it doesn't contain a package qualifier (such as
         * "org.mypackage"), it will be resolved from the classpath root.
         */
        private String basename;

        /**
         * Message bundles encoding.
         */
        private Charset encoding = StandardCharsets.UTF_8;

        /**
         * Loaded resource bundle files cache duration. When not set, bundles are cached
         * forever. If a duration suffix is not specified, seconds will be used.
         */
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration cacheDuration;

        /**
         * Whether to fall back to the system Locale if no files for a specific Locale have
         * been found. if this is turned off, the only fallback will be the default file (e.g.
         * "messages.properties" for basename "messages").
         */
        private boolean fallbackToSystemLocale = false;

        /**
         * Whether to always apply the MessageFormat rules, parsing even messages without
         * arguments.
         */
        private boolean alwaysUseMessageFormat = false;

        /**
         * Whether to use the message code as the default message instead of throwing a
         * "NoSuchMessageException". Recommended during development only.
         */
        private boolean useCodeAsDefaultMessage = true;
    }

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
