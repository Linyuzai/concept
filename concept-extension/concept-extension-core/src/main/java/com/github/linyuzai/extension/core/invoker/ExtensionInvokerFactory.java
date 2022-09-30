package com.github.linyuzai.extension.core.invoker;

import com.github.linyuzai.extension.core.concept.Extension;

public interface ExtensionInvokerFactory {

    ExtensionInvoker create(Extension extension, Extension.Argument argument);
}
