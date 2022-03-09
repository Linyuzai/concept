package com.github.linyuzai.plugin.jar;

import com.github.linyuzai.plugin.core.adapter.PluginAdapter;
import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.SneakyThrows;

import java.net.URL;

public class JarPluginAdapter implements PluginAdapter {

    @Override
    public Plugin adapt(Object o) {
        if (o instanceof URL && "jar".equals(((URL) o).getProtocol())) {
            return new JarPlugin((URL) o);
        }
        return null;
    }

    @SneakyThrows
    public URL parseURL(String jarPath) {
        String url;
        if (jarPath.startsWith("http")) {
            if (jarPath.endsWith("/")) {
                jarPath = jarPath.substring(0, jarPath.length() - 1);
            }
            url = "jar:" + jarPath + "!/";
        } else {
            if (jarPath.startsWith("/")) {
                jarPath = jarPath.substring(1);
            }
            url = "jar:file:/" + jarPath + "!/";
        }
        return new URL(url);
    }
}
