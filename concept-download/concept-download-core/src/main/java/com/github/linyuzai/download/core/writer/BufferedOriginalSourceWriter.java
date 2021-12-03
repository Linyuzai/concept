package com.github.linyuzai.download.core.writer;

import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.range.Range;
import com.github.linyuzai.download.core.source.Source;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.*;
import java.nio.charset.Charset;

@AllArgsConstructor
@NoArgsConstructor
public class BufferedOriginalSourceWriter implements SourceWriter {

    private int bufferSize = 1024;

    @Override
    public boolean support(Source source, Range range, DownloadContext context) {
        return source instanceof OriginalSource;
    }

    @Override
    public void write(InputStream is, OutputStream os, Range range, Charset charset) throws IOException {
        if (charset == null) {
            int len;
            byte[] bytes = new byte[bufferSize];
            while ((len = is.read(bytes)) > 0) {
                os.write(bytes, 0, len);
            }
        } else {
            InputStreamReader isr = new InputStreamReader(is, charset);
            OutputStreamWriter osw = new OutputStreamWriter(os, charset);
            int len;
            char[] chars = new char[bufferSize];
            while ((len = isr.read(chars)) > 0) {
                osw.write(chars, 0, len);
            }
        }
    }

    @Override
    public int getOrder() {
        return ORDER_ORIGINAL_SOURCE;
    }
}
