package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.URL;

/**
 * zip插件
 */
public interface ZipPlugin extends Plugin {

    String SUFFIX = ".zip";

    URL getURL();

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends Plugin.StandardMetadata {

    }
}
