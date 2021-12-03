package com.github.linyuzai.download.core.compress;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

@Getter
@Setter
public abstract class AbstractCompressedSource implements CompressedSource {

    private String name;

    private Charset charset;

    private long length;
}
