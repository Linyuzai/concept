package com.github.linyuzai.event.core.inherit;

import java.util.Map;

public interface Inheritable {

    Map<String, ? extends Endpoint> getEndpoints();

    interface Endpoint {

        String getInherit();
    }
}
