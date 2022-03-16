package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginPath;
import com.github.linyuzai.plugin.core.autoload.WatchServicePluginAutoLoader;
import com.github.linyuzai.plugin.core.matcher.OnPluginMatched;
import com.github.linyuzai.plugin.jar.filter.ModifierFilter;
import com.github.linyuzai.plugin.jar.JarPluginConcept;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.matcher.*;
import com.github.linyuzai.plugin.core.matcher.PluginMatch;
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
            .addMatcher(new ClassMatcher<Class<CustomPluginImpl>[]>() {
                @Override
                public void onMatched(Class<CustomPluginImpl>[] plugin) {
                    System.out.println(Arrays.toString(plugin));
                }
            })
            //.match(this)//自动匹配回调添加了@OnPluginMatched注解的方法参数
            .build();

    private final PluginAutoLoader loader = new WatchServicePluginAutoLoader.Builder()
            .pluginConcept(concept)
            .paths(new PluginPath.Builder().path("/Users/tanghanzheng/concept/plugin/").build())
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
     *
     * @param properties 匹配到的配置文件
     */
    @OnPluginMatched
    private void onPluginMatched(
            //包下所有的class
            @PluginPackage("com.github.linyuzai.concept.sample.plugin") Collection<?> pluginsByPackage,
            //包下所有的CustomPlugin
            @PluginPackage("com.github.linyuzai.concept.sample.plugin") Collection<? extends CustomPlugin> pluginsByPackageAndClass,
            //所有的 com.github.linyuzai.concept.sample.plugin.CustomPlugin 类
            @PluginClassName("com.github.linyuzai.concept.sample.plugin.CustomPlugin") Collection<? extends CustomPlugin> pluginsByClassName,
            @PluginClass(CustomPluginImpl.class) Collection<?> pluginsByClass,
            @PluginMatch(path = "/resources/concept") Properties properties,
            @PluginProperties("concept.plugin") Map<String, String> map,
            @PluginMatch(name = "config.json") String json) {
        //在这里处理匹配到的插件和配置文件
        for (CustomPlugin plugin : pluginsByClassName) {
            System.out.println(plugin.getClass());
        }
        System.out.println(properties);
        System.out.println(json);
    }

    @GetMapping("/load")
    public void loadPlugin() {
        concept.load("/Users/tanghanzheng/concept/plugin/sample-0.0.1-SNAPSHOT-plain.jar");
    }
}
