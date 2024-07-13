package com.github.linyuzai.plugin.core.tree;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 插件树
 */
public interface PluginTree {

    /**
     * 获得根节点
     */
    Node getRoot();

    /**
     * 获得转换器
     */
    Transformer getTransformer();

    /**
     * 获得追踪器
     */
    Tracer getTracer();

    /**
     * 插件树节点
     */
    interface Node {

        /**
         * 获得节点id
         */
        Object getId();

        /**
         * 获得节点名称
         */
        String getName();

        /**
         * 获得节点值
         */
        Object getValue();

        /**
         * 获得插件树
         */
        PluginTree getTree();

        /**
         * 获得父节点
         */
        Node getParent();

        /**
         * 获得子节点
         */
        Collection<Node> getChildren();

        /**
         * 获得节点数量
         */
        int getSize();

        /**
         * 获得节点数量（可筛选）
         */
        int getSize(Predicate<Node> predicate);

        /**
         * 节点值转换
         */
        Node map(Function<Node, Object> function);

        /**
         * 节点值转换（可筛选）
         */
        Node map(Function<Node, Object> function, Predicate<Node> predicate);

        /**
         * 节点过滤
         */
        Node filter(Predicate<Node> predicate);

        /**
         * 节点遍历
         */
        void forEach(Consumer<Node> consumer);

        /**
         * 是否是插件节点
         */
        boolean isPluginNode();
    }

    /**
     * 节点工厂
     */
    interface NodeFactory {

        Node create(Object id, String name, Object value);
    }

    /**
     * 转换器
     */
    interface Transformer {

        /**
         * 创建转换
         */
        InboundStage create(Object handler);

        /**
         * 入参配置
         */
        interface InboundStage {

            /**
             * 设置入参
             */
            TransformStage inbound(Node node);

            /**
             * 引用入参
             */
            TransformStage inboundKey(Object inboundKey);
        }

        /**
         * 转换配置
         */
        interface TransformStage {

            /**
             * 转换
             */
            OutboundStage transform(Function<Node, Node> transform);
        }

        /**
         * 出参配置
         */
        interface OutboundStage {

            /**
             * 获得出参
             */
            Node outbound();

            /**
             * 关联出参
             */
            void outboundKey(Object outboundKey);
        }
    }

    /**
     * 追踪器
     */
    interface Tracer {

        /**
         * 获得追踪链
         */
        TraceStage getTrace();

        /**
         * 追踪链节点
         */
        interface TraceStage {

            /**
             * 获得插件树节点
             */
            PluginTree.Node getNode();

            /**
             * 获得处理器
             */
            Object getHandler();

            /**
             * 获得上一个节点
             */
            TraceStage previous();

            /**
             * 获得下一个节点
             */
            TraceStage next();
        }
    }
}
