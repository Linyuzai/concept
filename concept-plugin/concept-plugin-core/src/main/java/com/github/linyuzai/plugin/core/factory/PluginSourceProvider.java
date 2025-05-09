package com.github.linyuzai.plugin.core.factory;

import java.io.IOException;
import java.io.InputStream;

public interface PluginSourceProvider {

    String getKey();

    InputStream getInputStream() throws IOException;
}
