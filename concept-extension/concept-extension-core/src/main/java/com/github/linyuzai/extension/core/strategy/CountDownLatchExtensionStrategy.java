package com.github.linyuzai.extension.core.strategy;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.invoker.ExtensionInvoker;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 使用 {@link CountDownLatch} 实现的同步等待响应的并发策略
 */
public class CountDownLatchExtensionStrategy extends ConcurrentExtensionStrategy {

    @SneakyThrows
    @Override
    public List<Extension.ArgumentAndResult> dispatchConcurrently(Collection<? extends ExtensionInvoker> invokers) {
        CountDownLatch latch = new CountDownLatch(invokers.size());
        List<Extension.ArgumentAndResult> results = Collections.synchronizedList(new ArrayList<>(invokers.size()));
        Collection<ContextPostHolder> contextPostHolders = newContextPostHolders();
        contextPostHolders.forEach(ContextPostHolder::create);
        for (ExtensionInvoker invoker : invokers) {
            execute(() -> {
                contextPostHolders.forEach(ContextPostHolder::post);
                results.addAll(invoker.invoke());
                contextPostHolders.forEach(ContextPostHolder::destroy);
                latch.countDown();
            });
        }
        latch.await();
        return results;
    }

    public void execute(Runnable runnable) {
        ExecutorService executorService = getExecutorService();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.execute(runnable);
        } else {
            new Thread(runnable).start();
        }
    }
}
