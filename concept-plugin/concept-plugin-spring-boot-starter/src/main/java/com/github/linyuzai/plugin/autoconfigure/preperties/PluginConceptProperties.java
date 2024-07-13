package com.github.linyuzai.plugin.autoconfigure.preperties;

import com.github.linyuzai.plugin.core.autoload.location.LocalPluginLocation;
import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.plugin")
public class PluginConceptProperties {

    private MetadataProperties metadata = new MetadataProperties();

    private AutoloadProperties autoload = new AutoloadProperties();

    private LoggerProperties logger = new LoggerProperties();

    private JarProperties jar = new JarProperties();

    private ManagementProperties management = new ManagementProperties();

    @Data
    public static class MetadataProperties {

        private Class<? extends Plugin.StandardMetadata> standardType = JarPlugin.StandardMetadata.class;
    }

    @Data
    public static class AutoloadProperties {

        private boolean enabled = true;

        private LocationProperties location = new LocationProperties();

        @Data
        public static class LocationProperties {

            private String basePath = LocalPluginLocation.DEFAULT_BASE_PATH;
        }
    }

    @Data
    public static class LoggerProperties {

        private StandardProperties standard = new StandardProperties();

        private ErrorProperties error = new ErrorProperties();

        @Data
        public static class StandardProperties {

            private boolean enabled = true;
        }

        @Data
        public static class ErrorProperties {

            private boolean enabled = true;
        }
    }

    @Data
    public static class JarProperties {

        private Mode mode = Mode.STREAM;

        public enum Mode {

            STREAM, FILE
        }
    }

    @Data
    public static class ManagementProperties {

        private AuthorizationProperties authorization = new AuthorizationProperties();

        private GithubCornerProperties githubCorner = new GithubCornerProperties();

        private HeaderProperties header = new HeaderProperties();

        private FooterProperties footer = new FooterProperties();

        @Data
        public static class AuthorizationProperties {

            private String password;
        }

        @Data
        public static class GithubCornerProperties {

            private boolean display = true;
        }

        @Data
        public static class HeaderProperties {

            private boolean display = true;

            private TitleProperties title = new TitleProperties();

            @Data
            public static class TitleProperties {

                private boolean display = true;

                private String text = "Concept Plugin";
            }
        }

        @Data
        public static class FooterProperties {

            private boolean display = true;
        }
    }
}
