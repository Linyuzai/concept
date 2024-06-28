package com.github.linyuzai.plugin.autoconfigure.preperties;

import com.github.linyuzai.plugin.core.autoload.location.PluginLocation;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "concept.plugin")
public class PluginConceptProperties {

    private AutoloadProperties autoload = new AutoloadProperties();

    private JarProperties jar = new JarProperties();

    private ManagementProperties management = new ManagementProperties();

    @Data
    public static class AutoloadProperties {

        private boolean enabled = true;

        private LocationProperties location = new LocationProperties();

        @Data
        public static class LocationProperties {

            private String basePath = PluginLocation.DEFAULT_BASE_PATH;
        }
    }

    @Data
    public static class JarProperties {

        private String mode = JarPlugin.Mode.STREAM;
    }

    @Data
    public static class ManagementProperties {

        private AuthorizationProperties authorization = new AuthorizationProperties();

        private HeaderProperties header = new HeaderProperties();

        private FooterProperties footer = new FooterProperties();

        @Data
        public static class AuthorizationProperties {

            private String password;
        }

        @Data
        public static class HeaderProperties {

            private boolean display = true;

            private TitleProperties title = new TitleProperties();

            private GithubCornerProperties githubCorner = new GithubCornerProperties();

            @Data
            public static class TitleProperties {

                private boolean display = true;

                private String text = "Concept Plugin";
            }

            @Data
            public static class GithubCornerProperties {

                private boolean display = true;
            }
        }

        @Data
        public static class FooterProperties {

            private boolean display = true;
        }
    }
}
