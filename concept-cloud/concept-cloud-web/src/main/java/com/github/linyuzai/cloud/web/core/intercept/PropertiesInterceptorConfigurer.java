package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.concept.WebConceptConfigurer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * 基于配置文件添加拦截器的配置器
 */
@Getter
@RequiredArgsConstructor
public class PropertiesInterceptorConfigurer implements WebConceptConfigurer {

    private final CloudWebProperties properties;

    /**
     * 生成配置文件中的断言配置对应的拦截器并添加
     */
    @Override
    public void configure(WebConcept concept) {
        Set<WebInterceptor.Scope> scopes = new HashSet<>(Arrays.asList(
                WebInterceptor.Scope.REQUEST,
                WebInterceptor.Scope.RESPONSE));
        addInterceptors(properties.getIntercept().getPredicate(), scopes, concept);

        Set<WebInterceptor.Scope> requestScopes = Collections.singleton(WebInterceptor.Scope.REQUEST);
        addInterceptors(properties.getIntercept().getRequest().getPredicate(), requestScopes, concept);

        Set<WebInterceptor.Scope> responseScopes = Collections.singleton(WebInterceptor.Scope.RESPONSE);
        addInterceptors(properties.getIntercept().getResponse().getPredicate(), responseScopes, concept);
    }

    private void addInterceptors(CloudWebProperties.InterceptProperties.PredicateProperties predicate,
                                 Set<WebInterceptor.Scope> scopes,
                                 WebConcept concept) {
        predicate.getRequestPath().forEach(it -> concept.addInterceptor(newInstance(it, scopes)));
    }

    private PropertiesRequestPathPatternPredicateWebInterceptor newInstance(
            CloudWebProperties.InterceptProperties.RequestPathProperties properties,
            Set<WebInterceptor.Scope> scopes) {
        return new PropertiesRequestPathPatternPredicateWebInterceptor(
                properties.getPatterns(),
                properties.isUseResponseBodyAsWebResult(),
                scopes,
                properties.isNegate(),
                properties.getOrder());
    }

    private static class PropertiesRequestPathPatternPredicateWebInterceptor
            extends RequestPathPatternPredicateWebInterceptor {

        private final int order;

        public PropertiesRequestPathPatternPredicateWebInterceptor(Collection<String> paths,
                                                                   boolean useResponseBodyAsWebResult,
                                                                   Set<Scope> scopes,
                                                                   boolean negate,
                                                                   int order) {
            super(paths);
            setUseResponseBodyAsWebResult(useResponseBodyAsWebResult);
            setScopes(scopes);
            this.order = order;
            if (negate) {
                negate();
            }
        }

        @Override
        public int getOrder() {
            return order;
        }
    }
}
