package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
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
    public InboundStage create(Object handler) {
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
            return doMap(this, null, function, node -> true);
        }

        @Override
        public Node map(Function<Node, Object> function, Predicate<Node> predicate) {
            return doMap(this, null, function, predicate);
        }

        protected Node doMap(Node node, Node parent, Function<Node, Object> function, Predicate<Node> predicate) {
            Object apply;
            if (isTransformable(node)) {
                if (!predicate.test(node)) {
                    return null;
                }
                apply = function.apply(node);
            } else {
                apply = node.getValue();
            }
            DefaultNode create = createNode(node.getId(), node.getName(), apply, parent);
            if (!node.getChildren().isEmpty()) {
                List<Node> collect = node.getChildren().stream()
                        .map(it -> doMap(it, create, function, predicate))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                create.children.addAll(collect);
            }
            return create;
        }

        @Override
        public Node filter(Predicate<Node> predicate) {
            return doFilter(this, null, predicate);
        }

        public Node doFilter(Node node, Node parent, Predicate<Node> predicate) {
            if (isTransformable(node) && !predicate.test(node)) {
                return null;
            }
            DefaultNode create = createNode(node.getId(), node.getName(), node.getValue(), parent);
            if (!node.getChildren().isEmpty()) {
                List<Node> collect = node.getChildren().stream()
                        .map(it -> doFilter(it, create, predicate))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                create.children.addAll(collect);
            }
            return create;
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

        protected boolean isTransformable(Node node) {
            if (node.isPluginNode()) {
                return false;
            }
            /*if (node.getValue() instanceof Plugin.Entry) {
                Plugin.Entry entry = (Plugin.Entry) node.getValue();
                if (entry.getContent() == null) {
                    return false;
                }
            }*/
            return true;
        }

        @Override
        public DefaultNode create(Object id, String name, Object value) {
            DefaultNode node = createNode(id, name, value, this);
            children.add(node);
            return node;
        }

        @Override
        public String toString() {
            return "PluginNode(" + id + ")";
        }
    }

    @Getter
    @RequiredArgsConstructor
    public class TransformerStages implements InboundStage, TransformStage, OutboundStage {

        private final Object handler;

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
            inbound = trace.getNode();
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

        private final Node node;

        private final Object handler;

        private final HandleStage next;

        @Override
        public HandleStage next() {
            return next;
        }
    }
}
