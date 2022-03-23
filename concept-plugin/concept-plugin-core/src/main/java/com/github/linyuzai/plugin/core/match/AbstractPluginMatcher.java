package com.github.linyuzai.plugin.core.match;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.convert.PluginConvertor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public abstract class AbstractPluginMatcher<T, R> implements PluginMatcher {

    @NonNull
    private PluginConvertor convertor;

    @Override
    public Object match(PluginContext context) {
        T source = context.get(getKey());
        R filter = filter(source);
        if (isEmpty(filter)) {
            return null;
        }
        return convertor.convert(filter);
    }

    public abstract Object getKey();

    public abstract R filter(T source);

    public abstract boolean isEmpty(R filter);
}
