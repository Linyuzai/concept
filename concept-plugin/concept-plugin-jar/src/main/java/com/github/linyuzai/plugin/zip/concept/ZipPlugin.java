package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * zip插件
 */
public interface ZipPlugin extends Plugin {

    String SUFFIX_ZIP = ".zip";

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends Plugin.StandardMetadata {

    }
}
