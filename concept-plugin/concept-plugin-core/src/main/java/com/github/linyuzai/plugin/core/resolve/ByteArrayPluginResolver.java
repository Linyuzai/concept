package com.github.linyuzai.plugin.core.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 字节数组插件解析器。
 * 用于提取插件内容位为字节数组，
 * 最底层的存储结构，方便后续类型转换。
 *
 * @param <T> 未解析的插件类型
 * @param <R> 解析后的插件类型
 */
@AllArgsConstructor
@DependOnResolvers(PathNamePluginResolver.class)
public abstract class ByteArrayPluginResolver<T, R> extends AbstractPluginResolver<T, R> {

    /**
     * 缓存大小
     */
    private final int bufferSize;

    public ByteArrayPluginResolver() {
        this(-1);
    }

    @Override
    public Object getKey() {
        return Plugin.PATH_NAME;
    }

    @Override
    public Object getResolveKey() {
        return Plugin.BYTE_ARRAY;
    }

    /**
     * 将 {@link InputStream} 转为字节数组
     *
     * @param stream 输入流 {@link InputStream}
     * @return 字节数组
     */
    public byte[] toBytes(InputStream stream) {
        return toBytes(stream, bufferSize);
    }

    /**
     * 将 {@link InputStream} 转为字节数组。
     * 考虑到插件文件一般不会很大，所以偏向直接读取全部，也可以避免编码问题。
     *
     * @param stream     输入流 {@link InputStream}
     * @param bufferSize 缓存大小
     * @return 字节数组
     */
    @SneakyThrows
    public static byte[] toBytes(InputStream stream, int bufferSize) {
        try (InputStream is = stream;
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            if (bufferSize > 0) {
                byte[] buffer = new byte[bufferSize];
                int l;
                while ((l = is.read(buffer)) > 0) {
                    os.write(buffer, 0, l);
                }
            } else {
                int available = stream.available();
                if (available > 0) {
                    byte[] buffer = new byte[available];
                    int l = is.read(buffer);
                    os.write(buffer, 0, l);
                }
            }
            return os.toByteArray();
        }
    }
}
