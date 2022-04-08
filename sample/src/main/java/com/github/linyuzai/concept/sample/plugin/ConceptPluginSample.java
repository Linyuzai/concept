package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginLocation;
import com.github.linyuzai.plugin.core.autoload.WatchServicePluginAutoLoader;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import com.github.linyuzai.plugin.jar.autoload.JarNotifier;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import com.github.linyuzai.plugin.jar.extract.ClassExtractor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

@Slf4j
public class ConceptPluginSample {

    /**
     * 插件提取配置
     */
    private final JarPluginConcept concept = new JarPluginConcept.Builder()
            //添加类提取器
            .extractTo(this)
            .build();

    @OnPluginExtract
    public void onPluginExtract(Class<? extends CustomPlugin> pluginClass, Properties properties) {
        //任意一个参数匹配上都会触发回调
    }

    /**
     * 加载一个 jar 插件
     *
     * @param filePath jar 文件路径
     */
    public void load(String filePath) {
        concept.load(filePath);
    }

    private final PluginAutoLoader loader = new WatchServicePluginAutoLoader.Builder()
            .locations(new PluginLocation.Builder()
                    .path("/Users/tanghanzheng/concept/plugin/")
                    .filter(it -> it.endsWith(".jar"))
                    .build())
            .executor(Executors.newSingleThreadExecutor())
            //.onCreate(concept::load)
            .onNotify(new JarNotifier(concept))
            .onError(e -> log.error("Plugin auto load error", e))
            .build();

    @PostConstruct
    private void start() {
        loader.start();
    }

    @PreDestroy
    private void stop() {
        loader.stop();
    }
}
