package com.github.linyuzai.download.core.compress.zip;

import com.github.linyuzai.download.core.compress.AbstractSourceCompressor;
import com.github.linyuzai.download.core.compress.CompressFormat;
import com.github.linyuzai.download.core.concept.Part;
import com.github.linyuzai.download.core.context.DownloadContext;
import com.github.linyuzai.download.core.options.DownloadOptions;
import com.github.linyuzai.download.core.source.Source;
import com.github.linyuzai.download.core.web.ContentType;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.IOException;
import java.io.OutputStream;

public class Zip4jSourceCompressor extends AbstractSourceCompressor<ZipOutputStream> {

    @Override
    public ZipOutputStream newOutputStream(OutputStream os, Source source, String format, DownloadContext context) throws IOException {
        DownloadOptions options = DownloadOptions.get(context);
        String password = options.getCompressPassword();
        if (password == null || password.isEmpty()) {
            return new ZipOutputStream(os);
        } else {
            return new ZipOutputStream(os, password.toCharArray());
        }
    }

    @Override
    public boolean supportEncryption(DownloadContext context) {
        return true;
    }

    @Override
    public void beforeWrite(Part part, ZipOutputStream os, DownloadContext context) throws IOException {
        ZipParameters parameters = new ZipParameters();
        parameters.setFileNameInZip(part.getPath());
        initZipParameters(parameters, context);
        os.putNextEntry(parameters);
    }

    protected void initZipParameters(ZipParameters parameters, DownloadContext context) {
        DownloadOptions options = DownloadOptions.get(context);
        String password = options.getCompressPassword();
        if (password != null && !password.isEmpty()) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);
        }
    }

    @Override
    public void afterWrite(Part part, ZipOutputStream os, DownloadContext context) throws IOException {
        os.closeEntry();
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
