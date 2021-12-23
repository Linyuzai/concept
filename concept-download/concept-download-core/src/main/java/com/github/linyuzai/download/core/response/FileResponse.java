package com.github.linyuzai.download.core.response;

import lombok.AllArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 文件响应 / File response
 * 可将结果输出到文件 / The results can be output to a file
 */
@AllArgsConstructor
public class FileResponse implements DownloadResponse {

    private File file;

    /**
     * 使用文件输出流 / Use output stream of file {@link FileOutputStream}
     *
     * @return 文件输出流 / Output stream of file
     * @throws IOException I/O exception
     */
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
    public void setContentLength(Long contentLength) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    @Override
    public void addHeader(String name, String value) {

    }
}
