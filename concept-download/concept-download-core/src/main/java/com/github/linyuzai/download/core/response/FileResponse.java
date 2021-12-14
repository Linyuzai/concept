package com.github.linyuzai.download.core.response;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@AllArgsConstructor
public class FileResponse implements DownloadResponse {

    private File file;

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public void setStatusCode(int statusCode) {

    }

    @Override
    public void setFilename(String filename) {

    }

    @Override
    public void setContentType(String contentType) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    @Override
    public void addHeader(String name, String value) {

    }
}
