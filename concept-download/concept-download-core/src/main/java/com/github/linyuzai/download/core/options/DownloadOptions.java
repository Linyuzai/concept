package com.github.linyuzai.download.core.options;

import lombok.Builder;
import lombok.Value;

import java.nio.charset.Charset;
import java.util.Collection;

@Value
@Builder(builderClassName = "Builder")
public class DownloadOptions {

    /**
     * 需要下载是数据对象
     */
    Object source;

    /**
     * 下载是数据对象缓存
     */
    boolean sourceCacheEnabled;

    /**
     * 下载显示的文件名称
     */
    String filename;

    /**
     * Content-Type Header
     */
    String contentType;

    boolean compressEnabled;

    /**
     * 压缩格式
     */
    String compressFormat;

    /**
     * 压缩目录时是否保持之前的结构
     */
    //boolean compressKeepStruct;

    /**
     * 压缩文件缓存
     */
    boolean compressCacheEnabled;

    boolean skipCompressOnSingleSource;

    Charset charset;

    Object request;

    Object response;

    /**
     * 内部使用
     */
    Object args;

    /**
     * 额外数据
     */
    Object extra;
}
