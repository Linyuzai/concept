package com.github.linyuzai.router.core.concept;

import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
public abstract class AbstractRouter implements Router {

    private String id;

    private String serviceId;

    private String host;

    private String port;

    private boolean forced;

    private boolean enabled;

    private long timestamp;

    public static abstract class Source implements Router.Source {

        public abstract String getServiceId();

        public abstract URI getUri();
    }

    public static abstract class Location implements Router.Location {

        public abstract String getServiceId();

        public abstract String getHost();

        public abstract int getPort();
    }
}
