package com.github.linyuzai.plugin.jar.autoload;

import com.github.linyuzai.plugin.core.autoload.PluginNotifier;
import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginConcept;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听 jar 的通知回调
 */
public class JarNotifier extends PluginNotifier {

    /**
     * 文件路径和 Plugin id 的映射关系
     */
    private final Map<String, Object> pathIdMapping = new ConcurrentHashMap<>();

    public JarNotifier(PluginConcept concept) {
        super(concept);
    }

    /**
     * 加载并添加映射关系
     *
     * @param o 文件路径
     */
    @SneakyThrows
    @Override
    public Plugin load(Object o) {
        String path = (String) o;
        Plugin plugin = concept.load(path);
        pathIdMapping.put(path, plugin.getId());
        return plugin;
    }

    /**
     * 卸载并移除映射关系
     *
     * @param o 文件路径
     */
    @Override
    public Plugin unload(Object o) {
        Object id = pathIdMapping.remove((String) o);
        if (id == null) {
            return null;
        }
        return concept.unload(id);
    }
}
