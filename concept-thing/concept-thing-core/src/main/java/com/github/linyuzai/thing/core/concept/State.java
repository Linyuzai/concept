package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.common.Containable;
import com.github.linyuzai.thing.core.operation.Operation;

public interface State extends Containable {

    Label label();

    Thing thing();

    <T> T value();

    Operation update(Object value);
}
