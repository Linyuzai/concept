package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

@AllArgsConstructor
@NoArgsConstructor
public class ASourceWriter implements SourceWriter {

    private int bufferSize = 1024 * 1024;

    @Override
    public boolean support(Source source, Range range, Charset charset, DownloadContext context) {
        return charset == null || range == null && source.getLength() <= bufferSize;
    }

    @Override
    public void write(InputStream is, OutputStream os, Range range, Charset charset) throws IOException {
        int len;
        byte[] bytes = new byte[bufferSize];
        while ((len = is.read(bytes)) > 0) {
            os.write(bytes, 0, len);
        }
    }
}
