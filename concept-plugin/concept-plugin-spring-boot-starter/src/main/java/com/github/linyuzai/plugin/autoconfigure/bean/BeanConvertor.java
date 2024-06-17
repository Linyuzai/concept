package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.handle.extract.convert.AbstractPluginConvertor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@Getter
@RequiredArgsConstructor
public class BeanConvertor extends AbstractPluginConvertor<Class<?>, Object> {

    private final ApplicationContext applicationContext;

    @Override
    public Object doConvert(Class<?> clazz) {
        return applicationContext.getAutowireCapableBeanFactory().createBean(clazz);
    }
}
