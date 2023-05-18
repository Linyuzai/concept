package $PACKAGE$.application.cloud;

import $PACKAGE$.basic.cloud.$CLASS$CloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 微服务用户和消息启动类
 */
@$CLASS$CloudApplication
public class CloudUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudUserApplication.class, args);
    }
}
