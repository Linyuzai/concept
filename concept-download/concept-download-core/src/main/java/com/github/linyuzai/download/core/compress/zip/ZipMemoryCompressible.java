package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.Compressed;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.DownloadWriter;

import java.io.*;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipMemoryCompressible extends Compressed {

    private final Source source;

    public ZipMemoryCompressible(Source source, String name) {
        this.source = source;
        if (name == null || name.isEmpty()) {
            setName(getName0());
        } else {
            setName(name);
        }
        setCharset(source.getCharset());
        setLength(source.getLength());
    }

    @Override
    public boolean isCacheEnabled() {
        return false;
    }

    @Override
    public void setCacheEnabled(boolean cacheEnabled) {

    }

    @Override
    public String getCachePath() {
        return null;
    }

    @Override
    public void setCachePath(String cachePath) {

    }

    public String getName0() {
        String name = source.getName();
        if (name == null) {
            return null;
        }
        int lastIndexOf = name.lastIndexOf(CompressFormat.DOT);
        if (lastIndexOf == -1) {
            return name + CompressFormat.ZIP_SUFFIX;
        } else {
            return name.substring(0, lastIndexOf) + CompressFormat.ZIP_SUFFIX;
        }
    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        Charset charset = source.getCharset();
        ZipOutputStream zos = charset == null ? new ZipOutputStream(os) : new ZipOutputStream(os, charset);
        source.write(zos, range, writer, handler);
    }

    protected void write(ZipOutputStream zos, Range range, DownloadWriter writer) throws IOException {
        source.write(zos, range, writer, new WriteHandler() {
            @Override
            public void handle(Part part) throws IOException {
                zos.putNextEntry(new ZipEntry(part.getPath()));
                WriteHandler.super.handle(part);
                zos.closeEntry();
            }
        });
    }
}