package com.github.linyuzai.connection.loadbalance.core.event;

import com.github.linyuzai.connection.loadbalance.core.concept.Connection;
import lombok.Getter;

@Deprecated
@Getter
public class TypeRedefineEvent extends AbstractConnectionEvent {

    private final String type;

    public TypeRedefineEvent(Connection connection, String type) {
        super(connection);
        this.type = type;
    }
}
