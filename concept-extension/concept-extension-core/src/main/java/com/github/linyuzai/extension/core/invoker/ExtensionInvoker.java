package com.github.linyuzai.extension.core.invoker;

import com.github.linyuzai.extension.core.concept.Extension;

import java.util.Collection;

public interface ExtensionInvoker {

    Collection<Extension.ArgumentAndResult> invoke();

    Extension getExtension();

    Extension.Argument getArgument();
}
