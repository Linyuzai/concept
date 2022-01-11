package com.github.linyuzai.download.core.web;

/**
 * 请求，如http / Request, such as http
 */
public interface DownloadRequest {

    /**
     * 通过名称获得请求头 / Get header by name
     *
     * @param name 名称 / Name
     * @return 请求头 / Header
     */
    String getHeader(String name);
}
