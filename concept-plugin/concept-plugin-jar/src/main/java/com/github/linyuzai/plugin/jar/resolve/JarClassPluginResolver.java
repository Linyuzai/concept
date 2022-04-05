package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类解析器
 */
@DependOnResolvers(JarClassNamePluginResolver.class)
public class JarClassPluginResolver extends AbstractPluginResolver<Map<String, String>, Map<String, Class<?>>> {

    /**
     * 对于所有的类名使用类加载器进行加载
     *
     * @param classNameMap 类名
     * @param context      上下文 {@link PluginContext}
     * @return 类
     */
    @Override
    public Map<String, Class<?>> doResolve(Map<String, String> classNameMap, PluginContext context) {
        JarPlugin plugin = context.getPlugin();
        ClassLoader classLoader = plugin.getPluginClassLoader();
        Map<String, Class<?>> classMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : classNameMap.entrySet()) {
            classMap.put(entry.getKey(), loadClass(classLoader, entry.getValue()));
        }
        return classMap;
    }

    @SneakyThrows
    private Class<?> loadClass(ClassLoader classLoader, String className) {
        return classLoader.loadClass(className);
    }

    @Override
    public Object getDependedKey() {
        return JarPlugin.CLASS_NAME;
    }

    @Override
    public Object getResolvedKey() {
        return JarPlugin.CLASS;
    }
}
