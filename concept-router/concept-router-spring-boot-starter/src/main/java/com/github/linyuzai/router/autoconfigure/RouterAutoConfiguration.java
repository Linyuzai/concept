package com.github.linyuzai.router.autoconfigure;

import com.github.linyuzai.router.autoconfigure.annotation.ConditionalOnRouterEnabled;
import com.github.linyuzai.router.autoconfigure.banner.ConceptRouterBanner;
import com.github.linyuzai.router.autoconfigure.matcher.AntPathRequestRouterMatcher;
import com.github.linyuzai.router.autoconfigure.event.ApplicationRouterEventPublisher;
import com.github.linyuzai.router.autoconfigure.properties.RouterProperties;
import com.github.linyuzai.router.core.concept.DefaultRouterConcept;
import com.github.linyuzai.router.core.concept.RouterConcept;
import com.github.linyuzai.router.core.event.RouterEventListener;
import com.github.linyuzai.router.core.event.RouterEventPublisher;
import com.github.linyuzai.router.core.locator.RouterLocator;
import com.github.linyuzai.router.core.locator.DefaultServiceRouterLocator;
import com.github.linyuzai.router.core.matcher.RouterMatcher;
import com.github.linyuzai.router.core.repository.InMemoryRouterRepository;
import com.github.linyuzai.router.core.repository.JacksonLocalRouterRepository;
import com.github.linyuzai.router.core.repository.RouterRepository;
import com.github.linyuzai.router.core.utils.RouterLogger;
import lombok.AllArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@ConditionalOnRouterEnabled
@EnableConfigurationProperties(RouterProperties.class)
@Configuration(proxyBeanMethods = false)
public class RouterAutoConfiguration {

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public RouterRepository routerRepository(Registration registration,
                                             RouterProperties properties) {
        RouterProperties.RepositoryProperties.RepositoryType type = properties.getRepository().getType();
        switch (type) {
            case MEMORY:
                return new InMemoryRouterRepository();
            case LOCAL:
                return new JacksonLocalRouterRepository(() -> {
                    String path = properties.getRepository().getLocal().getPath();
                    String servicePath = new File(path, registration.getServiceId()).getAbsolutePath();
                    return new File(servicePath, registration.getHost() + "_" + registration.getPort())
                            .getAbsolutePath();
                });
            default:
                throw new IllegalArgumentException("Repository type must in " +
                        Arrays.toString(RouterProperties.RepositoryProperties.RepositoryType.values()));
        }
    }

    @Bean
    public RouterRepositoryInitializer routerRepositoryInitializer(RouterRepository repository) {
        return new RouterRepositoryInitializer(repository);
    }

    @AllArgsConstructor
    public static class RouterRepositoryInitializer implements ApplicationRunner {

        private RouterRepository routerRepository;

        @Override
        public void run(ApplicationArguments args) throws Exception {
            routerRepository.initialize();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterMatcher routerMatcher() {
        return new AntPathRequestRouterMatcher();
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterLocator routerLocator() {
        return new DefaultServiceRouterLocator();
    }

    @Bean
    @ConditionalOnProperty(name = "concept.router.logger.enabled", havingValue = "true", matchIfMissing = true)
    public RouterLogger routerLogger() {
        Log log = LogFactory.getLog(RouterLogger.class);
        return new RouterLogger(log::info, log::error);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterEventPublisher routerEventPublisher(ApplicationEventPublisher eventPublisher,
                                                     List<RouterEventListener> listeners) {
        return new ApplicationRouterEventPublisher(eventPublisher, listeners);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterConcept routerConcept(RouterRepository repository,
                                       RouterMatcher matcher,
                                       RouterLocator locator,
                                       RouterEventPublisher eventPublisher,
                                       RouterProperties properties) {
        if (properties.getBanner().isEnabled()) {
            ConceptRouterBanner.print();
        }
        return new DefaultRouterConcept.Builder()
                .repository(repository)
                .matcher(matcher)
                .locator(locator)
                .eventPublisher(eventPublisher)
                .build();
    }
}
