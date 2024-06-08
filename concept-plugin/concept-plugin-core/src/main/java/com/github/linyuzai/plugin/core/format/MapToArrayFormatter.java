package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.tree.PluginTree;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link Map} 转数组的格式器
 */
@Getter
@AllArgsConstructor
public class MapToArrayFormatter extends TreeNodePluginFormatter<Object> {

    /**
     * 数组的类型
     */
    private final Class<?> arrayClass;

    /**
     * 格式化，根据数组类型实例化并添加 {@link Map} 的 value
     *
     * @param nodes 被格式化的对象
     * @return 数组格式的插件
     */
    @Override
    public Object formatNodes(List<PluginTree.Node> nodes) {
        Object array = Array.newInstance(arrayClass, nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            PluginTree.Node node = nodes.get(i);
            Array.set(array, i, node.getValue());
        }
        return array;
    }
}
