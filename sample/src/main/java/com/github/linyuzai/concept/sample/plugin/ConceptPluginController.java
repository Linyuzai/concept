package com.github.linyuzai.concept.sample.plugin;

import com.github.linyuzai.plugin.core.matcher.OnPluginMatched;
import com.github.linyuzai.plugin.jar.JarPluginConcept;
import com.github.linyuzai.plugin.jar.matcher.ClassMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Properties;

@RestController
@RequestMapping("/concept-plugin")
public class ConceptPluginController {

    private final JarPluginConcept jarPluginConcept = new JarPluginConcept.Builder()
            .addMatchers(new ClassMatcher<CustomPlugin>() {
                @Override
                public void onMatched(Collection<Class<? extends CustomPlugin>> plugins) {

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
