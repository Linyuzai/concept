package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名解析器
 */
@DependOnResolvers(JarPathNameResolver.class)
public class JarClassNameResolver extends AbstractPluginResolver<List<String>, Map<String, String>> {

    /**
     * 将所有 .class 结尾的文件过滤出来，
     * 将路径处理成类名
     *
     * @param filenames 文件名
     * @param context   上下文 {@link PluginContext}
     * @return 类名
     */
    @Override
    public Map<String, String> doResolve(List<String> filenames, PluginContext context) {
        Map<String, String> classNameMap = new LinkedHashMap<>();
        for (String filename : filenames) {
            if (filename.endsWith(".class")) {
                String className = filename.substring(0, filename.lastIndexOf("."))
                        .replaceAll("/", ".");
                classNameMap.put(filename, className);
            }
        }
        return classNameMap;
    }

    @Override
    public Object getDependedKey() {
        return JarPlugin.PATH_NAME;
    }

    @Override
    public Object getResolvedKey() {
        return JarPlugin.CLASS_NAME;
    }
}
