package com.github.linyuzai.plugin.autoconfigure.bean;

import com.github.linyuzai.plugin.core.handle.extract.TypeMetadataPluginExtractor;
import com.github.linyuzai.plugin.core.handle.extract.convert.PluginConvertor;
import com.github.linyuzai.plugin.core.handle.extract.match.PluginMatcher;
import com.github.linyuzai.plugin.core.type.TypeMetadata;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;

/**
 * 实例提取器
 *
 * @param <T> 实例类型
 */
@Getter
@RequiredArgsConstructor
public abstract class BeanExtractor<T> extends TypeMetadataPluginExtractor<T> {

    private final ApplicationContext applicationContext;

    /**
     * 返回一个 {@link BeanMatcher}
     *
     * @param metadata    {@link TypeMetadata}
     * @param annotations 注解
     * @return {@link BeanMatcher}
     */
    @Override
    public PluginMatcher getMatcher(TypeMetadata metadata, Annotation[] annotations) {
        Class<?> elementClass = metadata.getElementClass();
        return new BeanMatcher(elementClass, annotations);
    }

    @Override
    public PluginConvertor getConvertor(TypeMetadata metadata, Annotation[] annotations) {
        return new BeanConvertor(applicationContext);
    }
}
