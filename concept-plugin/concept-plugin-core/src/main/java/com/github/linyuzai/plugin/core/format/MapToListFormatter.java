package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.tree.PluginTree;
import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link Map} 转 {@link List} 的格式器
 */
@Getter
@AllArgsConstructor
public class MapToListFormatter extends TreeNodePluginFormatter<List<Object>> {

    /**
     * {@link List} 的类型
     */
    private final Class<?> listClass;

    public MapToListFormatter() {
        this(List.class);
    }

    /**
     * 格式化，根据 {@link List} 类型实例化并添加 {@link Map} 的 value
     *
     * @param nodes 被格式化的对象
     * @return {@link List} 格式的插件
     */
    @Override
    public List<Object> formatNodes(List<PluginTree.Node> nodes) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(nodes.stream()
                .map(PluginTree.Node::getValue)
                .collect(Collectors.toList()));
        return list;
    }
}
