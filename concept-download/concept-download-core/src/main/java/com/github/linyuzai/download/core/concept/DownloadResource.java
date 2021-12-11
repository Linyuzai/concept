package com.github.linyuzai.download.core.concept;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

@Getter
@Setter
public abstract class DownloadResource implements Downloadable {

    private String name;

    private Charset charset;

    private long length;

    private boolean cacheEnabled;

    private String cachePath;
}
