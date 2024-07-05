package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.handle.extract.convert.AbstractPluginConvertor;
import com.github.linyuzai.plugin.jar.handle.resolve.JarClass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

@Getter
@RequiredArgsConstructor
public class BeanConvertor extends AbstractPluginConvertor<JarClass, Object> {

    private final ApplicationContext applicationContext;

    @Override
    public Object doConvert(JarClass jarClass) {
        return applicationContext.getAutowireCapableBeanFactory().createBean(jarClass.get());
    }
}
