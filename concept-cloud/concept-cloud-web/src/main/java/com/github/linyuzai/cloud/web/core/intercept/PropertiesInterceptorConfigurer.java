package com.github.linyuzai.cloud.web.core.intercept;

import com.github.linyuzai.cloud.web.core.CloudWebProperties;
import com.github.linyuzai.cloud.web.core.concept.WebConcept;
import com.github.linyuzai.cloud.web.core.concept.WebConceptConfigurer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class PropertiesInterceptorConfigurer implements WebConceptConfigurer {

    private final CloudWebProperties properties;

    @Override
    public void configure(WebConcept concept) {
        Set<WebInterceptor.Scope> scopes = new HashSet<>(Arrays.asList(
                WebInterceptor.Scope.REQUEST,
                WebInterceptor.Scope.RESPONSE,
                WebInterceptor.Scope.ERROR));
        addInterceptors(properties.getIntercept().getPredicate(), scopes, concept);

        Set<WebInterceptor.Scope> requestScopes = Collections.singleton(WebInterceptor.Scope.REQUEST);
        addInterceptors(properties.getIntercept().getRequest().getPredicate(), requestScopes, concept);

        Set<WebInterceptor.Scope> responseScopes = Collections.singleton(WebInterceptor.Scope.RESPONSE);
        addInterceptors(properties.getIntercept().getResponse().getPredicate(), responseScopes, concept);

        Set<WebInterceptor.Scope> errorScopes = Collections.singleton(WebInterceptor.Scope.ERROR);
        addInterceptors(properties.getIntercept().getError().getPredicate(), errorScopes, concept);
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
                scopes,
                properties.isNegate(),
                properties.getOrder());
    }

    private static class PropertiesRequestPathPatternPredicateWebInterceptor
            extends RequestPathPatternPredicateWebInterceptor {

        private final Set<Scope> scopes;

        private final int order;

        public PropertiesRequestPathPatternPredicateWebInterceptor(Collection<String> paths,
                                                                   Set<Scope> scopes,
                                                                   boolean negate,
                                                                   int order) {
            super(paths);
            this.scopes = scopes;
            this.order = order;
            if (negate) {
                negate();
            }
        }

        @Override
        public Set<Scope> getScopes() {
            return scopes;
        }

        @Override
        public int getOrder() {
            return order;
        }
    }
}
