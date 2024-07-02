package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.metadata.property.MetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.PrefixMetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.StringArrayValueMetadataProperty;
import com.github.linyuzai.plugin.core.metadata.property.StringValueMetadataProperty;
import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

public interface JarPlugin extends ZipPlugin {

    interface MetadataProperties extends ZipPlugin.MetadataProperties {

        MetadataProperty<?> JAR = new PrefixMetadataProperty("jar");

        MetadataProperty<String> MODE = new StringValueMetadataProperty("mode", JAR);

        MetadataProperty<?> FILTER_CLASS = new PrefixMetadataProperty("class", FILTER);

        MetadataProperty<String[]> FILTER_CLASS_PATTERNS = new StringArrayValueMetadataProperty("patterns", FILTER_CLASS);
    }

    class Mode {

        public static final String FILE = "FILE";

        public static final String STREAM = "STREAM";
    }
}
