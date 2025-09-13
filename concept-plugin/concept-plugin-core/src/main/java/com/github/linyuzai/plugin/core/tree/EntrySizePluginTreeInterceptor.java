package com.github.linyuzai.plugin.core.tree;

import com.github.linyuzai.plugin.core.concept.PluginDefinition;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.exception.PluginException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EntrySizePluginTreeInterceptor implements PluginTreeInterceptor {

    private long maxEntrySize = -1;

    @Override
    public void intercept(PluginDefinition definition, PluginContext context) {
        if (maxEntrySize == -1) {
            return;
        }
        if (definition.getSize() > maxEntrySize) {
            throw new PluginException("Max entry size > " + maxEntrySize + ": " + definition.getPath());
        }
    }
}
