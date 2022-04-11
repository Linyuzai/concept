package com.github.linyuzai.plugin.jar.resolve;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.resolve.ByteArrayResolver;
import com.github.linyuzai.plugin.core.resolve.DependOnResolvers;
import com.github.linyuzai.plugin.jar.concept.JarPlugin;
import lombok.SneakyThrows;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

/**
 * jar 中非 class 文件的内容解析器
 */
@DependOnResolvers(JarPathNameResolver.class)
public class JarByteArrayResolver extends ByteArrayResolver<List<String>, Map<String, byte[]>> {

    /**
     * 非 class 和 properties 文件内容的字节数组
     *
     * @param filenames 文件名
     * @param context   上下文 {@link PluginContext}
     * @return 内容的字节数组
     */
    @Override
    public Map<String, byte[]> doResolve(List<String> filenames, PluginContext context) {
        JarPlugin plugin = context.getPlugin();
        JarFile jarFile = plugin.getFile();
        Map<String, byte[]> bytesMap = new LinkedHashMap<>();
        for (String filename : filenames) {
            if (filename.endsWith(".class") || filename.endsWith(".properties")) {
                continue;
            }
            bytesMap.put(filename, getBytes(jarFile, filename));
        }
        return bytesMap;
    }

    @SneakyThrows
    public byte[] getBytes(ZipFile zipFile, String name) {
        return toBytes(zipFile.getInputStream(zipFile.getEntry(name)));
    }
}
