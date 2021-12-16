package com.github.linyuzai.download.core.compress;

import com.github.linyuzai.download.core.concept.DownloadResource;
import lombok.Getter;
import lombok.Setter;

/**
 * 压缩的抽象类 / Abstract class of Compression
 * 方便以后扩展 / Convenient for future expansion
 */
@Getter
@Setter
public abstract class AbstractCompression extends DownloadResource implements Compression {

}
