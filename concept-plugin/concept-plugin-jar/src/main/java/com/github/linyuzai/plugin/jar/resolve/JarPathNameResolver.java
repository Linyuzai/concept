package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.AbstractPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.core.resolve.PathNameResolver;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;

import java.io.File;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

/**
 * 路径名称解析器
 */
@DependOnResolvers(JarEntryResolver.class)
public class JarPathNameResolver extends AbstractPluginResolver<List<JarEntry>, List<String>>
        implements PathNameResolver {

    /**
     * 获得 {@link JarEntry} 的名称并且处理分隔符
     *
     * @param entries {@link JarEntry}
     * @param context 上下文 {@link PluginContext}
     * @return 路径名称
     */
    @Override
    public List<String> doResolve(List<JarEntry> entries, PluginContext context) {
        return entries.stream()
                .map(ZipEntry::getName)
                .map(it -> it.replaceAll(File.separator, "/"))
                .filter(it -> !it.endsWith("/"))
                .collect(Collectors.toList());
    }

    @Override
    public Object getDependedKey() {
        return JarPlugin.ENTRY;
    }

    @Override
    public Object getResolvedKey() {
        return JarPlugin.PATH_NAME;
    }
}
