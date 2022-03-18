package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.BytesPluginResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.JarPlugin;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

@DependOnResolvers(JarFilePathNamePluginResolver.class)
public class JarBytesPluginResolver extends BytesPluginResolver {

    @Override
    public void resolve(PluginContext context) {
        List<String> filenames = context.get(Plugin.FILE_NAMES);
        JarPlugin plugin = context.getPlugin();
        JarFile jarFile = plugin.getFile();
        Map<String, byte[]> bytesMap = new LinkedHashMap<>();
        for (String filename : filenames) {
            if (filename.endsWith(".class")) {
                continue;
            }
            bytesMap.put(filename, getBytes(jarFile, filename));
        }
        context.set(JarPlugin.BYTES, bytesMap);
    }

    @SneakyThrows
    public byte[] getBytes(ZipFile zipFile, String name) {
        return toBytes(zipFile.getInputStream(zipFile.getEntry(name)));
    }
}
