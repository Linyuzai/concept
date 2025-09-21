package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 默认插件树
 */
@Getter
@RequiredArgsConstructor
public class DefaultPluginTree implements PluginTree, PluginTree.Transformer, PluginTree.Tracer {

    private final PluginDefinition definition;

    private final Node root;

    /**
     * 转换映射
     */
    private final Map<Object, List<TransformEntry>> transformMap = new LinkedHashMap<>();

    /**
     * 追踪链
     */
    private TracerStages trace;

    /**
     * 当前追踪节点
     */
    private TracerStages current;

    public DefaultPluginTree(Plugin plugin) {
        definition = plugin.getDefinition();
        root = createNode(plugin.getDefinition().getPath(), "", plugin, null);
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
        public int getSize() {
            return getSize(node -> true);
        }

        @Override
        public int getSize(Predicate<Node> predicate) {
            int size = 0;
            if (predicate.test(this)) {
                size++;
            }
            for (Node child : children) {
                size += child.getSize(predicate);
            }
            return size;
        }

        @Override
        public Node map(Function<Node, Object> function) {
            return doMap(this, null, function, node -> true);
        }

        @Override
        public Node map(Function<Node, Object> function, Predicate<Node> predicate) {
            return doMap(this, null, function, predicate);
        }

        protected Node doMap(Node node, Node parent, Function<Node, Object> function, Predicate<Node> predicate) {
            //转换当前节点
            Object apply;
            if (isTransformable(node)) {
                if (!predicate.test(node)) {
                    return null;
                }
                apply = function.apply(node);
            } else {
                apply = node.getValue();
            }
            //创建当前节点的对应节点并使用转换后的值
            DefaultNode create = createNode(node.getId(), node.getName(), apply, parent);
            if (!node.getChildren().isEmpty()) {
                //转换子节点
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
            //过滤当前节点
            if (isTransformable(node) && !predicate.test(node)) {
                return null;
            }
            //拷贝当前节点
            DefaultNode create = createNode(node.getId(), node.getName(), node.getValue(), parent);
            if (!node.getChildren().isEmpty()) {
                //过滤子节点
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
            List<TransformEntry> entries = transformMap.getOrDefault(inboundKey, Collections.emptyList());
            if (entries.isEmpty()) {
                throw new IllegalArgumentException("No plugin tree found: " + inboundKey);
            }
            inbound = entries.get(0).getNode();
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
            TransformEntry entry = new TransformEntry(outbound, handler);
            transformMap.computeIfAbsent(outboundKey, k -> new ArrayList<>()).add(0, entry);

            //设置追踪链
            TracerStages ts = new TracerStages(entry);
            if (trace == null) {
                trace = ts;
            }
            if (current != null) {
                ts.previous = current;
                current.next = ts;
            }
            current = ts;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class TransformEntry {

        private final Node node;

        private final Object handler;
    }

    @Getter
    @RequiredArgsConstructor
    public static class TracerStages implements TraceStage {

        private final TransformEntry entry;

        private TraceStage previous;

        private TraceStage next;

        @Override
        public Node getNode() {
            return entry.node;
        }

        @Override
        public Object getHandler() {
            return entry.handler;
        }

        @Override
        public TraceStage previous() {
            return previous;
        }

        @Override
        public TraceStage next() {
            return next;
        }
    }
}
