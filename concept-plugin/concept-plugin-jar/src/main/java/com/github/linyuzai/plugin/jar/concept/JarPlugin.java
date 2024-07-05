package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipPlugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

public interface JarPlugin extends ZipPlugin {

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends ZipPlugin.StandardMetadata {

        private JarMetadata jar = new JarMetadata();

        //private FilterMetadata filter = new FilterMetadata();

        @Data
        public static class JarMetadata {

            private String mode;
        }

        /*@Data
        @EqualsAndHashCode(callSuper = true)
        public static class FilterMetadata extends ZipPlugin.StandardMetadata.FilterMetadata {

            private ClassNameMetadata className = new ClassNameMetadata();

            @Data
            public static class ClassNameMetadata {

                private Set<String> patterns;
            }
        }*/
    }

    /*interface MetadataProperties extends ZipPlugin.MetadataProperties {

        MetadataProperty<?> JAR = new PrefixMetadataProperty("jar");

        MetadataProperty<String> MODE = new StringValueMetadataProperty("mode", JAR);

        MetadataProperty<?> FILTER_CLASS = new PrefixMetadataProperty("class", FILTER);

        MetadataProperty<String[]> FILTER_CLASS_PATTERNS = new StringArrayValueMetadataProperty("patterns", FILTER_CLASS);
    }*/

    class Mode {

        public static final String FILE = "FILE";

        public static final String STREAM = "STREAM";
    }
}
