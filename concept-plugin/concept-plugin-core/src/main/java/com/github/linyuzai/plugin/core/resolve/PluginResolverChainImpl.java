package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Iterator;

/**
 * 插件解析链 {@link PluginResolverChain} 的实现
 */
@Getter
@AllArgsConstructor
public class PluginResolverChainImpl implements PluginResolverChain {

    /**
     * 解析器
     */
    private final Collection<PluginResolver> resolvers;

    /**
     * 过滤器
     */
    private final Collection<PluginFilter> filters;

    /**
     * 执行解析。
     * 对于当前的上下文 {@link PluginContext}，
     * 遍历所有的解析器 {@link PluginResolver}，
     * 如果支持解析则直接进行解析，同时获得对应的过滤器 {@link PluginFilter} 进行过滤，
     * 然后将未使用的解析器和剩余的过滤器传入下一个解析链节点。
     * <p>
     * 获得所有支持解析的解析器 {@link PluginResolver}，
     *
     * @param context 上下文 {@link PluginContext}
     */
    @Override
    public void next(PluginContext context) {
        int size = resolvers.size();
        Iterator<PluginResolver> resolverIterator = resolvers.iterator();
        while (resolverIterator.hasNext()) {
            PluginResolver resolver = resolverIterator.next();
            if (resolver.support(context)) {
                resolverIterator.remove();
                resolver.resolve(context);
                //执行对应解析器的过滤器
                Iterator<PluginFilter> filterIterator = filters.iterator();
                while (filterIterator.hasNext()) {
                    PluginFilter filter = filterIterator.next();
                    if (filter.support(context)) {
                        Collection<Class<? extends PluginResolver>> dependencies = filter.getDependencies();
                        for (Class<? extends PluginResolver> dependency : dependencies) {
                            if (dependency.isInstance(resolver)) {
                                filterIterator.remove();
                                filter.filter(context);
                            }
                        }
                    }
                }
            }
        }
        //如果所有解析器都解析完成，直接返回
        //如果没有支持解析的解析器，直接返回，防止无限循环
        if (resolvers.isEmpty() || resolvers.size() == size) {
            return;
        }
        next(context);
    }
}
