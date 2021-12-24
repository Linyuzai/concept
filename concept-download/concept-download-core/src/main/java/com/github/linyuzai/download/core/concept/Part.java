package com.github.linyuzai.download.core.concept;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;

/**
 * 一个下载对象可能有多个部分 / A download object may have multiple parts
 */
public interface Part {

    /**
     * @return 输入流 / Input stream
     * @throws IOException I/O exception
     */
    InputStream getInputStream() throws IOException;

    /**
     * @return 名称 / name
     */
    String getName();

    /**
     * @return 路径 / Path
     */
    default String getPath() {
        return getName();
    }

    /**
     * @return Content Type
     */
    String getContentType();

    /**
     * @return 编码 / charset
     */
    Charset getCharset();

    /**
     * @return 字节数 / bytes count
     */
    Long getLength();

    default Collection<Part> getChildren() {
        return Collections.emptyList();
    }
}
