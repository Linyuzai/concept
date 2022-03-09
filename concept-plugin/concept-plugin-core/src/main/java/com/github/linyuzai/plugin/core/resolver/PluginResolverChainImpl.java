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

    private Collection<PluginExtractor> pluginExtractors;

    @Override
    public void next(PluginContext context) {
        for (PluginExtractor extractor : pluginExtractors) {
            if (extractor.support(context)) {
                extractor.extract(context);
            }
        }
        List<PluginResolver> supported = new ArrayList<>();
        List<PluginResolver> unsupported = new ArrayList<>();
        for (PluginResolver resolver : pluginResolvers) {
            if (resolver.support(context)) {
                supported.add(resolver);
            } else {
                unsupported.add(resolver);
            }
        }
        for (PluginResolver resolver : supported) {
            PluginResolverChainImpl chain = new PluginResolverChainImpl(unsupported, pluginExtractors);
            resolver.resolve(context, chain);
        }
    }
}
