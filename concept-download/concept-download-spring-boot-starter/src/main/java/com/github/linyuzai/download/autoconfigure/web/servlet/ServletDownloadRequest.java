package com.github.linyuzai.download.autoconfigure.web.servlet;

import com.github.linyuzai.download.core.web.DownloadRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;

/**
 * 持有 {@link HttpServletRequest} 的 {@link DownloadRequest}，用于 webmvc。
 */
@Getter
@RequiredArgsConstructor
public class ServletDownloadRequest implements DownloadRequest {

    private final HttpServletRequest request;

    @Override
    public String getHeader(String name) {
        return request.getHeader(name);
    }
}
