package com.github.linyuzai.extension.core.strategy;

import com.github.linyuzai.extension.core.concept.Extension;
import com.github.linyuzai.extension.core.invoker.ExtensionInvoker;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 并发的分发策略
 */
@Getter
@Setter
public abstract class ConcurrentExtensionStrategy extends SequentialExtensionStrategy {

    /**
     * 线程池
     */
    private ExecutorService executorService;

    /**
     * 并发过程中可以通过ContextPoster传递线程本地变量
     */
    private Collection<ContextPoster> contextPosters = new CopyOnWriteArrayList<>();

    /**
     * 当Request只有一个时是否通过顺序执行
     */
    private boolean sequentialOnSingleArgument = true;

    @Override
    public Collection<Extension.ArgumentAndResult> extend(Collection<? extends ExtensionInvoker> invokers) {
        if (sequentialOnSingleArgument && invokers.size() <= 1) {
            return dispatchSequential(invokers);
        }
        List<Extension.ArgumentAndResult> results = new ArrayList<>(invokers.size());
        //将Request分成两组
        Collection<ExtensionInvoker> concurrentInvokers = new ArrayList<>();
        Collection<ExtensionInvoker> sequentialInvokers = new ArrayList<>();
        for (ExtensionInvoker invoker : invokers) {
            //通过该方法判断分组
            if (ifConcurrentDispatch(invoker)) {
                concurrentInvokers.add(invoker);
            } else {
                sequentialInvokers.add(invoker);
            }
        }
        //并发分发
        if (!concurrentInvokers.isEmpty()) {
            results.addAll(dispatchConcurrently(concurrentInvokers));
        }
        //顺序分发
        if (!sequentialInvokers.isEmpty()) {
            results.addAll(dispatchSequential(sequentialInvokers));
        }
        return results;
    }

    /**
     * 销毁时需要关闭线程池释放资源
     */
    public void destroy() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }

    /**
     * 通过ContextPoster持有需要传递的变量
     *
     * @return ContextPostHolder列表
     */
    public Collection<ContextPostHolder> newContextPostHolders() {
        return getContextPosters().stream().map(ContextPostHolder::new).collect(Collectors.toList());
    }

    /**
     * 是否需要并发分发
     *
     * @param invoker
     * @return 是否需要并发分发
     */
    public boolean ifConcurrentDispatch(ExtensionInvoker invoker) {
        return true;
    }

    /**
     * 并发分发的抽象，需要同步返回
     *
     * @param invokers
     * @return 同步返回的执行结果
     */
    public abstract List<Extension.ArgumentAndResult> dispatchConcurrently(Collection<? extends ExtensionInvoker> invokers);

    /**
     * 并发环境本地变量传递接口
     */
    public interface ContextPoster {

        /**
         * @return 需要传递的变量
         */
        Object create();

        /**
         * 当环境切换时回调
         *
         * @param context 被传递的变量
         */
        void post(Object context);

        /**
         * 执行结束需要清理当前环境的变量缓存
         *
         * @param context 需要被清理的变量
         */
        void destroy(Object context);
    }

    /**
     * 被传递变量的持有者
     */
    public static class ContextPostHolder {

        private final ContextPoster contextPoster;

        /**
         * 被传递的变量
         */
        private Object context;

        public ContextPostHolder(ContextPoster contextPoster) {
            this.contextPoster = contextPoster;
        }

        /**
         * 获得需要传递的变量
         */
        public void create() {
            context = contextPoster.create();
        }

        /**
         * 当环境切换时传递变量
         */
        public void post() {
            contextPoster.post(context);
        }

        /**
         * 执行结束清理变量缓存
         */
        public void destroy() {
            contextPoster.destroy(context);
        }
    }
}
