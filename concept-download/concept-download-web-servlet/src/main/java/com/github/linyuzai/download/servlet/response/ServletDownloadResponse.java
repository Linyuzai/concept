package com.github.linyuzai.download.servlet.response;

import com.github.linyuzai.download.core.response.DownloadResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Getter
@AllArgsConstructor
public class ServletDownloadResponse implements DownloadResponse {

    private HttpServletResponse response;

    @Override
    public OutputStream getOutputStream() throws IOException {
        return response.getOutputStream();
    }

    @Override
    public void setStatusCode(int statusCode) {
        response.setStatus(statusCode);
    }

    @Override
    public void setFilename(String filename) {
        String encodeFilename = null;
        try {
            encodeFilename = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + encodeFilename);
    }

    @Override
    public void setContentType(String contentType) {
        response.setContentType(contentType);
    }

    @Override
    public void setHeader(String name, String value) {
        response.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        response.addHeader(name, value);
    }
}
