package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.zip.concept.ZipStreamPlugin;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * jar流插件
 */
@Getter
@Setter
public class JarStreamPlugin extends ZipStreamPlugin implements JarPlugin {

    private PluginClassLoader pluginClassLoader;

    public JarStreamPlugin(Supplier<InputStream> supplier) {
        super(supplier);
    }

    @Override
    protected JarPluginEntry createPluginEntry(String path, String name, byte[] bytes) {
        return new JarStreamPluginEntry(this, name, path, supplier, bytes);
    }

    /**
     * 缓存包和类的内容创建插件类加载器
     */
    @Override
    public void onLoad(PluginContext context) {
        prepareClassLoader(context);
    }

    /**
     * 关闭插件类加载器
     */
    @Override
    public void onUnload() {
        super.onUnload();
        try {
            pluginClassLoader.close();
        } catch (Throwable ignore) {
        }
    }
}
