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
}
