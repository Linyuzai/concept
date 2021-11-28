package com.github.linyuzai.download.servlet.request;

import com.github.linyuzai.download.core.request.DownloadRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
@AllArgsConstructor
public class ServletDownloadRequest implements DownloadRequest {

    private HttpServletRequest request;
}
