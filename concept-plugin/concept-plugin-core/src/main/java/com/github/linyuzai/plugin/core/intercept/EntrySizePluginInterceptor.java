package com.github.linyuzai.plugin.core.intercept;

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
public class EntrySizePluginInterceptor implements PluginInterceptor {

    private long maxEntrySize = -1;

    @Override
    public void beforeCreate(PluginDefinition definition, PluginContext context) {
        if (maxEntrySize == -1) {
            return;
        }
        if (definition.getSize() > maxEntrySize) {
            throw new PluginException("Max entry size > " + maxEntrySize + ": " + definition.getPath());
        }
    }
}
