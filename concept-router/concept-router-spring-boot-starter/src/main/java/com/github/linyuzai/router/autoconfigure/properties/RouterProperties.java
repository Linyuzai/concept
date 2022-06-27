package com.github.linyuzai.router.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

@Data
@ConfigurationProperties(prefix = "concept.router")
public class RouterProperties {

    private boolean enabled = true;

    private RepositoryProperties repository = new RepositoryProperties();

    private LoggerProperties logger = new LoggerProperties();

    private ManagementProperties management = new ManagementProperties();

    @Data
    public static class RepositoryProperties {

        private RepositoryType type = RepositoryType.LOCAL;

        private LocalProperties local = new LocalProperties();

        public enum RepositoryType {

            MEMORY, LOCAL
        }

        @Data
        public static class LocalProperties {

            private String path = new File(System.getProperty("user.home"), "concept/router").getAbsolutePath();
        }
    }

    @Data
    public static class LoggerProperties {

        private boolean enabled = true;
    }

    @Data
    public static class ManagementProperties {

        private boolean enabled = true;
    }
}
