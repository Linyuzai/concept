package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressor;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.IOException;
import java.io.OutputStream;

@Getter
@RequiredArgsConstructor
public class ZipArchiveSourceCompressor extends AbstractSourceCompressor<ZipArchiveOutputStream> {

    private final ArchiveStreamFactory archiveStreamFactory;

    public ZipArchiveSourceCompressor() {
        this(ArchiveStreamFactory.DEFAULT);
    }

    @Override
    public ZipArchiveOutputStream newOutputStream(OutputStream os, Source source, String format, DownloadContext context) throws IOException {
        //ParallelScatterZipCreator creator;
        try {
            ZipArchiveOutputStream stream = archiveStreamFactory
                    .createArchiveOutputStream(ArchiveStreamFactory.ZIP, os);
            init(stream);
            return stream;
        } catch (ArchiveException e) {
            throw new IOException(e);
        }
    }

    protected void init(ZipArchiveOutputStream os) {
        //os.setEncoding("UTF-8");
    }

    @Override
    public boolean supportEncryption(DownloadContext context) {
        return false;
    }

    @Override
    public void beforeWrite(Part part, ZipArchiveOutputStream os) throws IOException {
        os.putArchiveEntry(new ZipArchiveEntry(part.getPath()));
    }

    @Override
    public void afterWrite(Part part, ZipArchiveOutputStream os) throws IOException {
        os.closeArchiveEntry();
    }

    @Override
    public String getSuffix(String format) {
        return CompressFormat.ZIP_SUFFIX;
    }

    @Override
    public String getContentType(String format) {
        return ContentType.Application.X_ZIP_COMPRESSED;
    }

    @Override
    public String[] getFormats() {
        return new String[]{CompressFormat.ZIP};
    }
}
