package $PACKAGE$.sample.config;

import $PACKAGE$.sample.domain.user.*;
import $PACKAGE$.sample.domain.user.remote.RemoteUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户领域相关配置
 */
@Configuration
public class DomainUserConfiguration {

    /**
     * 远程用户存储
     * <p>
     * 注意方法名和 user 模块不能重复
     */
    @Bean
    @ConditionalOnMissingBean
    public UserRepository userRepositoryInSample() {
        return new RemoteUserRepository();
    }
}
