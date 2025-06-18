package com.github.linyuzai.plugin.core.storage;

import java.io.IOException;
import java.io.InputStream;

public interface PluginDefinition {

    String getPath();

    long getSize();

    long getCreateTime();

    Object getVersion();

    Object getSource();

    interface Loadable {

        String getPath();

        InputStream getInputStream() throws IOException;
    }
}
