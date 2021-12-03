package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class ZipCompressedSource implements CompressedSource {

    private final OriginalSource source;

    @Override
    public String getName() {
        String name = source.getName();
        if (name == null) {
            return null;
        }
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return name + "." + CompressFormat.ZIP;
        } else {
            return name.substring(0, lastIndexOf) + "." + CompressFormat.ZIP;
        }
    }

    @Override
    public Charset getCharset() {
        return source.getCharset();
    }

    @Override
    public long getLength() {
        return source.getLength();
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer, WriteHandler handler) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        source.write(zos, range, writer, handler);
    }

    protected void write(ZipOutputStream zos, Range range, SourceWriter writer) throws IOException {
        source.write(zos, range, writer, new Source.WriteHandler() {
            @Override
            public void handle(Source.Target target) throws IOException {
                zos.putNextEntry(new ZipEntry(target.getPath()));
                Source.WriteHandler.super.handle(target);
                zos.closeEntry();
            }
        });
    }
}
