package com.github.linyuzai.download.core.options;

import lombok.Builder;
import lombok.Value;

import java.nio.charset.Charset;
import java.util.Collection;

@Value
@Builder(builderClassName = "Builder")
public class DownloadOptions {

    /**
     * 需要下载是原始数据对象
     */
    Object original;

    /**
     * 原始数据对象是否开启缓存
     * 主要用于一些需要网络IO操作，如http等数据源的缓存
     * 本地文件没有缓存的概念，或者说文件本身就是缓存
     */
    boolean originalCacheEnabled;

    /**
     * 原始数据对象的缓存目录分组
     * 如用户指定缓存目录为/home/download/cache，该属性配置images
     * 则最终缓存文件会存储在/home/download/cache/images目录下
     * 方便根据不同业务指定不同的缓存文件夹，防止文件重名等问题
     */
    String originalCacheGroup;

    /**
     * 下载显示的文件名称，在浏览器上下载下来显示的名称
     */
    String filename;

    /**
     * Content-Type Header
     */
    String contentType;

    /**
     * 是否开启压缩
     */
    boolean compressEnabled;

    /**
     * 压缩格式
     */
    String compressFormat;

    /**
     * 当只有一个数据源是否跳过，不进行压缩
     * 单个文件目录也会压缩
     */
    boolean skipCompressOnSingle;

    /**
     * 压缩目录时是否保持之前的结构
     */
    //boolean compressKeepStruct;

    /**
     * 是否开启压缩文件缓存
     * 开启后，将会先在本地生成一个压缩文件
     * 否则，讲直接写入输出流
     * 如果文件小可以不开启
     */
    boolean compressCacheEnabled;

    /**
     * 压缩文件的缓存目录分组
     * 同 {@link DownloadOptions#originalCacheGroup}
     */
    String compressCacheGroup;

    String compressCacheName;

    /**
     * 是否删除压缩文件
     * 如果压缩文件每次的内容都不一样需要删除，防止存在缓存而不会再次压缩
     */
    boolean deleteCompressCache;

    /**
     * 如果指定了编码，会使用字符流的方式读写
     */
    Charset charset;

    /**
     * 提供支持任意Request的接口
     */
    Object request;

    /**
     * 提供支持任意Response的接口
     */
    Object response;

    /**
     * 内部使用参数，自定义数据请使用 {@link DownloadOptions#extra}
     */
    Object args;

    /**
     * 额外数据
     */
    Object extra;
}
