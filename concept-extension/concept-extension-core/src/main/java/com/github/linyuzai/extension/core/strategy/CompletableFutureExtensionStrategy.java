package com.github.linyuzai.extension.core.strategy;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.invoker.ExtensionInvoker;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 使用 {@link CompletableFuture} 实现的同步等待响应的并发策略
 */
public class CompletableFutureExtensionStrategy extends ConcurrentExtensionStrategy {

    @SneakyThrows
    @Override
    public List<Extension.ArgumentAndResult> dispatchConcurrently(Collection<? extends ExtensionInvoker> invokers) {
        List<Extension.ArgumentAndResult> allResults = new ArrayList<>();
        Collection<ContextPostHolder> contextPostHolders = newContextPostHolders();
        contextPostHolders.forEach(ContextPostHolder::create);
        CompletableFuture<?>[] futures = invokers.stream()
                .map(it -> CompletableFuture.supplyAsync(() -> {
                    contextPostHolders.forEach(ContextPostHolder::post);
                    Collection<Extension.ArgumentAndResult> results = it.invoke();
                    contextPostHolders.forEach(ContextPostHolder::destroy);
                    return results;
                }, getExecutorService()).whenComplete((argumentAndResults, throwable) -> {
                    if (throwable == null) {
                        allResults.addAll(argumentAndResults);
                    } else {
                        onException(throwable);
                    }
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<Void> all = CompletableFuture.allOf(futures);
        all.join();

        return allResults;
    }

    public void onException(Throwable e) {
        throw new RuntimeException(e);
    }
}
