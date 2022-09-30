package com.github.linyuzai.extension.core.strategy;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.invoker.ExtensionInvoker;

import java.util.Collection;

public interface ExtensionStrategy {

    Collection<Extension.ArgumentAndResult> extend(Collection<? extends ExtensionInvoker> invokers);
}
