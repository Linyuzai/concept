package com.github.linyuzai.plugin.core.concept;

import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public interface PluginDefinition {

    String getPath();

    long getSize();

    long getCreateTime();

    Object getVersion();

    /**
     * 获得输入流
     */
    @Nullable
    InputStream getInputStream();
}
