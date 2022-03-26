package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.autoload.PluginAutoLoader;
import com.github.linyuzai.plugin.core.autoload.PluginLocation;
import com.github.linyuzai.plugin.core.autoload.WatchServicePluginAutoLoader;
import com.github.linyuzai.plugin.core.extract.OnPluginExtract;
import com.github.linyuzai.plugin.core.match.PluginName;
import com.github.linyuzai.plugin.core.match.PluginPath;
import com.github.linyuzai.plugin.core.match.PluginProperties;
import com.github.linyuzai.plugin.jar.concept.JarPluginConcept;
import com.github.linyuzai.plugin.jar.extract.ClassExtractor;
import com.github.linyuzai.plugin.jar.extract.InstanceExtractor;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.match.PluginAnnotation;
import com.github.linyuzai.plugin.jar.match.PluginClass;
import com.github.linyuzai.plugin.jar.match.PluginClassName;
import com.github.linyuzai.plugin.jar.match.PluginPackage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/concept-plugin")
public class ConceptPluginController {

    private String append(String s) {
        StringBuilder builder = new StringBuilder(s);
        while (builder.length() < 70) {
            builder.append(" ");
        }
        return builder.toString();
    }

    private final JarPluginConcept concept = new JarPluginConcept.Builder()
            .addFilter(new PackageFilter("com.github.linyuzai.concept.sample.plugin"))
            //.addFilter(new ModifierFilter(Modifier::isInterface, Modifier::isAbstract).negate())
            //.addFilter(new AnnotationFilter(CustomPluginAnnotation.class))
            //Class
            /*.addExtractor(new ClassExtractor<Collection>() {
                @Override
                public void onExtract(Collection plugin) {
                    System.out.println(append("ClassExtractor<Collection>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List>() {
                @Override
                public void onExtract(List plugin) {
                    System.out.println(append("ClassExtractor<List>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set>() {
                @Override
                public void onExtract(Set plugin) {
                    System.out.println(append("ClassExtractor<Set>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map>() {
                @Override
                public void onExtract(Map plugin) {
                    System.out.println(append("ClassExtractor<Map>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<?>>() {
                @Override
                public void onExtract(Collection<?> plugin) {
                    System.out.println(append("ClassExtractor<Collection<?>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<?>>() {
                @Override
                public void onExtract(List<?> plugin) {
                    System.out.println(append("ClassExtractor<List<?>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<?>>() {
                @Override
                public void onExtract(Set<?> plugin) {
                    System.out.println(append("ClassExtractor<Set<?>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<?, ?>>() {
                @Override
                public void onExtract(Map<?, ?> plugin) {
                    System.out.println(append("ClassExtractor<Map<?, ?>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Object>>() {
                @Override
                public void onExtract(Collection<Object> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Object>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Object>>() {
                @Override
                public void onExtract(List<Object> plugin) {
                    System.out.println(append("ClassExtractor<List<Object>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Object>>() {
                @Override
                public void onExtract(Set<Object> plugin) {
                    System.out.println(append("ClassExtractor<Set<Object>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Object>>() {
                @Override
                public void onExtract(Map<Object, Object> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Object>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Object[]>() {
                @Override
                public void onExtract(Object[] plugin) {
                    System.out.println(append("ClassExtractor<Object[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<Type>>() {
                @Override
                public void onExtract(Collection<Type> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Type>>() {
                @Override
                public void onExtract(List<Type> plugin) {
                    System.out.println(append("ClassExtractor<List<Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Type>>() {
                @Override
                public void onExtract(Set<Type> plugin) {
                    System.out.println(append("ClassExtractor<Set<Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Type>>() {
                @Override
                public void onExtract(Map<Object, Type> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Type[]>() {
                @Override
                public void onExtract(Type[] plugin) {
                    System.out.println(append("ClassExtractor<Type[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Type>>() {
                @Override
                public void onExtract(Collection<? extends Type> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Type>>() {
                @Override
                public void onExtract(List<? extends Type> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Type>>() {
                @Override
                public void onExtract(Set<? extends Type> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Type>>() {
                @Override
                public void onExtract(Map<Object, ? extends Type> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Type>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class>>() {
                @Override
                public void onExtract(Collection<Class> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class>>() {
                @Override
                public void onExtract(List<Class> plugin) {
                    System.out.println(append("ClassExtractor<List<Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class>>() {
                @Override
                public void onExtract(Set<Class> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class>>() {
                @Override
                public void onExtract(Map<Object, Class> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class[]>() {
                @Override
                public void onExtract(Class[] plugin) {
                    System.out.println(append("ClassExtractor<Class[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<?>>>() {
                @Override
                public void onExtract(Collection<Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<?>>>() {
                @Override
                public void onExtract(List<Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<?>>>() {
                @Override
                public void onExtract(Set<Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<?>>>() {
                @Override
                public void onExtract(Map<Object, Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<?>[]>() {
                @Override
                public void onExtract(Class<?>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<?>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<?>>>() {
                @Override
                public void onExtract(Collection<? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<?>>>() {
                @Override
                public void onExtract(List<? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<?>>>() {
                @Override
                public void onExtract(Set<? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<?>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<?>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<?>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<Object>>>() {
                @Override
                public void onExtract(Collection<Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<Object>>>() {
                @Override
                public void onExtract(List<Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<Object>>>() {
                @Override
                public void onExtract(Set<Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<Object>>>() {
                @Override
                public void onExtract(Map<Object, Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<Object>[]>() {
                @Override
                public void onExtract(Class<Object>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<Object>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<Object>>>() {
                @Override
                public void onExtract(Collection<? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<Object>>>() {
                @Override
                public void onExtract(List<? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<Object>>>() {
                @Override
                public void onExtract(Set<? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<Object>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<Object>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<Object>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<CustomPlugin>>>() {
                @Override
                public void onExtract(List<Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Set<Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<CustomPlugin>[]>() {
                @Override
                public void onExtract(Class<CustomPlugin>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<CustomPlugin>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(List<? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Set<? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Collection<Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(List<Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Set<Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Class<? extends CustomPlugin>[]>() {
                @Override
                public void onExtract(Class<? extends CustomPlugin>[] plugin) {
                    System.out.println(append("ClassExtractor<Class<? extends CustomPlugin>[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new ClassExtractor<Collection<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Collection<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Collection<? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<List<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(List<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<List<? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Set<? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Set<? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Set<? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })
            .addExtractor(new ClassExtractor<Map<Object, ? extends Class<? extends CustomPlugin>>>() {
                @Override
                public void onExtract(Map<Object, ? extends Class<? extends CustomPlugin>> plugin) {
                    System.out.println(append("ClassExtractor<Map<Object, ? extends Class<? extends CustomPlugin>>>: ") + plugin);
                }
            })*/
            //Instance
            .addExtractor(new InstanceExtractor<Collection>() {
                @Override
                public void onExtract(Collection plugin) {
                    System.out.println(append("InstanceExtractor<Collection>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List>() {
                @Override
                public void onExtract(List plugin) {
                    System.out.println(append("InstanceExtractor<List>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set>() {
                @Override
                public void onExtract(Set plugin) {
                    System.out.println(append("InstanceExtractor<Set>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map>() {
                @Override
                public void onExtract(Map plugin) {
                    System.out.println(append("InstanceExtractor<Map>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Collection<?>>() {
                @Override
                public void onExtract(Collection<?> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<?>>() {
                @Override
                public void onExtract(List<?> plugin) {
                    System.out.println(append("InstanceExtractor<List<?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<?>>() {
                @Override
                public void onExtract(Set<?> plugin) {
                    System.out.println(append("InstanceExtractor<Set<?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<?, ?>>() {
                @Override
                public void onExtract(Map<?, ?> plugin) {
                    System.out.println(append("InstanceExtractor<Map<?, ?>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Collection<Object>>() {
                @Override
                public void onExtract(Collection<Object> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<Object>>() {
                @Override
                public void onExtract(List<Object> plugin) {
                    System.out.println(append("InstanceExtractor<List<Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<Object>>() {
                @Override
                public void onExtract(Set<Object> plugin) {
                    System.out.println(append("InstanceExtractor<Set<Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<Object, Object>>() {
                @Override
                public void onExtract(Map<Object, Object> plugin) {
                    System.out.println(append("InstanceExtractor<Map<Object, Object>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Object[]>() {
                @Override
                public void onExtract(Object[] plugin) {
                    System.out.println(append("InstanceExtractor<Object[]>: ") + Arrays.toString(plugin));
                }
            })

            .addExtractor(new InstanceExtractor<Collection<CustomPlugin>>() {
                @Override
                public void onExtract(Collection<CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<CustomPlugin>>() {
                @Override
                public void onExtract(List<CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<List<CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<CustomPlugin>>() {
                @Override
                public void onExtract(Set<CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Set<CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<Object, CustomPlugin>>() {
                @Override
                public void onExtract(Map<Object, CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Map<Object, CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<CustomPlugin[]>() {
                @Override
                public void onExtract(CustomPlugin[] plugin) {
                    System.out.println(append("InstanceExtractor<CustomPlugin[]>: ") + Arrays.toString(plugin));
                }
            })
            .addExtractor(new InstanceExtractor<Collection<? extends CustomPlugin>>() {
                @Override
                public void onExtract(Collection<? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Collection<? extends CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<List<? extends CustomPlugin>>() {
                @Override
                public void onExtract(List<? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<List<? extends CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Set<? extends CustomPlugin>>() {
                @Override
                public void onExtract(Set<? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Set<? extends CustomPlugin>>: ") + plugin);
                }
            })
            .addExtractor(new InstanceExtractor<Map<Object, ? extends CustomPlugin>>() {
                @Override
                public void onExtract(Map<Object, ? extends CustomPlugin> plugin) {
                    System.out.println(append("InstanceExtractor<Map<Object, ? extends CustomPlugin>>: ") + plugin);
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
            @PluginPath("/resources/concept") Properties p7,

            //名称为 config.json 的文件内容
            @PluginName("config.json") String p8) {
    }

    @GetMapping("/load")
    public void loadPlugin() {
        concept.load("/Users/tanghanzheng/concept/plugin/sample-0.0.1-SNAPSHOT-plain.jar");
    }
}
