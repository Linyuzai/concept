package com.github.linyuzai.download.core.source;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

@Getter
@Setter
public abstract class AbstractSource implements Source {

    private String name;

    private Charset charset;

    private long length;
}
