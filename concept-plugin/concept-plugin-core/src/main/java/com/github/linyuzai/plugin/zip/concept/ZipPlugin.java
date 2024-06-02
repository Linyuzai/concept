package com.github.linyuzai.plugin.zip.concept;

import com.github.linyuzai.plugin.core.concept.AbstractPlugin;
import com.github.linyuzai.plugin.core.context.PluginContext;
import com.github.linyuzai.plugin.zip.read.ZipContentReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Getter
@RequiredArgsConstructor
public class ZipPlugin extends AbstractPlugin {

    private final String path;

    protected ZipFile file;

    @Override
    public Object getId() {
        return path;
    }

    @Override
    public Collection<Object> collectContent(PluginContext context) {
        return getFile().stream()
                .map(ZipEntry::getName)
                //测试之后win环境中读取也不会存在\\的分隔符
                //.map(it -> it.replaceAll("\\\\", "/"))
                .filter(it -> !it.endsWith("/"))
                .collect(Collectors.toList());
    }

    protected void addDefaultReaders() {
        addReaders(new ZipContentReader(getFile()));
    }

    /**
     * 准备，通过 {@link JarURLConnection} 来读取 jar 内容
     */
    @SneakyThrows
    @Override
    public void onPrepare(PluginContext context) {
        this.file = createFile();
        addDefaultReaders();
    }

    protected ZipFile createFile() throws IOException {
        return new ZipFile(path);
    }

    /**
     * 释放资源，关闭资源文件的引用
     */
    @Override
    public void onRelease(PluginContext context) {
        if (file != null) {
            try {
                file.close();
            } catch (Throwable ignore) {
            }
            file = null;
        }
    }

    @Override
    public String toString() {
        return "ZipPlugin(" + getPath() + ")";
    }
}
