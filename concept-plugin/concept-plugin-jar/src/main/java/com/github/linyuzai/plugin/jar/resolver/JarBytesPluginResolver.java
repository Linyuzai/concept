package com.github.linyuzai.plugin.jar.resolver;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolver.BytesPluginResolver;
import com.github.linyuzai.plugin.core.resolver.dependence.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

@DependOnResolvers(JarFileNamePluginResolver.class)
public class JarBytesPluginResolver extends BytesPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> filenames = context.get(Plugin.FILE_NAMES);
        JarPlugin plugin = context.getPlugin();
        JarFile jarFile = plugin.getFile();
        Map<String, byte[]> bytesMap = filenames.stream()
                .filter(it -> !it.endsWith(".class"))
                .collect(Collectors.toMap(Function.identity(), it ->
                        getBytes(jarFile, it)));
        context.set(JarPlugin.BYTES, bytesMap);
    }

    @SneakyThrows
    public byte[] getBytes(ZipFile zipFile, String name) {
        return toBytes(zipFile.getInputStream(zipFile.getEntry(name)));
    }
}
