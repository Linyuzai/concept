package com.github.linyuzai.extension.core.handler;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.concept.simple.SimpleArgumentAndResult;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ExtensionHandlerChainImpl implements ExtensionHandlerChain {

    private int index = 0;

    private final List<ExtensionHandler> handlers;

    @Override
    public Collection<Extension.ArgumentAndResult> next(Extension extension, Extension.Argument argument) {
        if (index < handlers.size()) {
            ExtensionHandler handler = handlers.get(index++);
            return handler.onExtend(extension, argument, this);
        } else {
            Extension.Result result = extension.extend(argument);
            return Collections.singletonList(new SimpleArgumentAndResult(argument, result));
        }
    }
}
