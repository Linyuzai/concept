package com.github.linyuzai.extension.core.batch;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.concept.simple.SimpleArgument;

import java.util.Collection;
import java.util.stream.Collectors;

public class CollectionExtensionHandler extends BatchExtensionHandler {

    @Override
    public boolean isBatchArgument(Extension.Argument argument) {
        return argument.getTarget() instanceof Collection;
    }

    @Override
    public Collection<Extension.Argument> splitArgument(Extension.Argument argument) {
        Collection<?> targets = ((Collection<?>) argument.getTarget());
        return targets.stream()
                .map(it -> new SimpleArgument.Builder()
                        .target(it)
                        .key(argument.getKey())
                        .value(argument.getValue())
                        .configs(argument.getConfigs())
                        .build())
                .collect(Collectors.toList());
    }
}
