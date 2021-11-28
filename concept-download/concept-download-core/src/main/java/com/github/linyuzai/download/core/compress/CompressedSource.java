package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.writer.SourceWriter;

import java.io.IOException;
import java.io.OutputStream;

public interface CompressedSource {

    String getName();

    void write(OutputStream os, Range range, SourceWriter writer) throws IOException;
}
