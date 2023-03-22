package $PACKAGE$.user.config;

import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.user.domain.user.*;
import $PACKAGE$.user.domain.user.rpc.InnerUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 在 config 包中手动注入
 * <p>
 * 方便在 application 启动模块中扩展
 */
@Configuration
public class DomainUserConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UserController userController() {
        return new UserController();
    }

    /**
     * 由于这里注入了 InnerUserApi
     * <p>
     * basic 模块中的 FeignUserApi 将不会被注入
     * <p>
     * 用户相关的功能将会直接通过本地调用实现
     */
    @Bean
    public UserApi userApi() {
        return new InnerUserApi();
    }
}
