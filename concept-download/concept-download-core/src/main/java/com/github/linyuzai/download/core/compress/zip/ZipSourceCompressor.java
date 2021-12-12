package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressor;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.concept.Downloadable;
import com.github.linyuzai.download.core.writer.DownloadWriter;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class ZipSourceCompressor extends AbstractSourceCompressor {

    @Override
    public boolean support(String format, DownloadContext context) {
        return CompressFormat.ZIP.equals(format);
    }

    @Override
    public void doCompress(Source source, OutputStream os, DownloadWriter writer) throws IOException {
        Charset charset = source.getCharset();
        try (ZipOutputStream zos = charset == null ? new ZipOutputStream(os) : new ZipOutputStream(os, charset)) {
            source.write(zos, null, writer, new Downloadable.WriteHandler() {
                @Override
                public void handle(Downloadable.Part part) throws IOException {
                    zos.putNextEntry(new ZipEntry(part.getPath()));
                    Downloadable.WriteHandler.super.handle(part);
                    zos.closeEntry();
                }
            });
        }
    }

    @Override
    public String getSuffix() {
        return CompressFormat.ZIP_SUFFIX;
    }
}
