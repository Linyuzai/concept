package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressedSource implements CompressedSource {

    private final OriginalSource source;

    private final boolean cacheEnabled;

    private final String cachePath;

    public ZipCompressedSource(OriginalSource source, boolean cacheEnabled, String cachePath) {
        this.source = source;
        this.cacheEnabled = cacheEnabled;
        this.cachePath = cachePath;
        if (cacheEnabled && (cachePath == null || cachePath.isEmpty())) {
            throw new IllegalArgumentException("Cache enabled but cache path is null");
        }
    }

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
    public void write(OutputStream os, Range range, SourceWriter writer) throws IOException {
        if (cacheEnabled) {
            String name = getName();
            File file = new File(cachePath, name == null ? "null." + CompressFormat.ZIP : name);
            try (FileOutputStream fos = new FileOutputStream(file);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                write(zos, range, writer);
            }
            try (FileInputStream fis = new FileInputStream(file)) {
                writer.write(fis, os, range, null);
            }
        } else {
            try (ZipOutputStream zos = new ZipOutputStream(os)) {
                write(zos, range, writer);
            }
        }
        //throw new UnsupportedOperationException("Zip with no cache is not support range");
    }

    protected void write(ZipOutputStream zos, Range range, SourceWriter writer) throws IOException {
        source.write(zos, range, writer, new OriginalSource.WriteHandler() {
            @Override
            public void handle(OriginalSource.Target target) throws IOException {
                zos.putNextEntry(new ZipEntry(target.getPath()));
                OriginalSource.WriteHandler.super.handle(target);
                zos.closeEntry();
            }
        });
    }
}
