package com.github.linyuzai.plugin.core.resolver;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.extractor.PluginExtractor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
public class PluginResolverChainImpl implements PluginResolverChain {

    private Collection<PluginResolver> pluginResolvers;

    @Override
    public void next(PluginContext context) {
        List<PluginResolver> supported = new ArrayList<>();
        List<PluginResolver> unsupported = new ArrayList<>();
        for (PluginResolver resolver : pluginResolvers) {
            if (resolver.support(context)) {
                supported.add(resolver);
            } else {
                unsupported.add(resolver);
            }
        }
        PluginResolverChainImpl chain = new PluginResolverChainImpl(unsupported);
        for (PluginResolver resolver : supported) {
            resolver.resolve(context, chain);
        }
    }
}
