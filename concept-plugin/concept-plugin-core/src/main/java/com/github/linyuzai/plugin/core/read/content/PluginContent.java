package com.github.linyuzai.plugin.core.read.content;

import com.github.linyuzai.plugin.core.read.PluginReadable;

import java.io.IOException;
import java.io.InputStream;

public interface PluginContent extends PluginReadable {

    InputStream getInputStream() throws IOException;
}
