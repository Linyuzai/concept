package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 转 {@link Map} 的格式器
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapFormatter extends TreeNodeFormatter<Map<Object, Object>> {

    /**
     * {@link Map} 的类型
     */
    private Class<?> mapClass = Map.class;

    @Override
    public Map<Object, Object> doFormat(List<PluginTree.Node> nodes, PluginContext context) {
        Map<Object, Object> map = ReflectionUtils.newMap(mapClass);
        for (PluginTree.Node node : nodes) {
            map.put(node.getId(), node.getValue());
        }
        return map;
    }
}
