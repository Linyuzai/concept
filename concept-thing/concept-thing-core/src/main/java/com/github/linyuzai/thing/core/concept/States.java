package com.github.linyuzai.thing.core.concept;

import com.github.linyuzai.thing.core.common.Container;
import com.github.linyuzai.thing.core.operation.Operation;

import java.util.Map;

public interface States extends Container<State> {

    Operation update(Map<String, Object> values);
}
