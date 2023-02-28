package com.github.linyuzai.cloud.web;

import lombok.Data;
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

        @Data
        public static class RequestProperties {

            private boolean enabled = true;

            private SkipProperties skip = new SkipProperties();
        }

        @Data
        public static class ResponseProperties {

            private boolean enabled = true;

            private SkipProperties skip = new SkipProperties();
        }

        @Data
        public static class ErrorProperties {

            private boolean enabled = true;
        }

        @Data
        public static class SkipProperties {

            private List<String> urls;

            private boolean swagger = true;
        }
    }
}
