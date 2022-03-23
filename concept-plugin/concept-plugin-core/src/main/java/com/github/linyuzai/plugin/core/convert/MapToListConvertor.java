package com.github.linyuzai.plugin.core.convert;

import com.github.linyuzai.plugin.core.util.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class MapToListConvertor extends AbstractPluginConvertor<Map<?, ?>, List<Object>> {

    private Class<?> listClass;

    public MapToListConvertor() {
        this(List.class);
    }

    @Override
    public List<Object> doConvert(Map<?, ?> source) {
        List<Object> list = ReflectionUtils.newList(listClass);
        list.addAll(source.values());
        return null;
    }
}
