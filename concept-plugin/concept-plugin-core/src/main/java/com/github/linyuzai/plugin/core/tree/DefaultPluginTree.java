package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.tree.trace.PluginTracer;
import com.github.linyuzai.plugin.core.tree.transform.PluginTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class DefaultPluginTree implements PluginTree, PluginTransformer, PluginTracer {

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
    public PluginTransformer getTransformer() {
        return this;
    }

    @Override
    public PluginTracer getTracer() {
        return this;
    }

    @Override
    public InboundStage create(PluginHandler handler) {
        return new TransformerStages(handler);
    }

    @Override
    public Collection<Object> getTraceIds() {
        return Collections.unmodifiableSet(traceMap.keySet());
    }

    @Override
    public HandleStage getTrace(Object id) {
        return traceMap.get(id);
    }

    protected DefaultNode createNode(Object id, String name, Object value, Node parent) {
        return new DefaultNode(id, name, value, this, parent);
    }

    @SuppressWarnings("unchecked")
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
            if (isContentNode()) {
                if (!predicate.test(this)) {
                    return null;
                }
                apply = function.apply(this);
            } else {
                apply = value;
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
            if (isContentNode() && !predicate.test(this)) {
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
        public DefaultNode create(Object id, String name, Object value) {
            DefaultNode node = createNode(id, name, value, this);
            children.add(node);
            return node;
        }

        protected boolean isContentNode() {
            return !(value instanceof Plugin);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public class TransformerStages implements
            InboundStage,
            PluginTransformer.TransformStage,
            OutboundStage {

        private final PluginHandler handler;

        private Node parameter;

        private Node result;

        @Override
        public PluginTransformer.TransformStage inbound(Node tree) {
            parameter = tree;
            return this;
        }

        @Override
        public PluginTransformer.TransformStage inboundKey(Object inboundKey) {
            HandleStage trace = getTracer().getTrace(inboundKey);
            if (trace == null) {
                throw new IllegalArgumentException("No plugin tree found: " + inboundKey);
            }
            parameter = trace.getTree().getRoot();
            return this;
        }

        @Override
        public OutboundStage transform(Function<Node, Node> transform) {
            result = transform.apply(parameter);
            return this;
        }

        @Override
        public Node outbound() {
            return result;
        }

        @Override
        public void outboundKey(Object outboundKey) {
            HandleStage trace = getTracer().getTrace(outboundKey);

        }
    }
}
