package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.tree.trace.PluginTracer;
import com.github.linyuzai.plugin.core.tree.transform.PluginTransformer;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface PluginTree {

    Object getId();

    Node getRoot();

    PluginTransformer getTransformer();

    PluginTracer getTracer();

    interface Node {

        Object getId();

        String getName();

        Object getValue();

        PluginTree getTree();

        Node getParent();

        Collection<Node> getChildren();

        Node map(Function<Node, Object> function);

        Node map(Function<Node, Object> function, Predicate<Node> predicate);

        Node filter(Predicate<Node> predicate);

        void forEach(Consumer<Node> consumer);
    }

    interface NodeFactory {

        Node create(Object id, String name, Object value);
    }
}
