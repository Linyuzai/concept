package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.matcher.OnPluginMatched;
import com.github.linyuzai.plugin.jar.JarPluginConcept;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.matcher.*;
import com.github.linyuzai.plugin.core.matcher.PluginMatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("/concept-plugin")
public class ConceptPluginController {

    private final JarPluginConcept concept = new JarPluginConcept.Builder()
            .addFilter(new PackageFilter("com.github.linyuzai.concept.sample.plugin"))
            //.addFilter(new AnnotationFilter())
            .addMatcher(new PropertiesMatcher<List<Properties>>() {
                @Override
                public void onMatched(List<Properties> plugin) {

                }
            })
            .match(this)//自动匹配回调添加了@OnPluginMatched注解的方法参数
            .autoLoad()//自动监听目录
            .build();

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
        concept.load("");
    }
}
