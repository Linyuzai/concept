package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
public class UncompressedSource implements CompressedSource {

    private OriginalSource source;

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
