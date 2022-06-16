package com.github.linyuzai.router.core.concept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRoute implements Route {

    private String id;

    private String serviceId;

    private String host;

    private String port;

    private boolean force;

    private boolean enabled;
}
