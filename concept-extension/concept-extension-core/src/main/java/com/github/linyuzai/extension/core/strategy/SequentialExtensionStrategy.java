package com.github.linyuzai.extension.core.strategy;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.invoker.ExtensionInvoker;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 顺序执行
 */
public class SequentialExtensionStrategy implements ExtensionStrategy {

    /**
     * 按顺序执行
     *
     * @param invokers
     * @return 执行结果
     */
    @Override
    public Collection<Extension.ArgumentAndResult> extend(Collection<? extends ExtensionInvoker> invokers) {
        return dispatchSequential(invokers);
    }

    public List<Extension.ArgumentAndResult> dispatchSequential(Collection<? extends ExtensionInvoker> invokers) {
        return invokers.stream().flatMap(it -> it.invoke().stream()).collect(Collectors.toList());
    }
}
