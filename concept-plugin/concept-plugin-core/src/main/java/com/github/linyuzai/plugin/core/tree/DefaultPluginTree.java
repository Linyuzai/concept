package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class DefaultPluginTree implements PluginTree, PluginTree.Transformer, PluginTree.Tracer {

    private final Node root;

    private final Map<Object, HandleStage> traceMap = new LinkedHashMap<>();

    public DefaultPluginTree(Plugin plugin) {
        root = createNode(plugin.getId(), "", plugin, null);
    }

    @Override
    public Object getId() {
        return root.getId();
    }

    @Override
    public Transformer getTransformer() {
        return this;
    }

    @Override
    public Tracer getTracer() {
        return this;
    }

    @Override
    public InboundStage create(PluginHandler handler) {
        return new TransformerStages(handler);
    }

    @Override
    public HandleStage getTrace(Object id) {
        return traceMap.get(id);
    }

    @Override
    public Stream<HandleStage> stream() {
        return traceMap.values().stream();
    }

    protected DefaultNode createNode(Object id, String name, Object value, Node parent) {
        return new DefaultNode(id, name, value, this, parent);
    }

    @Getter
    @RequiredArgsConstructor
    public class DefaultNode implements Node, NodeFactory {

        private final Object id;

        private final String name;

        private final Object value;

        private final PluginTree tree;

        private final Node parent;

        private final Collection<Node> children = new ArrayList<>();

        @Override
        public Node map(Function<Node, Object> function) {
            return doMap(null, function, node -> true);
        }

        @Override
        public Node map(Function<Node, Object> function, Predicate<Node> predicate) {
            return doMap(null, function, predicate);
        }

        protected Node doMap(Node parent, Function<Node, Object> function, Predicate<Node> predicate) {
            Object apply;
            if (isPluginNode()) {
                apply = value;
            } else {
                if (!predicate.test(this)) {
                    return null;
                }
                apply = function.apply(this);
            }
            DefaultNode node = createNode(id, name, apply, parent);
            List<Node> collect = children.stream()
                    .map(it -> doMap(node, function, predicate))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            node.children.addAll(collect);
            return node;
        }

        @Override
        public Node filter(Predicate<Node> predicate) {
            return doFilter(null, predicate);
        }

        public Node doFilter(Node parent, Predicate<Node> predicate) {
            if (!isPluginNode() && !predicate.test(this)) {
                return null;
            }
            DefaultNode node = createNode(id, name, value, parent);
            List<Node> collect = children.stream()
                    .map(it -> doFilter(parent, predicate))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            node.children.addAll(collect);
            return node;
        }

        @Override
        public void forEach(Consumer<Node> consumer) {
            consumer.accept(this);
            children.forEach(it -> it.forEach(consumer));
        }

        @Override
        public boolean isPluginNode() {
            return value instanceof Plugin;
        }

        @Override
        public DefaultNode create(Object id, String name, Object value) {
            DefaultNode node = createNode(id, name, value, this);
            children.add(node);
            return node;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public class TransformerStages implements InboundStage, TransformStage, OutboundStage {

        private final PluginHandler handler;

        private Node inbound;

        private Node outbound;

        @Override
        public Transformer.TransformStage inbound(Node node) {
            inbound = node;
            return this;
        }

        @Override
        public Transformer.TransformStage inboundKey(Object inboundKey) {
            HandleStage trace = traceMap.get(inboundKey);
            if (trace == null) {
                throw new IllegalArgumentException("No plugin tree found: " + inboundKey);
            }
            inbound = trace.getTreeRoot();
            return this;
        }

        @Override
        public OutboundStage transform(Function<Node, Node> transformer) {
            outbound = transformer.apply(inbound);
            return this;
        }

        @Override
        public Node outbound() {
            return outbound;
        }

        @Override
        public void outboundKey(Object outboundKey) {
            traceMap.compute(outboundKey, (k, trace) -> new TracerStages(outbound, handler, trace));
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class TracerStages implements HandleStage {

        private final Node treeRoot;

        private final PluginHandler handler;

        private final HandleStage next;

        @Override
        public HandleStage next() {
            return next;
        }
    }
}
