package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.DownloadSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@AllArgsConstructor
public class UncompressedSource implements CompressedSource {

    private DownloadSource source;

    private SourceWriter writer;

    @Override
    public String getName() {
        return source.getName();
    }

    @Override
    public void write(OutputStream os, Range range, SourceWriter writer) throws IOException {
        source.write(os, range, writer);
    }
}
