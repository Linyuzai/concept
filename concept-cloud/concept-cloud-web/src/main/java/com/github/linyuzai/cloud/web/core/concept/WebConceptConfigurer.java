package com.github.linyuzai.cloud.web.core.concept;

/**
 * {@link WebConcept} 配置器
 * <p>
 * 在 {@link WebConcept} 实例化之后会被调用
 */
public interface WebConceptConfigurer {

    /**
     * 可自定义配置
     */
    void configure(WebConcept concept);
}
