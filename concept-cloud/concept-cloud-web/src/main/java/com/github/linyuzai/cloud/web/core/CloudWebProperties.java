package com.github.linyuzai.cloud.web.core;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "concept.cloud.web")
public class CloudWebProperties {

    private InterceptProperties intercept = new InterceptProperties();

    @Data
    public static class InterceptProperties {

        private boolean enabled = true;

        private RequestProperties request = new RequestProperties();

        private ResponseProperties response = new ResponseProperties();

        private ErrorProperties error = new ErrorProperties();

        private List<PredicateProperties> predicates;

        @Data
        public static class RequestProperties {

            private boolean enabled = true;

            private List<ScopePredicateProperties> predicates;
        }

        @Data
        public static class ResponseProperties {

            private boolean enabled = true;

            private List<ScopePredicateProperties> predicates;
        }

        @Data
        public static class ErrorProperties {

            private boolean enabled = true;

            private List<ScopePredicateProperties> predicates;
        }

        @Data
        public static class ScopePredicateProperties {

            private List<String> pathPatterns;

            private boolean negate = false;

            private int order = WebInterceptor.Order.PREDICATE;
        }

        @Data
        @EqualsAndHashCode(callSuper = true)
        public static class PredicateProperties extends ScopePredicateProperties {

            private List<WebInterceptor.Scope> scopes;
        }
    }
}
