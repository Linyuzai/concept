package com.github.linyuzai.plugin.core.concept;

import java.io.InputStream;

public interface PluginDefinition {

    String getPath();

    long getSize();

    long getCreateTime();

    Object getVersion();

    /**
     * 获得输入流
     * <p>
     * 可能为null
     */
    InputStream getInputStream();
}
