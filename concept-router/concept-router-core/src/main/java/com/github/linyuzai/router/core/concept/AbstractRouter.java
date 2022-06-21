package com.github.linyuzai.router.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRouter implements Router {

    private String id;

    private boolean forced;

    private boolean enabled;

    private long timestamp;
}
