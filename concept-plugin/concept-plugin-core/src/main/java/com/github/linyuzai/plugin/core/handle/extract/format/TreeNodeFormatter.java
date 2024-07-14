package com.github.linyuzai.plugin.core.handle.extract.format;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.tree.PluginTree;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于插件树节点的格式化器
 */
public abstract class TreeNodeFormatter<T> extends AbstractPluginFormatter<PluginTree.Node, T> {

    @Override
    public T doFormat(PluginTree.Node node, PluginContext context) {
        List<PluginTree.Node> nodes = new ArrayList<>();
        node.forEach(it -> {
            if (!it.isPluginNode()) {
                nodes.add(it);
            }
        });
        return doFormat(nodes, context);
    }

    /**
     * 格式化节点
     */
    public abstract T doFormat(List<PluginTree.Node> nodes, PluginContext context);
}
