package com.github.linyuzai.connection.loadbalance.core.concept;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public abstract class AbstractConnection implements Connection {

    private String host;

    private int post;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof AbstractConnection) {
            AbstractConnection that = (AbstractConnection) o;
            return post == that.post && Objects.equals(host, that.host);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, post);
    }
}
