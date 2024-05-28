package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.handle.PluginHandler;
import com.github.linyuzai.plugin.core.tree.trace.PluginTracer;
import com.github.linyuzai.plugin.core.tree.transform.PluginTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class DefaultPluginTree implements PluginTree, PluginTransformer, PluginTracer {

    private final Node root;

    private final Map<Object, HandleStage> traceMap = new LinkedHashMap<>();

    public DefaultPluginTree(Plugin plugin) {
        root = createNode(plugin.getId().toString(), plugin, plugin, null);
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
    public PluginTransformer.ParameterStage create(PluginHandler handler) {
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

    protected DefaultNode createNode(Object id, Object value, Plugin plugin, Node parent) {
        return new DefaultNode(id, value, plugin, parent);
    }

    @SuppressWarnings("unchecked")
    @Getter
    @RequiredArgsConstructor
    public class DefaultNode implements Node {

        private final Object id;

        private final Object value;

        private final Plugin plugin;

        private final Node parent;

        private final Collection<Node> children = new ArrayList<>();

        @Override
        public <T, R> Node map(Function<T, R> function) {
            return doMap(null, function);
        }

        protected <T, R> Node doMap(Node parent, Function<T, R> function) {
            Object apply;
            if (needHandle()) {
                apply = function.apply((T) value);
            } else {
                apply = value;
            }
            DefaultNode node = createNode(id, apply, plugin, parent);
            List<Node> collect = children.stream()
                    .map(it -> doMap(node, function))
                    .collect(Collectors.toList());
            node.children.addAll(collect);
            return node;
        }

        @Override
        public <T> Node filter(Predicate<T> predicate) {
            return doFilter(null, predicate);
        }

        public <T> Node doFilter(Node parent, Predicate<T> predicate) {
            if (needHandle() && !predicate.test((T) value)) {
                return null;
            }
            DefaultNode node = createNode(id, value, plugin, parent);
            List<Node> collect = children.stream()
                    .map(it -> doFilter(parent, predicate))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            node.children.addAll(collect);
            return node;
        }

        @Override
        public DefaultNode create(Object id, Object value, Plugin plugin) {
            DefaultNode node = createNode(id, value, plugin, this);
            children.add(node);
            return node;
        }

        protected boolean needHandle() {
            return !(value instanceof Plugin);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public class TransformerStages implements
            PluginTransformer.ParameterStage,
            PluginTransformer.TransformStage,
            PluginTransformer.ResultStage {

        private final PluginHandler handler;

        private Node parameter;

        private Node result;

        @Override
        public PluginTransformer.TransformStage parameter(Node tree) {
            parameter = tree;
            return this;
        }

        @Override
        public PluginTransformer.TransformStage parameterId(Object parameterId) {
            HandleStage trace = getTracer().getTrace(parameterId);
            if (trace == null) {
                throw new IllegalArgumentException("No plugin tree found: " + parameterId);
            }
            parameter = trace.getTree().getRoot();
            return this;
        }

        @Override
        public PluginTransformer.ResultStage transform(Function<Node, Node> transform) {
            result = transform.apply(parameter);
            return this;
        }

        @Override
        public Node result() {
            return result;
        }

        @Override
        public void resultId(Object resultId) {
            HandleStage trace = getTracer().getTrace(resultId);

        }
    }
}
