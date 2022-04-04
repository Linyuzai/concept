package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@DependOnResolvers(JarPathNamePluginResolver.class)
public class JarClassNamePluginResolver extends AbstractPluginResolver<List<String>, Map<String, String>> {

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
