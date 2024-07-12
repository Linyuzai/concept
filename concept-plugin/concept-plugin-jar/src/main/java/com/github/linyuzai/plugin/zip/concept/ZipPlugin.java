package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.Data;
import lombok.EqualsAndHashCode;

public interface ZipPlugin extends Plugin {

    @Data
    @EqualsAndHashCode(callSuper = true)
    class StandardMetadata extends Plugin.StandardMetadata {

    }
}
