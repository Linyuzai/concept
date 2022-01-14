package com.github.linyuzai.concept.sample.download;

import com.github.linyuzai.download.core.configuration.DownloadConfiguration;
import com.github.linyuzai.download.core.configuration.DownloadConfigurer;
import com.github.linyuzai.download.core.load.BlockSchedulerSourceLoader;
import com.github.linyuzai.download.core.load.SourceLoadException;
import com.github.linyuzai.download.core.load.SourceLoader;
import com.github.linyuzai.download.core.scheduler.DownloadScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.concurrent.Executors;

@Configuration
public class ConceptDownloadConfig implements DownloadConfigurer {

    @Override
    public void configure(DownloadConfiguration configuration) {
        System.out.println("可以在这里覆盖配置文件的配置！");
    }

    //@Bean
    /*public CoroutinesSourceLoaderInvoker coroutinesSourceLoaderInvoker() {
        System.out.println("如果需要进行HTTP请求可以使用协程加载！");
        return new CoroutinesSourceLoaderInvoker();
    }*/

    //@Bean(destroyMethod = "shutdown")
    /*public ExecutorSourceLoaderInvoker executorSourceLoaderInvoker() {
        System.out.println("如果需要进行HTTP请求可以使用线程池加载！");
        return new ExecutorSourceLoaderInvoker(Executors.newFixedThreadPool(5));
    }*/

    /*@Bean
    public DownloadScheduler downloadScheduler() {
        return new DownloadScheduler() {
            @Override
            public Scheduler getScheduler() {
                return Schedulers.fromExecutor(Executors.newFixedThreadPool(5));
            }
        };
    }*/

    /*@Bean
    public SourceLoader sourceLoader() {
        return new BlockSchedulerSourceLoader(Schedulers.fromExecutor(Executors.newFixedThreadPool(5)));
    }*/

    //@Bean
    /*public SourceLoadExceptionHandler sourceLoadExceptionHandler() {
        System.out.println("可以自定义加载时的异常处理器选择终止程序或只是打印！");
        return new SourceLoadExceptionHandler() {

            @Override
            public void onLoading(SourceLoadException e) {
                e.printStackTrace();
            }

            @Override
            public void onLoaded(Collection<SourceLoadException> exceptions) {

            }
        };
    }*/
}
