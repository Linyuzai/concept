package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.concept.DownloadMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSource extends DownloadMetadata implements Source {

    private boolean asyncLoad;
}
