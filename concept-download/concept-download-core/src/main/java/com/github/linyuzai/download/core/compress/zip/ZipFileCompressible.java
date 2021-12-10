package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.Compressed;
import com.github.linyuzai.download.core.source.file.FileSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.*;

@AllArgsConstructor
public class ZipFileCompressible extends Compressed {

    private final File file;

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public long getLength() {
        return file.length();
    }

    @Override
    public void setLength(long length) {

    }

    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    @Override
    public void setCacheEnabled(boolean cacheEnabled) {

    }

    @Override
    public String getCachePath() {
        return file.getParent();
    }

    @Override
    public void setCachePath(String cachePath) {

    }

    @Override
    public void write(OutputStream os, Range range, DownloadWriter writer, WriteHandler handler) throws IOException {
        FileSource source = new FileSource.Builder().file(file).build();
        source.write(os, range, writer, handler);
    }

    @Override
    public void deleteCache() {
        boolean delete = file.delete();
    }
}
