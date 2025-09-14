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
public class NestedDepthPluginInterceptor implements PluginInterceptor {

    private int maxNestedDepth = -1;

    @Override
    public void beforeCreate(PluginDefinition definition, PluginContext context) {
        if (maxNestedDepth < 0) {
            return;
        }
        int depth = 0;
        PluginContext ctx = context.getParent();
        while (ctx != null) {
            depth++;
            if (depth > maxNestedDepth) {
                throw new PluginException("Max nested depth > " + maxNestedDepth + ": " + definition.getPath());
            }
            ctx = ctx.getParent();
        }
    }
}
