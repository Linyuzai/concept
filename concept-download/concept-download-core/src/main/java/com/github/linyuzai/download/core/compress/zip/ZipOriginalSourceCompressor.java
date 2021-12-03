package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.CompressedSource;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.compress.OriginalSourceCompressor;
import com.github.linyuzai.download.core.original.OriginalSource;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@AllArgsConstructor
public class ZipOriginalSourceCompressor implements OriginalSourceCompressor {

    @Override
    public boolean support(String format, DownloadContext context) {
        return CompressFormat.ZIP.equals(format);
    }

    @Override
    public CompressedSource compress(OriginalSource source, SourceWriter writer, String cachePath, boolean delete, DownloadContext context) throws IOException {
        boolean cacheEnable = context.getOptions().isCompressCacheEnabled();
        if (cacheEnable) {
            File file = new File(cachePath, "compress.zip");
            if (file.exists()) {
                return new ZipFileCompressedSource(file);
            }
            try (FileOutputStream fos = new FileOutputStream(file);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                source.write(zos, null, writer, new Source.WriteHandler() {
                    @Override
                    public void handle(OriginalSource.Target target) throws IOException {
                        zos.putNextEntry(new ZipEntry(target.getPath()));
                        Source.WriteHandler.super.handle(target);
                        zos.closeEntry();
                    }
                });
            }
            return new ZipFileCompressedSource(file);
        } else {
            return new ZipCompressedSource(source);
        }
    }
}
