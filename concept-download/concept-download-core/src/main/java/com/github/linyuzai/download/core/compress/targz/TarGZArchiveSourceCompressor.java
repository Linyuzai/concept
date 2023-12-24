package com.github.linyuzai.download.core.compress.targz;

import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.compress.tar.TarArchiveSourceCompressor;
import com.github.linyuzai.download.core.web.ContentType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class TarGZArchiveSourceCompressor extends TarArchiveSourceCompressor {

    @Override
    protected OutputStream wrapper(OutputStream os) throws IOException {
        return new GZIPOutputStream(os);
    }

    @Override
    public String getSuffix(String format) {
        return CompressFormat.TAR_GZ_SUFFIX;
    }

    @Override
    public String getContentType(String format) {
        return ContentType.Application.X_TAR_GZ;
    }

    @Override
    public String[] getFormats() {
        return new String[]{CompressFormat.TAR_GZ};
    }
}
