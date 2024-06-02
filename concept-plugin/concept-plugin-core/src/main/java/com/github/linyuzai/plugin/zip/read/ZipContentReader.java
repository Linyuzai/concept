package com.github.linyuzai.plugin.zip.read;

import com.github.linyuzai.plugin.core.read.ContentReader;
import com.github.linyuzai.plugin.core.read.PluginReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@Getter
@RequiredArgsConstructor
public class ZipContentReader implements ContentReader {

    private final ZipFile file;

    @SneakyThrows
    @Override
    public Object read(Object name) {
        ZipEntry entry = file.getEntry(String.valueOf(name));
        if (entry == null) {
            return null;
        }
        return file.getInputStream(entry);
    }
}
