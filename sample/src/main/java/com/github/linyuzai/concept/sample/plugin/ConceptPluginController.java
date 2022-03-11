package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.matcher.OnPluginMatched;
import com.github.linyuzai.plugin.jar.JarPluginConcept;
import com.github.linyuzai.plugin.jar.filter.AnnotationFilter;
import com.github.linyuzai.plugin.jar.filter.PackageFilter;
import com.github.linyuzai.plugin.jar.matcher.ClassListMatcher;
import com.github.linyuzai.plugin.jar.matcher.InstanceMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/concept-plugin")
public class ConceptPluginController {

    private final JarPluginConcept concept = new JarPluginConcept.Builder()
            .addFilter(new PackageFilter("com.github.linyuzai.concept.sample.plugin"))
            .addFilter(new AnnotationFilter(Override.class))
            .addMatcher(new ClassListMatcher<CustomPlugin>() {
                @Override
                public void onMatched(List<Class<? extends CustomPlugin>> plugins) {

                }
            })
            .addMatcher(new InstanceMatcher<CustomPlugin>() {
                @Override
                public void onMatched(CustomPlugin plugin) {

                }
            })
            .match(this)//自动匹配回调方法参数
            .autoLoad()//自动监听目录
            .build();

    /**
     * 插件匹配回调
     *
     * @param plugins 匹配到的插件
     * @param properties 匹配到的配置文件
     */
    @OnPluginMatched
    private void onPluginMatched(Collection<? extends CustomPlugin> plugins, Properties properties) {

    }
}
