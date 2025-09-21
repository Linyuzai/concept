package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 默认插树仓储
 */
public class DefaultPluginTreeRepository implements PluginTreeRepository {

    private final Map<String, PluginTree> trees = newMap();

    /**
     * 获得插件树
     */
    @Override
    public PluginTree get(PluginDefinition definition) {
        return trees.get(definition.getPath());
    }

    /**
     * 添加插件树
     */
    @Override
    public void add(PluginTree tree) {
        if (tree == null) {
            return;
        }
        trees.put(tree.getDefinition().getPath(), tree);
    }

    /**
     * 移除插件树
     */
    @Override
    public PluginTree remove(PluginDefinition definition) {
        return trees.remove(definition.getPath());
    }

    /**
     * 插件树是否存在
     */
    @Override
    public boolean contains(PluginDefinition definition) {
        return trees.containsKey(definition.getPath());
    }

    @Override
    public Stream<PluginTree> stream() {
        return trees.values().stream();
    }

    protected Map<String, PluginTree> newMap() {
        return new ConcurrentHashMap<>();
    }
}
