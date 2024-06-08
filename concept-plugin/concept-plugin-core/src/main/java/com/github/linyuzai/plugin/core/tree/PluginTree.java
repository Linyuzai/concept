package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.handle.PluginHandler;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface PluginTree {

    Object getId();

    Node getRoot();

    Transformer getTransformer();

    Tracer getTracer();

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

        boolean isPluginNode();
    }

    interface NodeFactory {

        Node create(Object id, String name, Object value);
    }

    interface Transformer {

        InboundStage create(PluginHandler handler);

        interface InboundStage {

            TransformStage inbound(Node node);

            TransformStage inboundKey(Object inboundKey);
        }

        interface TransformStage {

            OutboundStage transform(Function<Node, Node> transform);
        }

        interface OutboundStage {

            Node outbound();

            void outboundKey(Object outboundKey);
        }
    }

    interface Tracer {

        HandleStage getTrace(Object id);

        Stream<HandleStage> stream();

        interface HandleStage {

            PluginTree.Node getTreeRoot();

            PluginHandler getHandler();

            HandleStage next();
        }
    }
}
