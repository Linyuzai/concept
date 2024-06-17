package com.github.linyuzai.plugin.core.handle.resolve;

import com.github.linyuzai.plugin.core.concept.Plugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.core.handle.HandlerDependency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 字节数组插件解析器。
 * 用于提取插件内容位为字节数组，
 * 最底层的存储结构，方便后续类型转换。
 */
@Getter
@RequiredArgsConstructor
@HandlerDependency(EntryResolver.class)
public class ContentResolver extends AbstractPluginResolver<Plugin.Entry, Plugin.Content> {

    @Override
    public boolean doFilter(Plugin.Entry source, PluginContext context) {
        return source.getContent() != null;
    }

    @Override
    public Plugin.Content doResolve(Plugin.Entry source, PluginContext context) {
        return source.getContent();
    }

    @Override
    public Object getInboundKey() {
        return Plugin.Entry.class;
    }

    @Override
    public Object getOutboundKey() {
        return Plugin.Content.class;
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
