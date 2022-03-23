package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginLocation;
import com.github.linyuzai.plugin.core.autoload.WatchServicePluginAutoLoader;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import com.github.linyuzai.plugin.core.match.PluginName;
import com.github.linyuzai.plugin.core.match.PluginProperties;
import com.github.linyuzai.plugin.jar.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.filter.ModifierFilter;
import com.github.linyuzai.plugin.jar.JarPluginConcept;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.match.*;
import com.github.linyuzai.plugin.core.match.PluginPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/concept-plugin")
public class ConceptPluginController {

    private final JarPluginConcept concept = new JarPluginConcept.Builder()
            .addFilter(new PackageFilter("com.github.linyuzai.concept.sample.plugin"))
            .addFilter(new ModifierFilter(Modifier::isInterface, Modifier::isAbstract).negate())
            //.addFilter(new AnnotationFilter())
            /*.addMatcher(new ClassMatcher<LinkedHashSet<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onMatched(LinkedHashSet<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(plugin.getClass());
                    System.out.println(plugin);
                }
            })*/
            .addExtractor(new ClassExtractor<Class<CustomPluginImpl>[]>() {
                @Override
                public void onExtract(Class<CustomPluginImpl>[] plugin) {
                    System.out.println(Arrays.toString(plugin));
                }
            })
            //.extractTo(this)//自动匹配回调添加了@OnPluginExtract注解的方法参数
            .build();

    private final PluginAutoLoader loader = new WatchServicePluginAutoLoader.Builder()
            .pluginConcept(concept)
            .locations(new PluginLocation.Builder().path("/Users/tanghanzheng/concept/plugin/").build())
            .executorService(Executors.newSingleThreadExecutor())
            .errorConsumer(e -> log.error("Plugin auto load error", e))
            .build();

    @PostConstruct
    private void start() {
        loader.start();
    }

    @PreDestroy
    private void stop() {
        loader.stop();
    }

    /**
     * 插件匹配回调
     */
    @OnPluginExtract
    private void onPluginExtract(

            //所有的 CustomPlugin 类和 properties 文件
            List<Class<? extends CustomPlugin>> p0, Properties p1,

            //包下所有的 CustomPlugin 实例
            @PluginPackage("com.github.linyuzai.concept.sample.plugin") Collection<? extends CustomPlugin> p2,

            //所有的 com.github.linyuzai.concept.sample.plugin.CustomPlugin 类
            @PluginClassName("com.github.linyuzai.concept.sample.plugin.CustomPlugin") Set<Class<?>> p3,

            //一个 CustomPluginImpl 对象
            @PluginClass(CustomPluginImpl.class) CustomPlugin p4,

            //所有标注了 CustomPluginAnnotation 注解的类
            @PluginAnnotation(CustomPluginAnnotation.class) Class<?>[] p5,

            //properties 文件中 concept.plugin 对应的配置
            @PluginProperties("concept.plugin") Map<String, String> p6,

            //一个指定目录下的 properties 文件
            @PluginPath(path = "/resources/concept") Properties p7,

            //名称为 config.json 的文件内容
            @PluginName(name = "config.json") String p8) {
    }

    @GetMapping("/load")
    public void loadPlugin() {
        concept.load("/Users/tanghanzheng/concept/plugin/sample-0.0.1-SNAPSHOT-plain.jar");
    }
}
