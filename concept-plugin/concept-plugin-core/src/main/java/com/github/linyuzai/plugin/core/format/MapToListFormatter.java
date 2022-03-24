package com.github.linyuzai.plugin.core.format;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MapToListFormatter extends AbstractPluginFormatter<Map<?, ?>, List<Object>> {

    private Class<?> listClass;

    public MapToListFormatter() {
        this(List.class);
    }

    @Override
    public List<Object> doFormat(Map<?, ?> source) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(source.values());
        return null;
    }
}
