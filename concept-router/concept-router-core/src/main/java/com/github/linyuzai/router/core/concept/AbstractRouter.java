package com.github.linyuzai.router.core.concept;

import lombok.Getter;
import lombok.Setter;

/**
 * {@link Router} 的抽象类
 */
@Getter
@Setter
public abstract class AbstractRouter implements Router {

    private String id;

    private boolean forced;

    private boolean enabled;

    private long timestamp;
}
