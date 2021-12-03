package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.cache.CacheableSource;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.original.file.FileOriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;

@AllArgsConstructor
public class ZipFileCompressedSource implements CompressedSource, CacheableSource {

    private final File file;

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public Charset getCharset() {
        return null;
    }

    @Override
    public long getLength() {
        return file.length();
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        FileOriginalSource source = new FileOriginalSource.Builder().file(file).build();
        source.write(os, range, writer, handler);
    }

    @Override
    public void deleteCache() {
        boolean delete = file.delete();
    }
}
