package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.tree.trace.PluginTracer;
import com.github.linyuzai.plugin.core.tree.transform.PluginTransformer;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

public interface PluginTree {

    Node getRoot();

    PluginTransformer getTransformer();

    PluginTracer getTracer();

    interface Node {

        Object getId();

        Object getValue();

        Plugin getPlugin();

        Node getParent();

        Collection<Node> getChildren();

        <T, R> Node map(Function<T, R> function);

        <T> Node filter(Predicate<T> predicate);

        Node create(Object id, Object value, Plugin plugin);
    }
}
