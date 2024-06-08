package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@link Map} 转 {@link Map} 的格式器
 */
@Getter
@AllArgsConstructor
public class MapToMapFormatter extends TreeNodePluginFormatter<Map<Object, Object>> {

    /**
     * {@link Map} 的类型
     */
    private final Class<?> mapClass;

    public MapToMapFormatter() {
        this(Map.class);
    }

    /**
     * 格式化，根据 {@link Map} 类型实例化并添加 {@link Map} 的 entry
     *
     * @param nodes 被格式化的对象
     * @return {@link Map} 格式的插件
     */
    @Override
    public Map<Object, Object> formatNodes(List<PluginTree.Node> nodes) {
        Map<Object, Object> map = ReflectionUtils.newMap(mapClass);
        for (PluginTree.Node node : nodes) {
            map.put(node.getId(), node.getValue());
        }
        return map;
    }
}
