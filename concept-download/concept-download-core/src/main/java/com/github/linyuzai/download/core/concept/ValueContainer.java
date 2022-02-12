package com.github.linyuzai.download.core.concept;

import com.github.linyuzai.download.core.web.reactive.DownloadMono;

/**
 * 值容器，用于方法返回值。
 * 由于 webflux 对返回值类型有校验，
 * 所以需要将返回值包装一层来通过校验。
 *
 * @see DownloadMono
 */
public interface ValueContainer {

    /**
     * 获得真实的返回值。
     *
     * @return 真实的返回值
     */
    Object getValue();
}
