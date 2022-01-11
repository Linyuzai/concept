package com.github.linyuzai.download.core.web.servlet;

import com.github.linyuzai.download.core.web.DownloadRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest实现 / implementations by HttpServletRequest
 */
@Getter
@AllArgsConstructor
public class ServletDownloadRequest implements DownloadRequest {

    private HttpServletRequest request;

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
