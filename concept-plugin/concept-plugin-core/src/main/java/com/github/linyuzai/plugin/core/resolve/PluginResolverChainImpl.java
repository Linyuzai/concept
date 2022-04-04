package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 插件解析链 {@link PluginResolverChain} 的实现
 */
@Getter
@AllArgsConstructor
public class PluginResolverChainImpl implements PluginResolverChain {

    /**
     * 所有的解析器
     */
    private final Collection<PluginResolver> resolvers;

    /**
     * 所有的过滤器
     */
    private final Collection<PluginFilter> filters;

    /**
     * 执行解析。
     * 对于当前的上下文 {@link PluginContext}，
     * 遍历所有的解析器 {@link PluginResolver}，
     * 如果支持解析则直接进行解析，同时获得对应的过滤器 {@link PluginFilter} 进行过滤，
     * 然后将不支持的解析器和剩余的过滤器传入下一个解析链节点。
     * <p>
     * 获得所有支持解析的解析器 {@link PluginResolver}，
     *
     * @param context 上下文 {@link PluginContext}
     */
    @Override
    public void next(PluginContext context) {
        List<PluginResolver> supportedResolvers = new ArrayList<>();
        List<PluginResolver> unsupportedResolvers = new ArrayList<>();

        //将支持解析和不支持解析的解析器分成两组
        for (PluginResolver resolver : resolvers) {
            if (resolver.support(context)) {
                supportedResolvers.add(resolver);
            } else {
                unsupportedResolvers.add(resolver);
            }
        }
        //如果没有支持解析的解析器，直接返回，防止无限循环
        if (supportedResolvers.isEmpty()) {
            return;
        }

        List<PluginFilter> unsupportedFilters = new ArrayList<>();

        //遍历所有支持解析的解析器进行解析
        //执行对应解析器的过滤器
        for (PluginResolver resolver : supportedResolvers) {
            resolver.resolve(context);
            for (PluginFilter filter : filters) {
                Class<? extends PluginResolver> filterWith = filter.filterWith();
                if (filterWith.isInstance(resolver)) {
                    filter.filter(context);
                } else {
                    unsupportedFilters.add(filter);
                }
            }
        }

        //将未使用到的解析器和过滤器交给下一步解析
        new PluginResolverChainImpl(unsupportedResolvers, unsupportedFilters).next(context);
    }
}
