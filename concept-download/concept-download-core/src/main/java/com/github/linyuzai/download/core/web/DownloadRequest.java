package com.github.linyuzai.download.core.web;

/**
 * 下载请求。
 */
public interface DownloadRequest {

    /**
     * 通过名称获得请求头。
     *
     * @param name 名称
     * @return 请求头
     */
    String getHeader(String name);

    /**
     * 通过 'Range' 请求头获得 {@link Range}。
     *
     * @return {@link Range} 或 null
     */
    default Range getRange() {
        String range = getHeader("Range");
        return Range.header(range);
    }
}
