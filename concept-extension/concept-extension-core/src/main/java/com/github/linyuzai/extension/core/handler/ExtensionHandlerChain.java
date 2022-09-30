package com.github.linyuzai.extension.core.handler;

import com.github.linyuzai.extension.core.concept.Extension;

import java.util.Collection;

public interface ExtensionHandlerChain {

    Collection<Extension.ArgumentAndResult> next(Extension extension, Extension.Argument argument);
}
