package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.DownloadResource;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象的数据源 / Abstract source
 */
@Getter
@Setter
public abstract class AbstractSource extends DownloadResource implements Source {

    private boolean asyncLoad;
}
