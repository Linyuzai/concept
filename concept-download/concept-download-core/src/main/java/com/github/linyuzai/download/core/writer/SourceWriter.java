package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.range.Range;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface SourceWriter {

    void write(InputStream is, OutputStream os, Range range, Charset charset) throws IOException;
}
