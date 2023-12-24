package com.github.linyuzai.download.core.compress.tar;

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
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.IOException;
import java.io.OutputStream;

@Getter
@RequiredArgsConstructor
public class TarArchiveSourceCompressor extends AbstractSourceCompressor<TarArchiveOutputStream> {

    private final ArchiveStreamFactory archiveStreamFactory;

    public TarArchiveSourceCompressor() {
        this(ArchiveStreamFactory.DEFAULT);
    }

    @Override
    public TarArchiveOutputStream newOutputStream(OutputStream os, Source source, String format, DownloadContext context) throws IOException {
        //ParallelScatterZipCreator creator;
        try {
            TarArchiveOutputStream stream = archiveStreamFactory
                    .createArchiveOutputStream(ArchiveStreamFactory.TAR, wrapper(os));
            init(stream);
            return stream;
        } catch (ArchiveException e) {
            throw new IOException(e);
        }
    }

    protected void init(TarArchiveOutputStream os) {
        os.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);
        os.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
        //os.setAddPaxHeadersForNonAsciiNames(true);
    }

    @Override
    public boolean supportPassword(DownloadContext context) {
        return false;
    }

    @Override
    public void beforeWrite(Part part, TarArchiveOutputStream os) throws IOException {
        os.putArchiveEntry(new TarArchiveEntry(part.getPath()));
    }

    @Override
    public void afterWrite(Part part, TarArchiveOutputStream os) throws IOException {
        os.closeArchiveEntry();
    }

    @Override
    public String getSuffix(String format) {
        return CompressFormat.TAR_SUFFIX;
    }

    @Override
    public String getContentType(String format) {
        return ContentType.Application.X_TAR;
    }

    @Override
    public String[] getFormats() {
        return new String[]{CompressFormat.TAR};
    }
}
