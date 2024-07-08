package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

public interface JarPlugin extends ZipPlugin {

    PluginClassLoader getPluginClassLoader();

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends ZipPlugin.StandardMetadata {

        private JarMetadata jar = new JarMetadata();

        @Data
        public static class JarMetadata {

            private String mode;
        }
    }

    class Mode {

        public static final String FILE = "FILE";

        public static final String STREAM = "STREAM";
    }
}
