package com.github.linyuzai.cloud.web.core;

import com.github.linyuzai.cloud.web.core.intercept.WebInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
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

        private PredicateProperties predicate = new PredicateProperties();

        @Data
        public static class RequestProperties {

            private boolean enabled = true;

            private PredicateProperties predicate = new PredicateProperties();
        }

        @Data
        public static class ResponseProperties {

            private boolean enabled = true;

            private PredicateProperties predicate = new PredicateProperties();
        }

        @Data
        public static class ErrorProperties {

            private boolean enabled = true;

            private PredicateProperties predicate = new PredicateProperties();
        }

        @Data
        public static class PredicateProperties {

            private List<RequestPathProperties> requestPath = Collections.emptyList();
        }

        @Data
        public static class RequestPathProperties {

            private List<String> patterns;

            private boolean negate = false;

            private int order = WebInterceptor.Orders.PREDICATE;
        }
    }
}
