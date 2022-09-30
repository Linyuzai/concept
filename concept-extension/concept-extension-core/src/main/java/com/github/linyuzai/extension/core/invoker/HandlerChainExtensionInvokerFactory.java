package com.github.linyuzai.extension.core.invoker;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.handler.ExtensionHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class HandlerChainExtensionInvokerFactory implements ExtensionInvokerFactory {

    private final List<ExtensionHandler> handlers;

    @Override
    public ExtensionInvoker create(Extension extension, Extension.Argument argument) {
        return new HandlerChainExtensionInvoker(extension, argument, handlers);
    }
}
