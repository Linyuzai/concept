package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.zip.concept.ZipPlugin;

public interface JarPlugin extends ZipPlugin {

    interface PropertyKey {

        String PREFIX = Metadata.PropertyKey.PREFIX + "jar.";
    }

    class Mode {

        public interface PropertyKey {

            String MODE = JarPlugin.PropertyKey.PREFIX + "mode";
        }

        public static final String FILE = "FILE";

        public static final String STREAM = "STREAM";
    }
}
