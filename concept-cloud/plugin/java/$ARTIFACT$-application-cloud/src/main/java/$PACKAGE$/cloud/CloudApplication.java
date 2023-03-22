package $PACKAGE$.cloud;

import $PACKAGE$.basic.$CLASS$CloudApplication;
import org.springframework.boot.SpringApplication;

/**
 * 微服务启动模块示例
 */
@$CLASS$CloudApplication
public class CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }
}
