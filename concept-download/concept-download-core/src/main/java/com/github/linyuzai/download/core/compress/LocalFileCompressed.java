package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.file.FileSource;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
public class LocalFileCompressed extends Compressed {

    private final File file;

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public long getLength() {
        return file.length();
    }

    @Override
    public boolean isCacheEnabled() {
        return true;
    }

    @Override
    public String getCachePath() {
        return file.getParent();
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
