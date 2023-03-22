package $PACKAGE$.basic.autoconfigure.feign;

import $PACKAGE$.basic.rpc.api.user.UserApi;
import $PACKAGE$.basic.rpc.feign.user.FeignUserApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过 spring.factories 注入，便于结合 {@link ConditionalOnMissingBean} 进行扩展
 */
@Configuration
@EnableFeignClients(basePackages = "$PACKAGE$.basic.rpc.feign")
public class FeignAutoConfiguration {

    /**
     * 如果和 user 模块打包在一起
     * <p>
     * user 模块中 InnerUserApi 将被注入
     * <p>
     * UserApi 将会走本地用户模块
     * <p>
     * 如果没有打包在一起
     * <p>
     * 将会注入该实例
     * <p>
     * 通过 Feign 来进行调用
     */
    @Bean
    @ConditionalOnMissingBean
    public UserApi userApi() {
        return new FeignUserApi();
    }
}
