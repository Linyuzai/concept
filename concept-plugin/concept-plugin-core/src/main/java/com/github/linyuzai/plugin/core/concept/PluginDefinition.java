package com.github.linyuzai.plugin.core.concept;

import java.io.InputStream;

public interface PluginDefinition {

    String getPath();

    long getSize();

    long getCreateTime();

    Object getVersion();

    InputStream getInputStream();

    interface Loadable {

        InputStream getInputStream();
    }
}
