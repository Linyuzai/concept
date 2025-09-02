package com.github.linyuzai.plugin.core.concept;

import java.io.IOException;
import java.io.InputStream;

public interface PluginDefinition {

    String getPath();

    long getSize();

    long getCreateTime();

    Object getVersion();

    interface Loadable {

        String getUrl();

        InputStream getInputStream() throws IOException;
    }
}
