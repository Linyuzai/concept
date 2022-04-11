package com.github.linyuzai.plugin.jar.autoload;

import com.github.linyuzai.plugin.core.autoload.PluginNotifier;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听 jar 的通知回调
 */
public class JarNotifier extends PluginNotifier {

    /**
     * 文件路径和 {@link URL} 的映射关系
     */
    private final Map<String, URL> pathUrlMapping = new ConcurrentHashMap<>();

    public JarNotifier(JarPluginConcept concept) {
        super(concept);
    }

    /**
     * 加载并添加映射关系
     *
     * @param o 文件路径
     */
    @Override
    public Plugin load(Object o) {
        String path = (String) o;
        JarPlugin plugin = (JarPlugin) concept.load(path);
        pathUrlMapping.put(path, plugin.getUrl());
        return plugin;
    }

    /**
     * 卸载并移除映射关系
     *
     * @param o 文件路径
     */
    @Override
    public Plugin unload(Object o) {
        URL url = pathUrlMapping.remove((String) o);
        if (url == null) {
            return null;
        }
        return concept.unload(url);
    }
}
