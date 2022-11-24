package com.github.linyuzai.extension.core.invoker;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.factory.ExceptionResultFactory;

public interface ExtensionInvokerFactory {

    ExtensionInvoker create(Extension extension, Extension.Argument argument, ExceptionResultFactory resultFactory);
}
