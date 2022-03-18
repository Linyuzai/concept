package com.github.linyuzai.plugin.jar.match;

import com.github.linyuzai.plugin.core.exception.PluginException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstanceObjectMatcher extends InstanceMatcher {

    public InstanceObjectMatcher(Class<?> target) {
        super(target);
    }

    @Override
    public Object convert(Map<String, Object> map) {
        List<Object> list = new ArrayList<>(map.values());
        if (map.size() > 1) {
            throw new PluginException("More than one instance matched: " + list);
        }
        return list.get(0);
    }
}
