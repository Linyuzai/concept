package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.filter.PluginFilter;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class PluginResolverChainImpl implements PluginResolverChain {

    private final Collection<PluginResolver> resolvers;

    private final Collection<PluginFilter> filters;

    @Override
    public void next(PluginContext context) {
        List<PluginResolver> supportedResolvers = new ArrayList<>();
        List<PluginResolver> unsupportedResolvers = new ArrayList<>();
        for (PluginResolver resolver : resolvers) {
            if (resolver.support(context)) {
                supportedResolvers.add(resolver);
            } else {
                unsupportedResolvers.add(resolver);
            }
        }
        if (supportedResolvers.isEmpty()) {
            return;
        }

        List<PluginFilter> unsupportedFilters = new ArrayList<>();

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

        new PluginResolverChainImpl(unsupportedResolvers, unsupportedFilters).next(context);
    }
}
