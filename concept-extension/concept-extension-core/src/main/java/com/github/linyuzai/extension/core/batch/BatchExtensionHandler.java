package com.github.linyuzai.extension.core.batch;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.handler.ExtensionHandler;
import com.github.linyuzai.extension.core.handler.ExtensionHandlerChain;

import java.util.Collection;
import java.util.Collections;

public abstract class BatchExtensionHandler implements ExtensionHandler {

    @Override
    public Collection<Extension.ArgumentAndResult> onExtend(Extension extension, Extension.Argument argument, ExtensionHandlerChain chain) {
        if (isBatchArgument(argument) &&
                extension instanceof BatchExtension &&
                ((BatchExtension) extension).batchSupport(argument)) {
            Collection<Extension.Argument> arguments = splitArgument(argument);
            if (arguments.isEmpty()) {
                return Collections.emptyList();
            } else {
                return nestingExtend(arguments, extension, chain);
            }
        } else {
            return chain.next(extension, argument);
        }
    }

    public abstract boolean isBatchArgument(Extension.Argument argument);

    public abstract Collection<Extension.Argument> splitArgument(Extension.Argument argument);
}
