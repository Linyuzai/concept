package com.github.linyuzai.download.core.source;

import com.github.linyuzai.download.core.writer.SourceWriter;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

@Getter
@Setter
public abstract class AbstractDownloadSource implements DownloadSource {

    private String name;

    private Charset charset;

    private boolean asyncLoad;
}
