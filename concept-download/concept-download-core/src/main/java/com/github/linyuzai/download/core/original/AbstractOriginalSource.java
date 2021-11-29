package com.github.linyuzai.download.core.original;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;

@Getter
@Setter
public abstract class AbstractOriginalSource implements OriginalSource {

    private String name;

    private Charset charset;

    private boolean asyncLoad;
}
