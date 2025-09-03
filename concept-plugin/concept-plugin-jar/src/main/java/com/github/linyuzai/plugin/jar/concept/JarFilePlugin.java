package com.github.linyuzai.plugin.jar.concept;

import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.zip.concept.ZipFilePlugin;
import lombok.Getter;
import lombok.Setter;

import java.util.jar.JarFile;

/**
 * jar文件插件
 */
@Getter
@Setter
public class JarFilePlugin extends ZipFilePlugin implements JarPlugin {

    private PluginClassLoader pluginClassLoader;

    public JarFilePlugin(JarFile jarFile) {
        super(jarFile);
    }

    @Override
    public JarFile getZipFile() {
        return (JarFile) super.getZipFile();
    }

    @Override
    protected JarPluginEntry createPluginEntry(String path, String name) {
        return new JarFilePluginEntry(this, name, path, getZipFile());
    }

    /**
     * 缓存包和类的内容创建插件类加载器
     */
    @Override
    public void onPrepare(PluginContext context) {
        prepareClassLoader(context);
    }

    /**
     * 关闭插件类加载器
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            pluginClassLoader.close();
        } catch (Throwable ignore) {
        }
    }
}
